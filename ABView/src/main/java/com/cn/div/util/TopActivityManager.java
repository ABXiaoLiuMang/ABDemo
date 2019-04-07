package com.cn.div.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;


public class TopActivityManager implements Application.ActivityLifecycleCallbacks{

    private Reference<Activity> mCurActivity = null;

    public static TopActivityManager getInstance() {
        return SingltonHolder.mInstance;
    }

    static class SingltonHolder{
        static final TopActivityManager mInstance = new TopActivityManager();
    }

    private TopActivityManager() {

    }

    public void init(Application app){
        app.registerActivityLifecycleCallbacks(this);
    }

    public Activity getCurActivity() {
        if (mCurActivity == null) return null;
        return mCurActivity.get();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (mCurActivity != null && mCurActivity.get() != null){
            mCurActivity.clear();
            mCurActivity = null;
        }

        mCurActivity = new WeakReference<>(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
//        LogUtils.i("%s on resume",activity.getClass().getSimpleName());
        if (mCurActivity != null && mCurActivity.get() != null && !mCurActivity.get().equals(activity)){
            mCurActivity.clear();
            mCurActivity = null;
        }

        if (mCurActivity == null || mCurActivity.get() == null){
            mCurActivity = new WeakReference<>(activity);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
//        LogUtils.i("%s 是否相等 ：%s",activity.getClass().getSimpleName(),(mCurActivity != null && mCurActivity.get() != null && mCurActivity.get().equals(activity) ));

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (mCurActivity != null && mCurActivity.get() != null && mCurActivity.get().equals(activity)) {
            mCurActivity.clear();
            mCurActivity = null;
        }
    }


}

