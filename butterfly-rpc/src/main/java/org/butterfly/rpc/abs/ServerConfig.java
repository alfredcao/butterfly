package org.butterfly.rpc.abs;

import java.io.Serializable;

/**
 * 服务端配置
 * @author caozhen
 * @date 2019-10-11 17:37
 */
public interface ServerConfig extends Serializable {
    /**
     * 获取服务器名称
     * @return 服务器名称
     */
    String getName();

    /**
     * 获取服务器监听端口号
     * @return 端口号
     */
    int getPort();

    /**
     * 获取服务器单个连接的超时时间(秒)
     * @return 服务器单个连接的超时时间(秒)
     */
    default int timeoutSeconds(){
        return 60;
    }
}
