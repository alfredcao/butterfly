package org.butterfly.rpc.component.policy;

import org.butterfly.rpc.abs.policy.RetryPolicy;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 重试策略基类
 * @author alfredcao
 * @date 2019-11-02 20:02
 */
public abstract class AbstractRetryPolicy implements RetryPolicy {
    private int maxRetryCount;
    protected AtomicInteger retryCount = new AtomicInteger(0);

    public AbstractRetryPolicy(){
        this(Integer.MAX_VALUE);
    }

    public AbstractRetryPolicy(int maxRetryCount){
        this.maxRetryCount = maxRetryCount;
    }

    @Override
    public final boolean canRetry() {
        if(this.retryCount.incrementAndGet() <= this.maxRetryCount){
            return this.proc();
        }
        return false;
    }

    /**
     * 处理
     */
    protected boolean proc(){
        return true;
    }

    @Override
    public void reset() {
        this.retryCount.set(0);
    }
}
