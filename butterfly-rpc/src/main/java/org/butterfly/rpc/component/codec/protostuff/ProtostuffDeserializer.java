package org.butterfly.rpc.component.codec.protostuff;

import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.butterfly.rpc.component.AbstractDeserializer;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisSerializer;

/**
 * protostuff反序列化器
 * @author alfredcao
 * @date 2019-10-26 23:13
 */
public class ProtostuffDeserializer extends AbstractDeserializer {
    @Override
    protected <T> T doDeserialize(byte[] bytes, Class<T> clazz) throws Exception {
        Objenesis objenesis = new ObjenesisSerializer();
        T obj = objenesis.newInstance(clazz);
        Schema schema = RuntimeSchema.createFrom(clazz);
        ProtostuffIOUtil.mergeFrom(bytes, obj, schema);
        return obj;
    }
}
