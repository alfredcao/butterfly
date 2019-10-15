package org.butterfly.rpc.component.netty;

import org.butterfly.rpc.component.AbstractServerConfig;

/**
 * netty服务端配置
 * @author caozhen
 * @date 2019-10-15 20:02
 */
public class NettyServerConfig extends AbstractServerConfig {
    public NettyServerConfig(String name, int port){
        this.setName(name);
        this.setPort(port);
    }
}
