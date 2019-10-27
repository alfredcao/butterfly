package org.butterfly.rpc.component.mina;

import org.butterfly.rpc.component.AbstractServerConfig;

/**
 * netty服务端配置
 * @author Timmy
 * @date 2019-10-15 20:02
 */
public class MinaServerConfig extends AbstractServerConfig {
    public MinaServerConfig() {
    }

    public MinaServerConfig(String name, int port){
        this.setName(name);
        this.setPort(port);
    }
}
