package org.butterfly.common.util;

import org.butterfly.common.core.enumeration.CodeEnum;
import org.butterfly.common.core.enumeration.NameEnum;

/**
 * 枚举工具类
 */
public class EnumUtil {
    /**
     * 根据code获取枚举常量
     */
    public static <T extends CodeEnum> T getEnumConstantByCode(Class<T> enumClass, int code, T defaultEnumConstant) {
        for (T enumConstant : enumClass.getEnumConstants()) {
            if(enumConstant.getCode() == code){
                return enumConstant;
            }
        }
        return defaultEnumConstant;
    }

    public static <T extends CodeEnum> T getEnumConstantByCode(Class<T> enumClass, int code) {
        return getEnumConstantByCode(enumClass, code, null);
    }

    /**
     * 根据name获取枚举常量
     */
    public static <T extends NameEnum> T getEnumConstantByName(Class<T> enumClass, String name, T defaultEnumConstant) {
        for (T enumConstant : enumClass.getEnumConstants()) {
            if(enumConstant.getName().equals(name)){
                return enumConstant;
            }
        }
        return defaultEnumConstant;
    }

    public static <T extends NameEnum> T getEnumConstantByName(Class<T> enumClass, String name) {
        return getEnumConstantByName(enumClass, name, null);
    }
}
