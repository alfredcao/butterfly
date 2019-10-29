package org.butterfly.rpc.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.butterfly.common.util.CheckUtil;
import org.butterfly.rpc.model.constant.Constant;
import org.butterfly.rpc.model.enumeration.RpcMsgType;

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

    /**
     * 获取握手请求RPC消息
     * @return
     */
    public static RpcMsg handshakeReqRpcMsg(String sessionId, String requestId){
        CheckUtil.checkNotNull(sessionId, "会话ID");
        CheckUtil.checkNotNull(requestId, "请求ID");
        RpcHeader rpcHeader = RpcHeader.builder()
                .magic(Constant.MAGIC_RPC)
                .type(RpcMsgType.HANDSHAKE_REQ.getCode())
                .sessionId(sessionId)
                .requestId(requestId)
                .build();
        return RpcMsg.builder()
                .header(rpcHeader)
                .build();
    }

    /**
     * 获取握手响应RPC消息
     * @return
     */
    public static RpcMsg handshakeRespRpcMsg(String sessionId, String requestId){
        CheckUtil.checkNotNull(sessionId, "会话ID");
        CheckUtil.checkNotNull(requestId, "请求ID");
        RpcHeader rpcHeader = RpcHeader.builder()
                .magic(Constant.MAGIC_RPC)
                .type(RpcMsgType.HANDSHAKE_RESP.getCode())
                .sessionId(sessionId)
                .requestId(requestId)
                .build();
        return RpcMsg.builder()
                .header(rpcHeader)
                .build();
    }

    /**
     * 获取心跳请求RPC消息
     * @return
     */
    public static RpcMsg heartBeatReqRpcMsg(String sessionId, String requestId){
        CheckUtil.checkNotNull(sessionId, "会话ID");
        CheckUtil.checkNotNull(requestId, "请求ID");
        RpcHeader rpcHeader = RpcHeader.builder()
                .magic(Constant.MAGIC_RPC)
                .type(RpcMsgType.HEARTBEAT_REQ.getCode())
                .sessionId(sessionId)
                .requestId(requestId)
                .build();
        return RpcMsg.builder()
                .header(rpcHeader)
                .build();
    }

    /**
     * 获取心跳响应RPC消息
     * @return
     */
    public static RpcMsg heartBeatRespRpcMsg(String sessionId, String requestId){
        CheckUtil.checkNotNull(sessionId, "会话ID");
        CheckUtil.checkNotNull(requestId, "请求ID");
        RpcHeader rpcHeader = RpcHeader.builder()
                .magic(Constant.MAGIC_RPC)
                .type(RpcMsgType.HEARTBEAT_RESP.getCode())
                .sessionId(sessionId)
                .requestId(requestId)
                .build();
        return RpcMsg.builder()
                .header(rpcHeader)
                .build();
    }
}
