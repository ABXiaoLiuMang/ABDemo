package com.dale.net.utils;

import android.os.Looper;


public class ContextUtil {

    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
