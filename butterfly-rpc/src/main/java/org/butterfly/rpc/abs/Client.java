package org.butterfly.rpc.abs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.butterfly.common.core.enumeration.CodeEnum;
import org.butterfly.common.core.enumeration.NameEnum;
import org.butterfly.common.util.EnumUtil;
import org.butterfly.rpc.model.dto.RpcMsg;

/**
 * 客户端
 * @author caozhen
 * @date 2019-10-11 16:14
 */
public interface Client extends EndPoint {
    /**
     * 初始化客户端
     * @param config 客户端配置
     * @return 初始化是否成功
     */
    boolean init(ClientConfig config);

    /**
     * 连接
     * @return 连接是否成功
     */
    boolean connect();

    /**
     * 断开连接
     * @return 断开连接是否成功
     */
    boolean disconnect();

    /**
     * 获取客户端状态信息
     * @return 客户端状态信息
     */
    StatusInfo status();

    /**
     * 发送消息
     * @param msg 消息字节数组
     * @throws Exception
     */
    void send(byte[] msg) throws Exception;

    /**
     * 发送RPC消息
     * @param rpcMsg rpc消息
     * @throws Exception
     */
    void send(RpcMsg rpcMsg) throws Exception;


    @Getter@Setter
    @AllArgsConstructor
    class StatusInfo {
        private volatile Status status;
        private volatile Throwable cause;
    }

    /**
     * 客户端状态
     * @author caozhen
     * @date 2019-10-11 17:40
     */
    @Getter
    @AllArgsConstructor
    enum Status implements CodeEnum, NameEnum {
        NEW(1, "新建"),
        INIT(10, "初始化"),
        INIT_EXCEPTION(11, "初始化异常"),
        CONNECT(20, "连接"),
        CONNECT_EXCEPTION(21, "连接异常"),
        DISCONNECT(30, "断开连接"),
        DISCONNECT_EXCEPTION(31, "断开连接异常");

        private int code;
        private String name;

        public Status resolve(int code){
            return EnumUtil.getEnumConstantByCode(Status.class, code);
        }
    }
}
