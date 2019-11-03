package org.butterfly.rpc.component.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.butterfly.rpc.abs.Client;
import org.butterfly.rpc.abs.ClientConfig;
import org.butterfly.rpc.abs.codec.Deserializer;
import org.butterfly.rpc.abs.codec.Serializer;
import org.butterfly.rpc.component.AbstractClient;
import org.butterfly.rpc.component.codec.hessian.HessianDeserializer;
import org.butterfly.rpc.component.codec.hessian.HessianSerializer;
import org.butterfly.rpc.model.constant.Constant;
import org.butterfly.rpc.model.dto.RpcMsg;

/**
 * netty客户端
 * @author caozhen
 * @date 2019-10-16 14:39
 */
@Slf4j
public class NettyClient extends AbstractClient {
    private EventLoopGroup boss; // 主线程池
    private Bootstrap bootstrap; // 客户端启动器
    private Channel channel; // 通道

    public NettyClient() {
        super();
    }

    public NettyClient(Serializer serializer, Deserializer deserializer) {
        super(serializer, deserializer);
    }

    @Override
    protected void doInit() throws Throwable {
        this.boss = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
        final Serializer serializer = this.getSerializer();
        final Deserializer deserializer = this.getDeserializer();
        final int maxRecBytes = this.maxRecBytes();
        final NettyClient client = this;
        this.bootstrap.group(this.boss).channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new ReadTimeoutHandler(config.timeoutSeconds()));
                        socketChannel.pipeline().addLast(new NettyReconnectHandler(client));
                        socketChannel.pipeline().addLast(new NettyRpcMsgDecoder(maxRecBytes, deserializer));
                        socketChannel.pipeline().addLast(new NettyRpcMsgEncoder(serializer));
                        socketChannel.pipeline().addLast(new NettyHandShakeReqHandler(config));
                        socketChannel.pipeline().addLast(new NettyHeartBeatReqHandler(config));
                    }
                });
    }

    @Override
    protected void doConnect() throws Throwable {
        ChannelFuture channelFuture = this.bootstrap.connect(this.config.getServerAddress(), this.config.getServerPort());
        channelFuture.addListener(new ConnectionListener(this));
        this.channel = channelFuture.channel();
    }

    @Override
    protected void doDisconnect() throws Throwable {
        this.releaseResource();
    }

    @Override
    protected void processConnectException(Throwable t) {
        super.processConnectException(t);
        this.releaseResource();
    }

    private void releaseResource(){
        if(this.boss != null){
            this.boss.shutdownGracefully().syncUninterruptibly();
            this.boss = null;
        }
        if(this.bootstrap != null){
            this.bootstrap = null;
        }
        if(this.channel != null){
            this.channel = null;
        }
    }

    @Override
    public void send(byte[] msg) throws Exception {
        this.channel.writeAndFlush(Unpooled.buffer().writeBytes(msg)).sync();
    }

    @Override
    public void send(RpcMsg rpcMsg) throws Exception {
        this.channel.writeAndFlush(rpcMsg).sync();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.releaseResource();
    }

    private class ConnectionListener implements ChannelFutureListener {
        private NettyClient client;
        ConnectionListener(NettyClient client){
            this.client = client;
        }

        @Override
        public void operationComplete(ChannelFuture channelFuture) throws Exception {
            if (!channelFuture.isSuccess()) {
                new Thread(() -> {
                    if(this.client.getConfig().getRetryPolicy().canRetry()){
                        try {
                            log.error("{}客户端【{}】连接服务器【地址 -> {}，端口 -> {}】异常！进行第{}次重连...",
                                    Constant.LOG_PREFIX, this.client.getConfig().getName(), this.client.getConfig().getServerAddress(),
                                    this.client.getConfig().getServerPort(), this.client.getConfig().getRetryPolicy().getRetryCount());
                            this.client.doConnect();
                        } catch (Throwable t){
                            this.client.processConnectException(t);
                        }
                    } else {
                        this.client.processConnectException(channelFuture.cause());
                    }
                }).start();
            } else {
                log.info("{}客户端【{}】连接服务器【地址 -> {}，端口 -> {}】成功！",
                        Constant.LOG_PREFIX, this.client.getConfig().getName(), this.client.getConfig().getServerAddress(),
                        this.client.getConfig().getServerPort());
                this.client.getConfig().getRetryPolicy().reset();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Client client = new NettyClient(new HessianSerializer(), new HessianDeserializer());
        ClientConfig config = new NettyClientConfig("测试客户端", "localhost", 20000);
        client.init(config);
        client.connect();
//        // 组装RPC消息
//        RpcMsg rpcMsg = new RpcMsg();
//        RpcHeader rpcHeader = new RpcHeader();
//        rpcHeader.setType((byte)3);
//        rpcHeader.setServiceId("serviceId");
//        rpcHeader.setSessionId("sessionId");
//        rpcHeader.setRequestId("requestId");
//        Map<String, Object> extendAttributes = new HashMap<>();
//        extendAttributes.put("key1", "value1");
//        rpcHeader.setExtendAttributes(extendAttributes);
//        rpcMsg.setHeader(rpcHeader);
//        rpcMsg.setBody(config);
//
//        client.send(rpcMsg);
//        log.info("已发出信息！");
//        client.disconnect();
    }
}
