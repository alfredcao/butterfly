package org.butterfly.rpc.component.codec.hessian;

import com.caucho.hessian.io.Hessian2Input;
import org.butterfly.rpc.component.AbstractDeserializer;

import java.io.ByteArrayInputStream;

/**
 * Hessian反序列化器
 * @author alfredcao
 * @date 2019-10-24 23:23
 */
public class HessianDeserializer extends AbstractDeserializer {
    @Override
    protected <T> T doDeserialize(byte[] bytes, Class<T> clazz) throws Exception {
        Hessian2Input input = new Hessian2Input(new ByteArrayInputStream(bytes));
        return (T) input.readObject(clazz);
    }
}
