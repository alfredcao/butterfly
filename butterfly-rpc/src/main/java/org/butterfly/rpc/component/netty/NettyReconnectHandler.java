package org.butterfly.rpc.component.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.butterfly.common.util.CheckUtil;
import org.butterfly.rpc.model.constant.Constant;

/**
 * netty断线重连处理器
 * @author alfredcao
 * @date 2019-11-02 11:12
 */
@Slf4j
public class NettyReconnectHandler extends ChannelInboundHandlerAdapter {
    private NettyClient client;

    public NettyReconnectHandler(NettyClient client){
        CheckUtil.checkNotNull(client, "netty client");
        this.client = client;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        new Thread(() -> {
            if(this.client.getConfig().getRetryPolicy().canRetry()){
                try {
                    log.error("{}客户端【{}】连接服务器【地址 -> {}，端口 -> {}】通道 INACTIVE!!！进行第{}次重连...",
                            Constant.LOG_PREFIX, this.client.getConfig().getName(), this.client.getConfig().getServerAddress(),
                            this.client.getConfig().getServerPort(), this.client.getConfig().getRetryPolicy().getRetryCount());
                    this.client.doConnect();
                } catch (Throwable t){
                    this.client.processConnectException(t);
                }
            } else {
                this.client.processConnectException(new Exception("channel inactive"));
            }
        }).start();
        super.channelInactive(ctx);
    }
}
