package org.butterfly.rpc.abs;

/**
 * 客户端配置
 * @author caozhen
 * @date 2019-10-11 17:38
 */
public interface ClientConfig {
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
}
