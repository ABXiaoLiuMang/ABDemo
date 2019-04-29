package com.dale.demo;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;


/**
 * 文件描述:
 * 作者Dale:2019/3/15
 */
public interface BaseLifecycleInterface extends LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onStop(LifecycleOwner owner);


}
