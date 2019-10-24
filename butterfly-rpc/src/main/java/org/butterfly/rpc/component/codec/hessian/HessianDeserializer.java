package org.butterfly.rpc.component.codec.hessian;

import com.caucho.hessian.io.Hessian2Input;
import org.butterfly.rpc.abs.codec.Deserializer;
import org.butterfly.rpc.component.netty.NettyServerConfig;

import java.io.ByteArrayInputStream;

/**
 * Hessian反序列化器
 * @author alfredcao
 * @date 2019-10-24 23:23
 */
public class HessianDeserializer implements Deserializer {
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws Exception {
        Hessian2Input input = new Hessian2Input(new ByteArrayInputStream(bytes));
        return (T) input.readObject(clazz);
    }
}
