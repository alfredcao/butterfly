package org.butterfly.rpc.component.policy;

import org.butterfly.common.util.CheckUtil;

/**
 * 固定频率重试策略
 * @author alfredcao
 * @date 2019-11-02 20:46
 */
public class FixFrequencyRetryPolicy extends SleepRetryPolicy {
    private static final long DEFAULT_FIX_SLEEP_TIME_MS = 5 * 1000; // 默认频率为5秒
    private long fixSleepTimeMs;

    public FixFrequencyRetryPolicy(){
        this(DEFAULT_FIX_SLEEP_TIME_MS);
    }

    public FixFrequencyRetryPolicy(long fixSleepTimeMs){
        super();
        this.fixSleepTimeMs = fixSleepTimeMs;
    }

    public FixFrequencyRetryPolicy(long fixSleepTimeMs, int maxRetryCount){
        super(maxRetryCount);
        this.fixSleepTimeMs = fixSleepTimeMs;
    }

    private void checkFixSleepTimeMs(long fixSleepTimeMs){
        CheckUtil.checkPositive(fixSleepTimeMs, "fixSleepTimeMs必须为正数！");
    }

    @Override
    protected long sleepTimeMs() {
        return this.fixSleepTimeMs;
    }
}
