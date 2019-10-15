package org.butterfly.rpc.component;

import lombok.Getter;
import org.butterfly.common.util.CheckUtil;
import org.butterfly.rpc.abs.ServerConfig;

/**
 * 服务器配置基类
 * @author caozhen
 * @date 2019-10-15 19:46
 */
public abstract class AbstractServerConfig implements ServerConfig {
    @Getter
    private String name;
    @Getter
    private int port;

    protected void setName(String name){
        CheckUtil.checkNotNull(name, "server config name");
        this.name = name;
    }
    protected void setPort(int port){
        CheckUtil.checkNotNull(port, "server config port");
        this.port = port;
    }
}
