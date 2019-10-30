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

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 心跳请求
 * @author alfredcao
 * @date 2019-10-30 17:50
 */
@Slf4j
public class NettyHeartBeatReqHandler extends SimpleChannelInboundHandler<RpcMsg> {
    private ClientConfig config;

    public NettyHeartBeatReqHandler(ClientConfig config){
        CheckUtil.checkNotNull(config, "client config");
        this.config = config;
    }
    public static final long DEFAULT_INITIAL_DELAY = 0;
    public static final long DEFAULT_INTERVAL = 5;
    public static final TimeUnit DEFAULT_UNIT = TimeUnit.SECONDS;
    private long initialDelay = DEFAULT_INITIAL_DELAY;
    private long interval = DEFAULT_INTERVAL;
    private TimeUnit unit = DEFAULT_UNIT;
    private ScheduledFuture heartBeat;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcMsg rpcMsg) throws Exception {
        if(rpcMsg.getHeader().getType() == RpcMsgType.HANDSHAKE_RESP.getCode()
                && rpcMsg.getHeader().getStatusCode() == RpcMsgStatusCode.OK.getCode()){
            // 握手成功
            this.heartBeat = ctx.executor().scheduleAtFixedRate(() -> {
                RpcMsg heartBeatReqRpcMsg = RpcMsg.heartBeatReqRpcMsg(ctx.channel().id().asLongText(), UUIDUtil.uuid());
                ctx.writeAndFlush(heartBeatReqRpcMsg);
            }, this.initialDelay, this.interval, this.unit);
        } else if(rpcMsg.getHeader().getType() == RpcMsgType.HEARTBEAT_RESP.getCode()) {
            log.debug("{}客户端【{}】接收到服务器【地址 -> {}，端口 -> {}】心跳响应！通道ID -> {}", Constant.LOG_PREFIX, this.config.getName(), this.config.getServerAddress(), this.config.getServerPort(), ctx.channel().id().asLongText());
        } else {
            ctx.fireChannelRead(rpcMsg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if(this.heartBeat != null){
            this.heartBeat.cancel(true);
            this.heartBeat = null;
        }
        ctx.fireExceptionCaught(cause);
    }
}
