package org.butterfly.rpc.component;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.butterfly.common.util.CheckUtil;
import org.butterfly.rpc.abs.Client;
import org.butterfly.rpc.abs.ClientConfig;
import org.butterfly.rpc.abs.codec.Deserializer;
import org.butterfly.rpc.abs.codec.Serializer;
import org.butterfly.rpc.model.constant.Constant;

/**
 * 客户端实现基类
 * @author caozhen
 * @date 2019-10-16 09:51
 */
@Slf4j
public abstract class AbstractClient implements Client {
    private final StatusInfo statusInfo = new StatusInfo(Status.NEW, null);
    @Getter
    private Serializer serializer;
    @Getter
    private Deserializer deserializer;

    public AbstractClient() {
    }

    public AbstractClient(Serializer serializer, Deserializer deserializer) {
        this.serializer = serializer;
        this.deserializer = deserializer;
    }

    @Getter
    protected ClientConfig config;

    @Override
    public boolean init(ClientConfig config) {
        synchronized (this) {
            if(!Status.NEW.equals(this.statusInfo.getStatus())){
                throw new IllegalStateException(String.format("客户端非%s状态不可执行初始化操作！", Status.NEW.getName()));
            }
            try {
                this.config = config;
                this.doInit();
                this.statusInfo.setStatus(Status.INIT);
                return true;
            } catch (Throwable t){
                log.error("初始化客户端异常！", t);
                this.statusInfo.setStatus(Status.INIT_EXCEPTION);
                this.statusInfo.setCause(t);
            }
        }
        return false;
    }

    @Override
    public boolean connect() {
        synchronized (this) {
            if(!Status.INIT.equals(this.statusInfo.getStatus())){
                throw new IllegalStateException(String.format("客户端非%s状态不可执行连接操作！", Status.INIT.getName()));
            }
            try {
                this.doConnect();
                this.statusInfo.setStatus(Status.CONNECT);
                return true;
            } catch (Throwable t){
                this.processConnectException(t);
            }
        }
        return false;
    }

    /**
     * 处理连接异常
     * @param t
     */
    protected void processConnectException(Throwable t){
        log.error("{}客户端【{}】连接服务器【地址 -> {}，端口 -> {}】异常！不再进行重连操作！", Constant.LOG_PREFIX, this.config.getName(), this.config.getServerAddress(), this.config.getServerPort(), t);
        this.statusInfo.setStatus(Status.CONNECT_EXCEPTION);
        this.statusInfo.setCause(t);
    }

    @Override
    public boolean disconnect() {
        synchronized (this) {
            if(!Status.CONNECT.equals(this.statusInfo.getStatus())){
                throw new IllegalStateException(String.format("客户端非%s状态不可执行断开连接操作！", Status.CONNECT.getName()));
            }
            try {
                this.doDisconnect();
                this.statusInfo.setStatus(Status.DISCONNECT);
                return true;
            } catch (Throwable t){
                this.processDisconnectException(t);
            }
        }
        return false;
    }

    /**
     * 处理连接异常
     * @param t
     */
    protected void processDisconnectException(Throwable t){
        log.error("{}客户端【{}】断开连接服务器【地址 -> {}，端口 -> {}】异常！", Constant.LOG_PREFIX, this.config.getName(), this.config.getServerAddress(), this.config.getServerPort(), t);
        this.statusInfo.setStatus(Status.DISCONNECT_EXCEPTION);
        this.statusInfo.setCause(t);
    }

    /**
     * 执行初始化操作
     * @throws Throwable
     */
    protected abstract void doInit() throws Throwable;

    /**
     * 执行连接操作
     * @throws Throwable
     */
    protected abstract void doConnect() throws Throwable;

    /**
     * 执行停止操作
     * @throws Throwable
     */
    protected abstract void doDisconnect() throws Throwable;

    @Override
    public StatusInfo status() {
        return this.statusInfo;
    }
}
