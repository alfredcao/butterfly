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

    /**
     * 校验数字为正数
     */
    public static void checkPositive(long number, String msg){
        if(number <= 0){
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 校验数字为正数
     */
    public static void checkPositive(int number, String msg){
        if(number <= 0){
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 校验数字为正数
     */
    public static void checkPositive(float number, String msg){
        if(number <= 0){
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 校验数字为正数
     */
    public static void checkPositive(double number, String msg){
        if(number <= 0){
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 校验数字为正数
     */
    public static void checkPositive(short number, String msg){
        if(number <= 0){
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 校验数字为正数
     */
    public static void checkPositive(byte number, String msg){
        if(number <= 0){
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 校验数字1大于数字2
     */
    public static void checkBig(long number1, long number2, String msg){
        if(number1 <= number2){
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 校验数字1大于数字2
     */
    public static void checkBig(int number1, int number2, String msg){
        if(number1 <= number2){
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 校验数字1大于数字2
     */
    public static void checkBig(float number1, float number2, String msg){
        if(number1 <= number2){
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 校验数字1大于数字2
     */
    public static void checkBig(double number1, double number2, String msg){
        if(number1 <= number2){
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 校验数字1大于数字2
     */
    public static void checkBig(short number1, short number2, String msg){
        if(number1 <= number2){
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 校验数字1大于数字2
     */
    public static void checkBig(byte number1, byte number2, String msg){
        if(number1 <= number2){
            throw new IllegalArgumentException(msg);
        }
    }
}
