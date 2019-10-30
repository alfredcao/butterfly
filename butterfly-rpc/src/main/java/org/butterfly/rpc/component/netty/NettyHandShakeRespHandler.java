package org.butterfly.rpc.component.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.butterfly.rpc.abs.ServerConfig;
import org.butterfly.rpc.model.constant.Constant;
import org.butterfly.rpc.model.dto.RpcMsg;
import org.butterfly.rpc.model.enumeration.RpcMsgStatusCode;
import org.butterfly.rpc.model.enumeration.RpcMsgType;

/**
 * 握手响应
 * @author alfredcao
 * @date 2019-10-30 20:41
 */
@Slf4j
public class NettyHandShakeRespHandler extends SimpleChannelInboundHandler<RpcMsg> {
    private ServerConfig config;
    public NettyHandShakeRespHandler(ServerConfig config){
        this.config = config;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcMsg rpcMsg) throws Exception {
        if(rpcMsg.getHeader().getType() == RpcMsgType.HANDSHAKE_REQ.getCode()){
            // 握手请求
            log.info("{}服务器【{}】接收到握手请求！通道ID -> {}，远程地址 -> {}，RPC消息 -> {}", Constant.LOG_PREFIX, this.config.getName(), ctx.channel().id().asLongText(), ctx.channel().remoteAddress(), rpcMsg.toString());
            RpcMsg handshakeRespRpcMsg = RpcMsg.handshakeRespRpcMsg(ctx.channel().id().asLongText(), rpcMsg.getHeader().getRequestId(), RpcMsgStatusCode.OK.getCode());
            ctx.writeAndFlush(handshakeRespRpcMsg);
        } else {
            ctx.fireChannelRead(rpcMsg);
        }
    }
}
