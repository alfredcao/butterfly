package org.butterfly.common.util;

import java.util.UUID;

/**
 * uuid工具类
 * @author alfredcao
 * @date 2019-10-30 20:09
 */
public class UUIDUtil {
    /**
     * 获取UUID
     * @return UUID
     */
    public static String uuid(){
        return UUID.randomUUID().toString();
    }
}
