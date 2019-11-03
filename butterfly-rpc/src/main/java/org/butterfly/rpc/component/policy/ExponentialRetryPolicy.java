package org.butterfly.rpc.component.policy;

import lombok.extern.slf4j.Slf4j;

/**
 * 指数级休眠重试策略
 * @author alfredcao
 * @date 2019-11-02 20:24
 */
@Slf4j
public class ExponentialRetryPolicy extends SleepRetryPolicy {
    private static final long DEFAULT_BASE_SLEEP_TIME_MS = 2 * 1000; // 默认基础休眠时间为2秒
    private static final long DEFAULT_MAX_SLEEP_TIME_MS = 30 * 1000; // 默认最大休眠时间为30秒
    private static final double DEFAULT_MULTIPLIER = 1.5; // 默认休眠时间按50%增长

    private final long baseSleepTimeMs;
    private long currentSleepTimeMs;
    private final long maxSleepTimeMs;
    private final double multiplier;

    public ExponentialRetryPolicy(){
        this(DEFAULT_BASE_SLEEP_TIME_MS);
    }

    public ExponentialRetryPolicy(long baseSleepTimeMs){
        super();
        this.currentSleepTimeMs = this.baseSleepTimeMs = baseSleepTimeMs;
        this.maxSleepTimeMs = DEFAULT_MAX_SLEEP_TIME_MS;
        this.multiplier = DEFAULT_MULTIPLIER;
    }

    public ExponentialRetryPolicy(long baseSleepTimeMs, int maxRetryCount){
        this(baseSleepTimeMs, maxRetryCount, DEFAULT_MAX_SLEEP_TIME_MS);
    }

    public ExponentialRetryPolicy(long baseSleepTimeMs, int maxRetryCount, long maxSleepTimeMs){
        this(baseSleepTimeMs, maxRetryCount, maxSleepTimeMs, DEFAULT_MULTIPLIER);
    }

    public ExponentialRetryPolicy(long baseSleepTimeMs, int maxRetryCount, long maxSleepTimeMs, double multiplier){
        super(maxRetryCount);
        this.currentSleepTimeMs = this.baseSleepTimeMs = baseSleepTimeMs;
        this.maxSleepTimeMs = maxSleepTimeMs;
        this.multiplier = multiplier;
    }

    @Override
    protected long sleepTimeMs() {
        if(this.currentSleepTimeMs >= this.maxSleepTimeMs){
            return this.maxSleepTimeMs;
        } else if(this.currentSleepTimeMs <= 0){
            this.currentSleepTimeMs = this.baseSleepTimeMs;
        } else {
            this.currentSleepTimeMs *= (retryCount.get() == 1) ? 1 : this.multiplier;
        }
        this.currentSleepTimeMs = this.currentSleepTimeMs < this.maxSleepTimeMs ? this.currentSleepTimeMs : this.maxSleepTimeMs;
        return this.currentSleepTimeMs;
    }

    @Override
    public void reset() {
        super.reset();
        this.currentSleepTimeMs = this.baseSleepTimeMs;
    }
}
