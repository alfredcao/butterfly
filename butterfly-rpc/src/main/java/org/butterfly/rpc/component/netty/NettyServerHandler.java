package org.butterfly.rpc.component.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.butterfly.common.util.CheckUtil;
import org.butterfly.rpc.abs.ServerConfig;

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
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 读取到请求，异步处理
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
