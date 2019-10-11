package org.butterfly.rpc.component;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.butterfly.rpc.abs.Server;
import org.butterfly.rpc.abs.ServerConfig;

/**
 * 服务器实现基类
 * @author alfredcao
 * @date 2019-10-11 22:30
 */
@Slf4j
public abstract class AbstractServer implements Server {
    private final StatusInfo statusInfo = new StatusInfo(Status.NEW, null);
    @Getter
    protected ServerConfig config;

    @Override
    public final boolean init(ServerConfig config) {
        synchronized (this) {
            if(!Status.NEW.equals(this.statusInfo.getStatus())){
                throw new IllegalStateException(String.format("服务器非%s状态不可执行初始化操作！", Status.NEW.getName()));
            }
            try {
                this.config = config;
                this.doInit();
                return true;
            } catch (Throwable e){
                log.error("初始化服务器异常！", e);
                this.statusInfo.setStatus(Status.INIT_EXCEPTION);
                this.statusInfo.setCause(e);
            }
        }
        return false;
    }

    @Override
    public final boolean start() {
        synchronized (this) {
            if(!Status.INIT.equals(this.statusInfo)){
                throw new IllegalStateException(String.format("服务器非%s状态不可执行启动操作！", Status.INIT.getName()));
            }
            try {
                this.doStart();
                return true;
            } catch (Throwable e){
                log.error("启动服务器异常！", e);
                this.statusInfo.setStatus(Status.START_EXCEPTION);
                this.statusInfo.setCause(e);
            }
        }
        return false;
    }

    @Override
    public final boolean stop() {
        synchronized (this) {
            if(!Status.START.equals(this.statusInfo)){
                throw new IllegalStateException(String.format("服务器非%s状态不可执行停止操作！", Status.START.getName()));
            }
            try {
                this.doStop();
                return true;
            } catch (Throwable e){
                log.error("停止服务器异常！", e);
                this.statusInfo.setStatus(Status.STOP_EXCEPTION);
                this.statusInfo.setCause(e);
            }
        }
        return false;
    }

    /**
     * 执行初始化操作
     * @throws Throwable
     */
    protected void doInit() throws Throwable {}

    /**
     * 执行启动操作
     * @throws Throwable
     */
    protected void doStart() throws Throwable {}

    /**
     * 执行停止操作
     * @throws Throwable
     */
    protected void doStop() throws Throwable {}

    @Override
    public StatusInfo status() {
        return this.statusInfo;
    }
}
