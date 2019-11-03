package org.butterfly.rpc.component.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.butterfly.rpc.abs.Server;
import org.butterfly.rpc.abs.ServerConfig;
import org.butterfly.rpc.abs.codec.Deserializer;
import org.butterfly.rpc.abs.codec.Serializer;
import org.butterfly.rpc.component.AbstractServer;
import org.butterfly.rpc.component.codec.hessian.HessianDeserializer;
import org.butterfly.rpc.component.codec.hessian.HessianSerializer;
import org.butterfly.rpc.model.constant.Constant;

/**
 * netty服务器
 * @author alfredcao
 * @date 2019-10-11 23:00
 */
@Slf4j
public class NettyServer extends AbstractServer {
    private EventLoopGroup boss;
    private EventLoopGroup worker;
    private ServerBootstrap serverBootstrap;
    private Channel channel;

    public NettyServer() {
        super();
    }

    public NettyServer(Serializer serializer, Deserializer deserializer) {
        super(serializer, deserializer);
    }

    @Override
    protected void doInit() throws Throwable {
        super.doInit();
        final ServerConfig config = this.config;
        this.boss = new NioEventLoopGroup();
        this.worker = new NioEventLoopGroup();
        // 配置服务器
        this.serverBootstrap = new ServerBootstrap();
        final Serializer serializer = this.getSerializer();
        final Deserializer deserializer = this.getDeserializer();
        final int maxRecBytes = this.maxRecBytes();
        this.serverBootstrap.group(this.boss, this.worker)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .handler(new LoggingHandler())
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new ReadTimeoutHandler(config.timeoutSeconds()));
                        socketChannel.pipeline().addLast(new NettyRpcMsgDecoder(maxRecBytes, deserializer));
                        socketChannel.pipeline().addLast(new NettyRpcMsgEncoder(serializer));
                        socketChannel.pipeline().addLast(new NettyHandShakeRespHandler(config));
                        socketChannel.pipeline().addLast(new NettyHeartBeatRespHandler(config));
                        socketChannel.pipeline().addLast(new NettyServerHandler(config));
                    }
                });
    }

    @Override
    protected void doStart() throws Throwable {
        try {
            ChannelFuture channelFuture = serverBootstrap.bind(this.config.getPort()).sync();
            this.channel = channelFuture.channel();
            log.info("{}服务器【{}】已注册监听端口 {} 成功！", Constant.LOG_PREFIX, this.config.getName(), this.config.getPort());
        } catch (Throwable t){
            this.releaseResource();
            throw t;
        }
    }

    @Override
    protected void doStop() throws Throwable {
        this.releaseResource();
    }

    private void releaseResource(){
        if(this.channel != null){
            this.channel.close();
            this.channel = null;
        }
        if(this.serverBootstrap != null){
            this.serverBootstrap = null;
        }
        if(this.boss != null){
            this.boss.shutdownGracefully().syncUninterruptibly();
            this.boss = null;
        }
        if(this.worker != null){
            this.worker.shutdownGracefully().syncUninterruptibly();
            this.worker = null;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.releaseResource();
    }

    public static void main(String[] args) {
        Server server = new NettyServer(new HessianSerializer(), new HessianDeserializer());
        ServerConfig config = new NettyServerConfig("测试服务器", 20000);
        server.init(config);
        server.start();
    }
}
