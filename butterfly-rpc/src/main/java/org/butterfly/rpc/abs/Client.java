package org.butterfly.rpc.abs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.butterfly.common.core.enumeration.CodeEnum;
import org.butterfly.common.core.enumeration.NameEnum;
import org.butterfly.common.util.EnumUtil;

/**
 * 客户端
 * @author caozhen
 * @date 2019-10-11 16:14
 */
public interface Client {
    /**
     * 初始化客户端
     * @param config 客户端配置
     */
    void init(ClientConfig config);

    /**
     * 启动客户端
     */
    void start();

    /**
     * 停止客户端
     */
    void stop();

    /**
     * 获取客户端状态
     * @return 客户端状态
     */
    Status status();

    /**
     * 客户端状态
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
