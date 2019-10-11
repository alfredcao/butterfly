package org.butterfly.rpc.abs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
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
     * @return 初始化是否成功
     */
    boolean init(ServerConfig config);

    /**
     * 启动服务器
     * @return 启动是否成功
     */
    boolean start();

    /**
     * 停止服务器
     * @return 停止是否成功
     */
    boolean stop();

    /**
     * 获取服务器状态
     * @return 服务器状态
     */
    StatusInfo status();

    @Getter@Setter
    @AllArgsConstructor
    class StatusInfo {
        private volatile Status status;
        private volatile Throwable cause;
    }

    /**
     * 服务器状态
     * @author caozhen
     * @date 2019-10-11 17:40
     */
    @Getter
    @AllArgsConstructor
    enum Status implements CodeEnum, NameEnum {
        NEW(1, "新建"),
        INIT(10, "初始化"),
        INIT_EXCEPTION(11, "初始化异常"),
        START(20, "启动"),
        START_EXCEPTION(21, "启动异常"),
        STOP(30, "停止"),
        STOP_EXCEPTION(31, "停止异常");

        private int code;
        private String name;

        public Status resolve(int code){
            return EnumUtil.getEnumConstantByCode(Status.class, code);
        }
    }
}
