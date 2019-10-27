package org.butterfly.rpc.component.mina;

import org.butterfly.rpc.component.AbstractClientConfig;

/**
 * netty客户端配置
 * @author Timmy
 * @date 2019-10-21 09:33
 */
public class MinaClientConfig extends AbstractClientConfig {
    public MinaClientConfig() {
    }

    public MinaClientConfig(String name, String serverAddress, int serverPort) {
        this.setName(name);
        this.setServerAddress(serverAddress);
        this.setServerPort(serverPort);
    }
}
