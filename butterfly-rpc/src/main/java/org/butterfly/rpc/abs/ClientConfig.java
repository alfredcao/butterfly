package org.butterfly.rpc.abs;

import org.butterfly.rpc.abs.policy.RetryPolicy;

import java.io.Serializable;

/**
 * 客户端配置
 * @author caozhen
 * @date 2019-10-11 17:38
 */
public interface ClientConfig extends Serializable {
    /**
     * 获取客户端名称
     * @return 客户端名称
     */
    String getName();

    /**
     * 获取服务端地址
     * @return 服务端地址
     */
    String getServerAddress();

    /**
     * 获取服务端端口
     * @return 服务端端口
     */
    int getServerPort();

    /**
     * 获取客户端连接的超时时间(秒)
     * @return 客户端连接的超时时间(秒)
     */
    default int timeoutSeconds(){
        return 60;
    }

    /**
     * 获取重试策略
     * @return 重试策略
     */
    RetryPolicy getRetryPolicy();
}
