package org.butterfly.rpc.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * rpc消息
 * @author alfredcao
 * @date 2019-10-27 20:19
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RpcMsg implements Serializable {
    private RpcHeader header;
    private Object body;

    @Override
    public String toString() {
        return this.header.toString();
    }
}
