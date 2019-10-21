package org.butterfly.rpc.component.netty;

import org.butterfly.rpc.component.AbstractClientConfig;

/**
 * netty客户端配置
 * @author caozhen
 * @date 2019-10-21 09:33
 */
public class NettyClientConfig extends AbstractClientConfig {
    public NettyClientConfig(String name, String serverAddress, int serverPort) {
        this.setName(name);
        this.setServerAddress(serverAddress);
        this.setServerPort(serverPort);
    }
}
