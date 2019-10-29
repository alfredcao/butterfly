package org.butterfly.rpc.component.codec.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import org.butterfly.rpc.component.AbstractDeserializer;

import java.io.ByteArrayInputStream;

/**
 * kryo反序列化器
 * @author caozhen
 * @date 2019-10-25 09:22
 */
public class KryoDeserializer extends AbstractDeserializer {
    @Override
    protected <T> T doDeserialize(byte[] bytes, Class<T> clazz) throws Exception {
        Kryo kryo = new Kryo(); // 可优化成ThreadLocal形式
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        Input input = new Input(bais);
        input.close();
        return (T) kryo.readClassAndObject(input);
    }
}
