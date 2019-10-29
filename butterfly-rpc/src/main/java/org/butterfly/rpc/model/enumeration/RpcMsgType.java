package org.butterfly.rpc.model.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.butterfly.common.core.enumeration.CodeByteEnum;
import org.butterfly.common.core.enumeration.NameEnum;
import org.butterfly.common.util.EnumUtil;

/**
 * rpc消息类型
 * @author alfredcao
 * @date 2019-10-29 20:42
 */
@AllArgsConstructor
public enum RpcMsgType implements CodeByteEnum, NameEnum {
    UNKNOWN((byte)-1, "未知"),
    HANDSHAKE_REQ((byte)1, "握手请求"),
    HANDSHAKE_RESP((byte)2, "握手响应"),
    HEARTBEAT_REQ((byte)10, "心跳请求"),
    HEARTBEAT_RESP((byte)11, "心跳响应");

    @Getter
    private byte code;
    @Getter
    private String name;

    public RpcMsgType resolve(byte code){
        return this.resolve(code, UNKNOWN);
    }

    public RpcMsgType resolve(byte code, RpcMsgType def){
        return EnumUtil.getEnumConstantByCodeByte(RpcMsgType.class, code, def);
    }
}
