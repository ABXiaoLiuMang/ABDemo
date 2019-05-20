package com.dale.net.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

/**
 * create by Dale
 * create on 2019/5/20
 * description:
 */
public class NetLog {

    public static String customTagPrefix = "NetLog"; // 自定义Tag的前缀，可以是作者名

    // 容许打印日志的类型，默认是true，设置为false则不打印
    public static boolean allowD = false;

    @SuppressLint("DefaultLocale")
    private static String generateTag(StackTraceElement caller, String mtag) {
        String tag = "%s.%s(Line:%d)"; // 占位符
        String callerClazzName = caller.getClassName(); // 获取到类名
        callerClazzName = callerClazzName.substring(callerClazzName
                .lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(),
                caller.getLineNumber()); // 替换
        tag = TextUtils.isEmpty(mtag) ? customTagPrefix + ":"
                + tag :mtag + ":"+ tag ;
        return tag;
    }


    public static void i(String content) {
        if (!allowD)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller,null);
        Log.i(tag, content);
    }

    public static void i(String mtag, String content) {
        if (!allowD)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller,mtag);
        Log.i(tag, content);
    }


    public static void e(String content) {
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller,null);
        Log.e(tag, content);
    }

    private static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }
}
