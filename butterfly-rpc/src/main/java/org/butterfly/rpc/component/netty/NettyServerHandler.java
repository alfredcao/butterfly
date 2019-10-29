package org.butterfly.rpc.component.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.butterfly.common.util.CheckUtil;
import org.butterfly.rpc.abs.ServerConfig;
import org.butterfly.rpc.model.constant.Constant;
import org.butterfly.rpc.model.dto.RpcMsg;

import java.net.SocketAddress;

/**
 * 服务端处理器
 * @author caozhen
 * @date 2019-10-15 09:31
 */
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcMsg> {
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
        ctx.fireChannelRegistered();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcMsg rpcMsg) throws Exception {
        log.info("获取到RPC消息：" + rpcMsg.toString());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        SocketAddress address = channel.remoteAddress();
        ChannelId channelId = channel.id();
        log.info("{}通道从服务器【{}】取消注册成功！通道信息 -> 通道ID：{}，远程地址 -> {}", Constant.LOG_PREFIX, this.config.getName(), channelId.asLongText(), address.toString());
        ctx.fireChannelUnregistered();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
        ctx.fireChannelReadComplete();
    }
}
