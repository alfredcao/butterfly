package org.butterfly.common.util;

/**
 * 校验工具类
 * @author caozhen
 * @date 2019-10-15 19:52
 */
public class CheckUtil {
    /**
     * 校验对象不可为空
     * @param obj 目标对象
     * @param msg 异常信息
     */
    public static void checkNotNull(Object obj, String msg){
        if(obj == null){
            throw new NullPointerException(msg);
        }
    }
}
