package org.butterfly.rpc.component.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.butterfly.rpc.abs.ServerConfig;
import org.butterfly.rpc.model.constant.Constant;
import org.butterfly.rpc.model.dto.RpcMsg;
import org.butterfly.rpc.model.enumeration.RpcMsgType;

/**
 * 心跳响应
 * @author alfredcao
 * @date 2019-10-30 20:18
 */
@Slf4j
public class NettyHeartBeatRespHandler extends SimpleChannelInboundHandler<RpcMsg> {
    private ServerConfig config;
    public NettyHeartBeatRespHandler(ServerConfig config){
        this.config = config;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcMsg rpcMsg) throws Exception {
        if(rpcMsg.getHeader().getType() == RpcMsgType.HEARTBEAT_REQ.getCode()){
            // 心跳请求
            log.debug("{}服务器【{}】接收到心跳请求！通道ID -> {}，远程地址 -> {}，RPC消息 -> {}", Constant.LOG_PREFIX, this.config.getName(), ctx.channel().id().asLongText(), ctx.channel().remoteAddress(), rpcMsg.toString());
            RpcMsg heartBeatRespRpcMsg = RpcMsg.heartBeatRespRpcMsg(ctx.channel().id().asLongText(), rpcMsg.getHeader().getRequestId());
            ctx.writeAndFlush(heartBeatRespRpcMsg);
        } else {
            ctx.fireChannelRead(rpcMsg);
        }
    }
}
