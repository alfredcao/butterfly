package org.butterfly.rpc.component.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.butterfly.rpc.abs.Client;
import org.butterfly.rpc.abs.ClientConfig;
import org.butterfly.rpc.abs.codec.Deserializer;
import org.butterfly.rpc.abs.codec.Serializer;
import org.butterfly.rpc.component.AbstractClient;
import org.butterfly.rpc.component.codec.hessian.HessianDeserializer;
import org.butterfly.rpc.component.codec.hessian.HessianSerializer;
import org.butterfly.rpc.model.constant.Constant;
import org.butterfly.rpc.model.dto.RpcHeader;
import org.butterfly.rpc.model.dto.RpcMsg;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * netty客户端
 * @author caozhen
 * @date 2019-10-16 14:39
 */
@Slf4j
public class NettyClient extends AbstractClient {
    private EventLoopGroup boss; // 主线程池
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
    }

    @Override
    protected void doConnect() throws Throwable {
        try {
            Bootstrap bootstrap = new Bootstrap();
            final Serializer serializer = this.getSerializer();
            final Deserializer deserializer = this.getDeserializer();
            final int maxRecBytes = this.maxRecBytes();
            bootstrap.group(this.boss).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyRpcMsgDecoder(maxRecBytes, deserializer));
                            socketChannel.pipeline().addLast(new NettyRpcMsgEncoder(serializer));
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(this.config.getServerAddress(), this.config.getServerPort()).sync();
            log.info("{}客户端【{}】已成功连接服务器【地址 -> {}，端口 -> {}】 成功！", Constant.LOG_PREFIX, this.config.getName(), this.config.getServerAddress(), this.config.getServerPort());
            this.channel = channelFuture.channel();
        } catch (Throwable t){
            this.releaseResource();
            throw t;
        }
    }

    @Override
    protected void doDisconnect() throws Throwable {
        this.releaseResource();
    }

    private void releaseResource(){
        if(this.boss != null){
            this.boss.shutdownGracefully().syncUninterruptibly();
            this.boss = null;
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

    public static void main(String[] args) throws Exception {
        Client client = new NettyClient(new HessianSerializer(), new HessianDeserializer());
        ClientConfig config = new NettyClientConfig("测试客户端", "localhost", 20000);
        client.init(config);
        client.connect();
        // 组装RPC消息
        RpcMsg rpcMsg = new RpcMsg();
        RpcHeader rpcHeader = new RpcHeader();
        rpcHeader.setType((byte)3);
        rpcHeader.setServiceId("serviceId");
        rpcHeader.setSessionId("sessionId");
        rpcHeader.setRequestId("requestId");
        Map<String, Object> extendAttributes = new HashMap<>();
        extendAttributes.put("key1", "value1");
        rpcHeader.setExtendAttributes(extendAttributes);
        rpcMsg.setHeader(rpcHeader);
        rpcMsg.setBody(config);

        client.send(rpcMsg);
        log.info("已发出信息！");
        client.disconnect();
    }
}
