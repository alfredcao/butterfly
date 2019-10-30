package org.butterfly.rpc.model.dto;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.MapUtils;
import org.butterfly.rpc.model.constant.Constant;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * rpc消息头
 * @author alfredcao
 * @date 2019-10-27 20:21
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RpcHeader implements Serializable {
    private int magic = Constant.MAGIC_RPC;
    private int length; // 消息总字节数 = 消息头字节数 + 消息体字节数
    private byte type; // 消息类型
    private String serviceId; // 服务ID
    private String sessionId; // 会话ID
    private String requestId; // 请求ID
    private Map<String, Object> extendAttributes; // 扩展属性

    @Override
    public String toString() {
        String extendAttributesDesc = "";
        if(MapUtils.isNotEmpty(this.extendAttributes)){
            extendAttributesDesc = JSON.toJSONString(this.extendAttributes);
        }

        return "Header [length : " + this.length +
                ", type : " + this.type +
                ", sessionId : " + this.sessionId +
                ", requestId : " + this.requestId +
                ", serviceId : " + this.serviceId +
                ", extendAttributes : " + extendAttributesDesc + "]";
    }

    public int getStatusCode(){
        Object statusCode = null;
        if(this.extendAttributes == null || (statusCode = this.extendAttributes.get("statusCode")) == null){
            return -1;
        }
        return (int)statusCode;
    }

    public void setStatusCode(int statusCode){
        if(this.extendAttributes == null){
            this.extendAttributes = new HashMap<>();
        }
        this.extendAttributes.put("statusCode", statusCode);
    }
}
