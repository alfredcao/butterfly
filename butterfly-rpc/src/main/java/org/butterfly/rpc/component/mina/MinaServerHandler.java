package org.butterfly.rpc.component.mina;

import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.butterfly.common.util.CheckUtil;
import org.butterfly.rpc.abs.ServerConfig;
import org.butterfly.rpc.model.constant.Constant;
import org.butterfly.rpc.model.dto.RpcMsg;
import org.butterfly.rpc.model.enumeration.RpcMsgType;

import java.net.SocketAddress;

/**
 * Created by Timmy on 2019/10/26.
 */
@Slf4j
public class MinaServerHandler extends IoHandlerAdapter {
    private ServerConfig config;

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        log.info("mina server exceptionCaught ",cause);
    }

    public MinaServerHandler(ServerConfig config){
        this.setConfig(config);
    }

    public void setConfig(ServerConfig config){
        CheckUtil.checkNotNull(config, "server config");
        this.config = config;
    }


    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        //业务代码在这里编写处理
        /*
        byte[] b = (byte[])message;
        StringBuffer stringBuffer = new StringBuffer();
        for(int i = 0; i < b.length; i++){
            stringBuffer.append((char) b[i]); //可以根据需要自己改变类型
        }
        session.write(message);
        log.info("mina server receive:{}",stringBuffer.toString());
        */
        RpcMsg msg = (RpcMsg)message;
        log.info("mina server receive:{}",msg.toString());
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        log.info("Mina server session created");
        super.sessionCreated(session);
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        SocketAddress address = session.getRemoteAddress();
        log.info("{}Mina通道注册至服务器【{}】成功！通道信息 -> 通道ID：{}，远程地址 -> {}", Constant.LOG_PREFIX, this.config.getName(), session.getId(), address.toString());

    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        SocketAddress address = session.getRemoteAddress();
        log.info("{}Mina通道关闭至服务器【{}】成功！通道信息 -> 通道ID：{}，远程地址 -> {}", Constant.LOG_PREFIX, this.config.getName(), session.getId(), address.toString());

    }

}
