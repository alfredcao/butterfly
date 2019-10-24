package org.butterfly.rpc.component.codec.hessian;

import com.caucho.hessian.io.Hessian2Output;
import org.butterfly.rpc.abs.codec.Serializer;

import java.io.ByteArrayOutputStream;

/**
 * hessian序列化器
 * @author alfredcao
 * @date 2019-10-24 23:21
 */
public class HessianSerializer implements Serializer {
    @Override
    public byte[] serialize(Object obj) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Hessian2Output output = new Hessian2Output(baos);
        output.writeObject(obj);
        output.flush();
        return baos.toByteArray();
    }
}
