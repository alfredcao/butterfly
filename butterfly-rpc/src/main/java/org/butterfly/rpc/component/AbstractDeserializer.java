package org.butterfly.rpc.component;

import org.butterfly.rpc.abs.codec.Deserializer;

/**
 * 反序列化器实现基类
 * @author alfredcao
 * @date 2019-10-29 16:05
 */
public abstract class AbstractDeserializer implements Deserializer {
    @Override
    public final <T> T deserialize(byte[] bytes, Class<T> clazz) throws Exception {
        if(bytes == null){
            return null;
        }
        return this.doDeserialize(bytes, clazz);
    }

    protected abstract <T> T doDeserialize(byte[] bytes, Class<T> clazz) throws Exception;
}
