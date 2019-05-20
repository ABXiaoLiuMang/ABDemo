package com.dale.net.utils;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

public class MainThreadExecutor implements Executor {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private MainThreadExecutor() {
    }

    public static MainThreadExecutor getInstance() {
        return Holder.INSTENCE;
    }

    private static class Holder {
        static final MainThreadExecutor INSTENCE = new MainThreadExecutor();
    }

    @Override
    public void execute(@NonNull Runnable command) {
        handler.post(command);
    }




}
