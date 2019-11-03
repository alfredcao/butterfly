package org.butterfly.rpc.abs.policy;

/**
 * 重试策略
 * @author alfredcao
 * @date 2019-11-02 11:47
 */
public interface RetryPolicy {
    /**
     * 是否可重试
     * @return 是否可重试
     */
    boolean canRetry();

    /**
     * 获取重试次数
     * @return 重试次数
     */
    int getRetryCount();

    /**
     * 重置
     */
    void reset();
}
