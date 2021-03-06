package org.butterfly.rpc.component.mina.encoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.butterfly.common.util.CheckUtil;
import org.butterfly.rpc.abs.codec.Serializer;


/**
 * Created by HUAWEI on 2019/10/26.
 */
public class SimpleProtocolEncoder extends ProtocolEncoderAdapter {
    private Serializer serializer;

    public SimpleProtocolEncoder(Serializer serializer){
        if(serializer == null){
            // TODO 提供默认序列化器
        }
        CheckUtil.checkNotNull(serializer, "序列化器");
        this.serializer = serializer;
    }
    @Override
    public void encode(IoSession session, Object message,
                       ProtocolEncoderOutput out) throws Exception {
        IoBuffer buffer = IoBuffer.allocate(100).setAutoExpand(true);
       // byte[] bytes =( byte[])message;
        byte[] bytes = this.serializer.serialize(message);
        //byte[] sizeBytes = BytesUtil.int2Bytes(bytes.length);
        // 将前 4 位设置成数据体的字节长度
        //buffer.put(sizeBytes);
        buffer.putInt(bytes.length);
        buffer.put(bytes);
        buffer.flip();
        out.write(buffer);
    }

}
