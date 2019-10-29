package org.butterfly.rpc.abs;

/**
 * 端点
 * @author alfredcao
 * @date 2019-10-29 17:01
 */
public interface EndPoint {
    /**
     * 最大可接收的字节数
     * @return
     */
    default int maxRecBytes(){
        return Integer.MAX_VALUE;
    }
}
