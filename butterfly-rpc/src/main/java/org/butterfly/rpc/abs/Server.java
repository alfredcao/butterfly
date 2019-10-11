package org.butterfly.rpc.abs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.butterfly.common.core.enumeration.CodeEnum;
import org.butterfly.common.core.enumeration.NameEnum;
import org.butterfly.common.util.EnumUtil;

/**
 * 服务器
 * @author caozhen
 * @date 2019-10-11 16:14
 */
public interface Server {
    /**
     * 初始化服务器
     * @param config 服务器配置
     */
    void init(ServerConfig config);

    /**
     * 启动服务器
     */
    void start();

    /**
     * 停止服务器
     */
    void stop();

    /**
     * 获取服务器状态
     * @return 服务器状态
     */
    Status status();

    /**
     * 服务器状态
     * @author caozhen
     * @date 2019-10-11 17:40
     */
    @Getter
    @AllArgsConstructor
    enum Status implements CodeEnum, NameEnum {
        NEW(1, "新建"),
        INIT(2, "初始化"),
        START(3, "启动"),
        STOP(4, "停止");

        private int code;
        private String name;

        public Status resolve(int code){
            return EnumUtil.getEnumConstantByCode(Status.class, code);
        }
    }
}
