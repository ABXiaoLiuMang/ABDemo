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

    /**
     * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input) || "null".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

}
