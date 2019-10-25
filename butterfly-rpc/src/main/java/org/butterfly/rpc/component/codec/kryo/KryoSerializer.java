package org.butterfly.rpc.component.codec.kryo;

import org.butterfly.rpc.abs.codec.Serializer;

/**
 * kryo序列化器
 * @author caozhen
 * @date 2019-10-25 09:22
 */
public class KryoSerializer implements Serializer {
    @Override
    public byte[] serialize(Object obj) throws Exception {
        return new byte[0];
    }
}
