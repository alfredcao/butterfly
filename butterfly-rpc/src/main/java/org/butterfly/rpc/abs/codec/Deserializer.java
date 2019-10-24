package org.butterfly.rpc.abs.codec;

/**
 * 反序列化器
 * @author alfredcao
 * @date 2019-10-24 23:16
 */
public interface Deserializer {
    /**
     * 反序列化
     * @param bytes 源字节数组
     * @param clazz 对象类型
     * @return 对象
     * @throws Exception
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz) throws Exception;
}
