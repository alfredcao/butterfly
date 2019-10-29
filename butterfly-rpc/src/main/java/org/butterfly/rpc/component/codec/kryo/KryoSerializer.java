package org.butterfly.rpc.component.codec.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import org.butterfly.rpc.component.AbstractSerializer;

import java.io.ByteArrayOutputStream;

/**
 * kryo序列化器
 * @author caozhen
 * @date 2019-10-25 09:22
 */
public class KryoSerializer extends AbstractSerializer {
    @Override
    protected byte[] doSerialize(Object obj) throws Exception {
        Kryo kryo = new Kryo(); // 可优化成ThreadLocal形式
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        kryo.writeClassAndObject(output, obj);
        output.close();
        return baos.toByteArray();
    }
}
