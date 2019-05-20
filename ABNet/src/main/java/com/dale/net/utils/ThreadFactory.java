package com.dale.net.utils;

import java.util.concurrent.ScheduledThreadPoolExecutor;


public class ThreadFactory {

    public static ScheduledThreadPoolExecutor newTimeThreadPool(int size){
        return new ScheduledThreadPoolExecutor(size);
    }
}
