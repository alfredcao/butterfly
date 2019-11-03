package org.butterfly.rpc.component;

import lombok.Getter;
import org.butterfly.common.util.CheckUtil;
import org.butterfly.rpc.abs.ClientConfig;
import org.butterfly.rpc.abs.policy.RetryPolicy;

/**
 * 客户端配置基类
 * @author caozhen
 * @date 2019-10-21 09:32
 */
public abstract class AbstractClientConfig implements ClientConfig {
    @Getter
    private String name;
    @Getter
    private String serverAddress;
    @Getter
    private int serverPort;
    @Getter
    private RetryPolicy retryPolicy;


    protected void setName(String name){
        CheckUtil.checkNotNull(name, "client config name");
        this.name = name;
    }
    protected void setServerAddress(String serverAddress){
        CheckUtil.checkNotNull(serverAddress, "client config serverAddress");
        this.serverAddress = serverAddress;
    }

    protected void setServerPort(int serverPort){
        CheckUtil.checkNotNull(serverPort, "client config serverPort");
        this.serverPort = serverPort;
    }

    protected void setRetryPolicy(RetryPolicy retryPolicy){
        CheckUtil.checkNotNull(retryPolicy, "client config retryPolicy");
        this.retryPolicy = retryPolicy;
    }
}
