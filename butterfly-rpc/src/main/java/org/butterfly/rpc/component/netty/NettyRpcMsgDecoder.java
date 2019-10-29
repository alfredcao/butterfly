package org.butterfly.rpc.component.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.butterfly.common.util.CheckUtil;
import org.butterfly.rpc.abs.codec.Deserializer;
import org.butterfly.rpc.model.constant.Constant;
import org.butterfly.rpc.model.dto.RpcHeader;
import org.butterfly.rpc.model.dto.RpcMsg;

import java.nio.ByteOrder;
import java.util.Map;

public class NettyRpcMsgDecoder extends LengthFieldBasedFrameDecoder {
    private Deserializer deserializer;
    public NettyRpcMsgDecoder(int maxFrameLength) {
        this(maxFrameLength, null);
    }

    public NettyRpcMsgDecoder(int maxFrameLength, Deserializer deserializer) {
        this(maxFrameLength, true, deserializer);
    }

    public NettyRpcMsgDecoder(int maxFrameLength, boolean failFast) {
        this(maxFrameLength, failFast, null);
    }

    public NettyRpcMsgDecoder(int maxFrameLength, boolean failFast, Deserializer deserializer) {
        this(ByteOrder.BIG_ENDIAN, maxFrameLength, failFast, deserializer);
    }

    public NettyRpcMsgDecoder(ByteOrder byteOrder, int maxFrameLength, boolean failFast) {
        this(byteOrder, maxFrameLength, failFast, null);
    }

    public NettyRpcMsgDecoder(ByteOrder byteOrder, int maxFrameLength, boolean failFast, Deserializer deserializer) {
        super(byteOrder, maxFrameLength, 4, 4, -8, 0, failFast);
        if(deserializer == null){
            // TODO 提供默认反序列化器
        }
        CheckUtil.checkNotNull(deserializer, "反序列化器");
        this.deserializer = deserializer;
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if(frame == null){
            return null;
        }
        int magic = frame.readInt();
        if(magic != Constant.MAGIC_RPC){
            throw new IllegalArgumentException("非法请求！");
        }
        RpcMsg rpcMsg = new RpcMsg();
        // 解码请求头
        RpcHeader rpcHeader = new RpcHeader();
        rpcHeader.setMagic(magic);
        rpcHeader.setLength(frame.readInt());
        rpcHeader.setType(frame.readByte());
        byte serviceIdLength = frame.readByte(); // serviceId字节长度
        if(serviceIdLength > 0){
            byte[] serviceIdBytes = new byte[serviceIdLength];
            frame.readBytes(serviceIdBytes);
            rpcHeader.setServiceId(this.deserializer.deserialize(serviceIdBytes, String.class));
        }
        byte sessionIdLength = frame.readByte(); // sessionId字节数组长度
        if(sessionIdLength > 0){
            byte[] sessionIdBytes = new byte[sessionIdLength];
            frame.readBytes(sessionIdBytes);
            rpcHeader.setSessionId(this.deserializer.deserialize(sessionIdBytes, String.class));
        }
        byte requestIdLength = frame.readByte(); // requestId字节数组长度
        if(requestIdLength > 0){
            byte[] requestIdBytes = new byte[requestIdLength];
            frame.readBytes(requestIdBytes);
            rpcHeader.setRequestId(this.deserializer.deserialize(requestIdBytes, String.class));
        }
        int extendAttributesLength = frame.readInt(); // extendAttributes字节数组长度
        if(extendAttributesLength > 0){
            byte[] extendAttributesBytes = new byte[extendAttributesLength];
            frame.readBytes(extendAttributesBytes);
            rpcHeader.setExtendAttributes(this.deserializer.deserialize(extendAttributesBytes, Map.class));
        }
        rpcMsg.setHeader(rpcHeader);

        // 解码请求体
        int bodyLength = frame.readInt(); // request字节数组长度
        if(bodyLength > 0){
            byte[] bodyBytes = new byte[bodyLength];
            frame.readBytes(bodyBytes);
            rpcMsg.setBody(this.deserializer.deserialize(bodyBytes, Object.class));
        }

        return rpcMsg;
    }
}
