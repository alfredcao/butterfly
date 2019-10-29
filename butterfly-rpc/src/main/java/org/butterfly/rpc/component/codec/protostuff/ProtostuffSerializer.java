package org.butterfly.rpc.component.codec.protostuff;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.butterfly.rpc.component.AbstractSerializer;

/**
 * protostuff序列化器
 * @author alfredcao
 * @date 2019-10-26 23:04
 */
public class ProtostuffSerializer extends AbstractSerializer {
    @Override
    protected byte[] doSerialize(Object obj) throws Exception {
        byte[] bytes = null;
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema schema = RuntimeSchema.createFrom(obj.getClass());
            bytes = ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } finally {
            buffer.clear();
        }
        return bytes;
    }
}
