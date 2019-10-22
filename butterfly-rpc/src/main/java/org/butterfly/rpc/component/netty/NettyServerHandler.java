package org.butterfly.rpc.component.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.butterfly.common.util.CheckUtil;
import org.butterfly.rpc.abs.ServerConfig;
import org.butterfly.rpc.model.constant.Constant;

import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * 服务端处理器
 * @author caozhen
 * @date 2019-10-15 09:31
 */
@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    private ServerConfig config;

    public NettyServerHandler(ServerConfig config){
        this.setConfig(config);
    }

    public void setConfig(ServerConfig config){
        CheckUtil.checkNotNull(config, "server config");
        this.config = config;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        SocketAddress address = channel.remoteAddress();
        ChannelId channelId = channel.id();
        log.info("{}通道注册至服务器【{}】成功！通道信息 -> 通道ID：{}，远程地址 -> {}", Constant.LOG_PREFIX, this.config.getName(), channelId.asLongText(), address.toString());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        SocketAddress address = channel.remoteAddress();
        ChannelId channelId = channel.id();
        log.info("{}通道从服务器【{}】取消注册成功！通道信息 -> 通道ID：{}，远程地址 -> {}", Constant.LOG_PREFIX, this.config.getName(), channelId.asLongText(), address.toString());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 读取到请求，异步处理
        ByteBuf byteBuf = (ByteBuf)msg;
        byte[] msgArray = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(msgArray);
        log.info("{}服务器【{}】接收到请求：{}", Constant.LOG_PREFIX, this.config.getName(), new String(msgArray, StandardCharsets.UTF_8));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
