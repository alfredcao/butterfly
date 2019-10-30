package org.butterfly.rpc.model.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.butterfly.common.core.enumeration.CodeEnum;
import org.butterfly.common.core.enumeration.NameEnum;

/**
 * rpc消息状态
 * @author alfredcao
 * @date 2019-10-30 18:05
 */
@AllArgsConstructor
public enum RpcMsgStatusCode implements CodeEnum, NameEnum {
    UNKNOWN(-1, "未知"),
    OK(200, "请求成功"),
    BAD_REQUEST(400, "错误请求"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误");

    @Getter
    private int code;
    @Getter
    private String name;
}
