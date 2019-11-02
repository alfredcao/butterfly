package org.butterfly.rpc.component.policy;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * 指数级休眠重试策略
 * @author alfredcao
 * @date 2019-11-02 20:24
 */
@Slf4j
public class ExponentialRetryPolicy extends SleepRetryPolicy {
    private static final long DEFAULT_MAX_SLEEP_TIME_MS = 60 * 1000; // 默认最大休眠时间为60秒

    private final Random random = new Random();
    private final long baseSleepTimeMs;
    private final long maxSleepTimeMs;

    public ExponentialRetryPolicy(long baseSleepTimeMs){
        super();
        this.baseSleepTimeMs = baseSleepTimeMs;
        this.maxSleepTimeMs = DEFAULT_MAX_SLEEP_TIME_MS;
    }

    public ExponentialRetryPolicy(long baseSleepTimeMs, int maxRetryCount){
        this(baseSleepTimeMs, maxRetryCount, DEFAULT_MAX_SLEEP_TIME_MS);
    }

    public ExponentialRetryPolicy(long baseSleepTimeMs, int maxRetryCount, long maxSleepTimeMs){
        super(maxRetryCount);
        this.baseSleepTimeMs = baseSleepTimeMs;
        this.maxSleepTimeMs = maxSleepTimeMs;
    }

    @Override
    protected long sleepTimeMs() {
        long sleepTimeMs = this.baseSleepTimeMs * Math.max(1, this.random.nextInt(1 << (this.retryCount.get() + 1)));
        if (sleepTimeMs > this.maxSleepTimeMs) {
            sleepTimeMs = this.maxSleepTimeMs;
        }
        return sleepTimeMs;
    }
}
