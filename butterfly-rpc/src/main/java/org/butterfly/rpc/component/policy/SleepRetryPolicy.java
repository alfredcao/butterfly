package org.butterfly.rpc.component.policy;

import java.util.concurrent.TimeUnit;

/**
 * 休眠重试策略
 * @author alfredcao
 * @date 2019-11-02 20:15
 */
public abstract class SleepRetryPolicy extends AbstractRetryPolicy {
    public SleepRetryPolicy() {
        super();
    }

    public SleepRetryPolicy(int maxRetryCount) {
        super(maxRetryCount);
    }

    @Override
    protected boolean proc() {
        try {
            TimeUnit.MILLISECONDS.sleep(this.sleepTimeMs());
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
            return false;
        }
        return true;
    }

    /**
     * 休眠时间（毫秒）
     * @return
     */
    protected abstract long sleepTimeMs();
}
