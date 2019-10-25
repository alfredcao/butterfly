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
}
