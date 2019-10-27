package org.butterfly.rpc.component.codec.protostuff;

import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.butterfly.rpc.abs.codec.Deserializer;
import org.butterfly.rpc.component.netty.NettyClientConfig;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisSerializer;

/**
 * protostuff反序列化器
 * @author alfredcao
 * @date 2019-10-26 23:13
 */
public class ProtostuffDeserializer implements Deserializer {
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws Exception {
        Objenesis objenesis = new ObjenesisSerializer();
        T obj = objenesis.newInstance(clazz);
        Schema schema = RuntimeSchema.createFrom(clazz);
        ProtostuffIOUtil.mergeFrom(bytes, obj, schema);
        return obj;
    }

    public static void main(String[] args) throws Exception {
        ProtostuffSerializer serializer = new ProtostuffSerializer();
        NettyClientConfig config = new NettyClientConfig("测试客户端", "localhost", 20000);
        byte[] bytes = serializer.serialize(config);
        System.out.println("字节数组长度：" + bytes.length);
        ProtostuffDeserializer deserializer = new ProtostuffDeserializer();
        NettyClientConfig clone = deserializer.deserialize(bytes, NettyClientConfig.class);
        System.out.println(clone.getName() + " " + clone.getServerAddress() + " " + clone.getServerPort());

    }
}
