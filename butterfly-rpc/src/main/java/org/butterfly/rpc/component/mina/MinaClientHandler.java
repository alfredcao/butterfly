package org.butterfly.rpc.component.mina;

import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.butterfly.common.util.CheckUtil;
import org.butterfly.rpc.abs.ClientConfig;
import org.butterfly.rpc.abs.ServerConfig;
import org.butterfly.rpc.model.constant.Constant;

import java.net.SocketAddress;

/**
 * Created by HUAWEI on 2019/10/26.
 */
@Slf4j
public class MinaClientHandler   extends IoHandlerAdapter {
    private ClientConfig config;

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        super.exceptionCaught(session, cause);
    }

    public MinaClientHandler(ClientConfig config){
        this.setConfig(config);
    }

    public void setConfig(ClientConfig config){
        CheckUtil.checkNotNull(config, "server config");
        this.config = config;
    }
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        SocketAddress address = session.getRemoteAddress();
        String text = new String((byte[]) message);
        log.info("{}MinaClient收到服务器消息【{}】", Constant.LOG_PREFIX, text);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        super.messageSent(session, message);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        SocketAddress address = session.getRemoteAddress();
        log.info("{}Mina通道关闭至服务器【{}】成功！通道信息 -> 通道ID：{}，远程地址 -> {}", Constant.LOG_PREFIX, this.config.getName(), session.getId(), address.toString());

    }
}
