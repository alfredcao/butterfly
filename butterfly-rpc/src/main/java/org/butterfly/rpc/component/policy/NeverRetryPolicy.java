package org.butterfly.rpc.component.policy;

/**
 * 不重试策略
 * @author alfredcao
 * @date 2019-11-02 20:08
 */
public class NeverRetryPolicy extends AbstractRetryPolicy {
    public NeverRetryPolicy(){
        super(0);
    }
}
