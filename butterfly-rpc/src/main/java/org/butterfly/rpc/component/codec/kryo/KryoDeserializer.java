package org.butterfly.rpc.component.codec.kryo;

import org.butterfly.rpc.abs.codec.Deserializer;

/**
 * kryo反序列化器
 * @author caozhen
 * @date 2019-10-25 09:22
 */
public class KryoDeserializer implements Deserializer {
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws Exception {
        return null;
    }
}
