package org.butterfly.rpc.component.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.butterfly.rpc.abs.Server;
import org.butterfly.rpc.abs.ServerConfig;
import org.butterfly.rpc.component.AbstractServer;

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

    @Override
    protected void doStart() throws Throwable {
        final ServerConfig config = this.config;

        this.boss = new NioEventLoopGroup();
        this.worker = new NioEventLoopGroup();
        this.serverBootstrap = new ServerBootstrap();

        // 配置服务器
        this.serverBootstrap.group(this.boss, this.worker)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<ServerChannel>() {
                    @Override
                    protected void initChannel(ServerChannel serverChannel) throws Exception {
                        serverChannel.pipeline().addLast(new NettyServerHandler(config));
                    }
                });
        this.serverBootstrap.bind(this.config.getPort()).sync();
    }

    @Override
    protected void doStop() throws Throwable {
        if(this.boss != null){
            this.boss.shutdownGracefully().syncUninterruptibly();
        }
        if(this.worker != null){
            this.worker.shutdownGracefully().syncUninterruptibly();
        }
    }

    public static void main(String[] args) {
        Server server = new NettyServer();
        server.init(new NettyServerConfig("测试服务器", 20000));
        server.start();
    }
}
