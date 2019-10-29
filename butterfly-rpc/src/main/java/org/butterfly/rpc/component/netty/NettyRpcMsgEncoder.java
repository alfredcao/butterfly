package org.butterfly.rpc.component.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.butterfly.common.util.CheckUtil;
import org.butterfly.rpc.abs.codec.Serializer;
import org.butterfly.rpc.model.dto.RpcMsg;

import java.util.List;

/**
 * netty rpc消息编码器
 * @author alfredcao
 * @date 2019-10-29 15:31
 */
public class NettyRpcMsgEncoder extends MessageToMessageEncoder<RpcMsg> {
    private Serializer serializer;

    public NettyRpcMsgEncoder(){
        this(null);
    }

    public NettyRpcMsgEncoder(Serializer serializer){
        if(serializer == null){
            // TODO 提供默认序列化器
        }
        CheckUtil.checkNotNull(serializer, "序列化器");
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcMsg rpcMsg, List<Object> out) throws Exception {
        CheckUtil.checkNotNull(rpcMsg, "rpc消息");
        CheckUtil.checkNotNull(rpcMsg, "rpc消息头");

        ByteBuf byteBuf = Unpooled.buffer();
        // 请求头编码
        byteBuf.writeInt(rpcMsg.getHeader().getMagic());
        byteBuf.writeInt(rpcMsg.getHeader().getLength()); // 此处为占位
        byteBuf.writeByte(rpcMsg.getHeader().getType());
        byte[] serviceIdBytes = this.serializer.serialize(rpcMsg.getHeader().getServiceId());
        byteBuf.writeByte(serviceIdBytes.length);
        byteBuf.writeBytes(serviceIdBytes);
        byte[] sessionIdBytes = this.serializer.serialize(rpcMsg.getHeader().getSessionId());
        byteBuf.writeByte(sessionIdBytes.length);
        byteBuf.writeBytes(sessionIdBytes);
        byte[] requestIdBytes = this.serializer.serialize(rpcMsg.getHeader().getRequestId());
        byteBuf.writeByte(requestIdBytes.length);
        byteBuf.writeBytes(requestIdBytes);
        byte[] extendAttributesBytes = this.serializer.serialize(rpcMsg.getHeader().getExtendAttributes());
        byteBuf.writeInt(extendAttributesBytes.length);
        byteBuf.writeBytes(extendAttributesBytes);
        // 请求体编码
        byte[] bodyBytes = this.serializer.serialize(rpcMsg.getBody());
        byteBuf.writeInt(bodyBytes.length);
        byteBuf.writeBytes(bodyBytes);
        // 设置最终Length字段
        byteBuf.setInt(4, byteBuf.readableBytes());

        out.add(byteBuf);
    }
}
