package org.butterfly.rpc.abs.codec;

/**
 * 序列化器
 * @author alfredcao
 * @date 2019-10-24 23:13
 */
public interface Serializer {
    /**
     * 序列化
     * @param obj 序列化对象
     * @return 序列化后的字节数组
     */
    byte[] serialize(Object obj) throws Exception;
}
