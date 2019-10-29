package org.butterfly.rpc.component;

import org.butterfly.rpc.abs.codec.Serializer;

/**
 * 序列化器实现基类
 * @author alfredcao
 * @date 2019-10-29 16:03
 */
public abstract class AbstractSerializer implements Serializer {
    @Override
    public final byte[] serialize(Object obj) throws Exception {
        if(obj == null){
            return new byte[0];
        }
        return this.doSerialize(obj);
    }

    /**
     * 执行序列化
     * @param obj 序列化对象
     * @return 序列化后的字节数组
     */
    abstract protected byte[] doSerialize(Object obj) throws Exception;
}
