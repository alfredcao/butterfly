package org.butterfly.rpc.component.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.butterfly.common.util.CheckUtil;
import org.butterfly.common.util.UUIDUtil;
import org.butterfly.rpc.abs.ClientConfig;
import org.butterfly.rpc.model.constant.Constant;
import org.butterfly.rpc.model.dto.RpcMsg;
import org.butterfly.rpc.model.enumeration.RpcMsgStatusCode;
import org.butterfly.rpc.model.enumeration.RpcMsgType;

/**
 * 握手请求
 * @author alfredcao
 * @date 2019-10-30 20:27
 */
@Slf4j
public class NettyHandShakeReqHandler extends SimpleChannelInboundHandler<RpcMsg> {
    private ClientConfig config;

    public NettyHandShakeReqHandler(ClientConfig config){
        CheckUtil.checkNotNull(config, "client config");
        this.config = config;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        RpcMsg handshakeReqRpcMsg = RpcMsg.handshakeReqRpcMsg(ctx.channel().id().asLongText(), UUIDUtil.uuid());
        ctx.writeAndFlush(handshakeReqRpcMsg);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcMsg rpcMsg) throws Exception {
        if(rpcMsg.getHeader().getType() == RpcMsgType.HANDSHAKE_RESP.getCode()){
            // 握手响应
            if(rpcMsg.getHeader().getStatusCode() == RpcMsgStatusCode.OK.getCode()){
                // 握手成功
                log.info("{}客户端【{}】与服务器【地址 -> {}，端口 -> {}】握手成功！通道ID -> {}", Constant.LOG_PREFIX, this.config.getName(), this.config.getServerAddress(), this.config.getServerPort(), ctx.channel().id().asLongText());
                ctx.fireChannelRead(rpcMsg);
            } else {
                // 握手失败，关闭通道
                log.info("{}客户端【{}】与服务器【地址 -> {}，端口 -> {}】握手失败，即将关闭相关通道！通道ID -> {}", Constant.LOG_PREFIX, this.config.getName(), this.config.getServerAddress(), this.config.getServerPort(), ctx.channel().id().asLongText());
                ctx.close();
            }
        } else {
            ctx.fireChannelRead(rpcMsg);
        }
    }
}
