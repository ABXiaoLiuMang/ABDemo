package com.dale.net.utils;

import java.lang.reflect.Type;

public class StringUtils {

    /**
     * desc: 检查class是否是String类型
     */
    public static boolean isStringType(Class<?> tClass){
        return tClass != null && tClass.isAssignableFrom(String.class);
    }

    /**
     * desc: 检查class是否是String类型
     */
    public static boolean isStringType(Type type){
        try {
            return type != null && isStringType((Class<?>)type);
        }catch (Exception e){
            return false;
        }
    }


    public static String buffer(Object... strings){
        StringBuffer buffer = new StringBuffer("");
        for (Object o:
                strings) {
            buffer.append(o);
        }
        return buffer.toString();
    }
}
