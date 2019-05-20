package com.cn.common.ui;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.SupportActivity;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * create by Dale
 * create on 2019/5/17
 * description:
 */
public class BasePresenter<V>{
    private WeakReference<V> mView;
    private WeakReference<FragmentActivity> activityRef;
    private WeakReference<Fragment> fragmentRef;
    public void setView(V v) {
        this.mView = new WeakReference<>(v);
        checkContext(v);
        checkFragment(v);
    }

    private void checkFragment(V v) {
        if (v instanceof Fragment){
            Fragment fragment = (Fragment) v;
            fragmentRef = new WeakReference<>(fragment);
        }
    }

    private void checkContext(V v) {
        Activity activity;
        if (v instanceof SupportActivity) {
            activity = (Activity) v;
            setContext(activity);
        }else if(v instanceof Fragment){
            activity = ((Fragment)v).getActivity();
            setContext(activity);
        }else if (v instanceof View){
            activity = (Activity) ((View)v).getContext();
            setContext(activity);
        }else if (v instanceof Dialog){
            activity = (Activity) ((Dialog)v).getContext();
            setContext(activity);
        }else {
            throw new IllegalArgumentException("v must extend Activity / Fragment / View / Dialog");
        }
    }

    private void setContext(Activity activity){
        if (activity instanceof FragmentActivity) {
            activityRef = new WeakReference<>((FragmentActivity)activity);
        }else {
            throw new IllegalArgumentException("Activity/Fragment must extend android.support.v7 Activity/Fragment");
        }
    }

    @CallSuper
    public void onCreate(){
//        EventUtils.register(this);
    }

    @CallSuper
    public void onStart(){}

    @CallSuper
    public void onRestart(){}

    /**
     * 仅Fragment有此回调
     */
    @CallSuper
    public void onVisible(){}

    /**
     * 仅Fragment有此回调
     */
    @CallSuper
    public void onInVisible(){
    }

    @CallSuper
    public void onResume(){}

    @CallSuper
    public void onPause(){}

    @CallSuper
    public void onStop(){}

    @CallSuper
    public void onDestroy(){
//        EventUtils.unregister(this);
        onDetached();
    }

    /**
     * desc: 界面跳转event接收
     */
//    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
//    public final void  getEvent(YaboEvent event){
//        if (event.needGetEvent(this)){
//            handleEvent(event);
//        }
//    }
//
//    public void handleEvent(YaboEvent event){
//
//    }

    /**
     * desc: 获取当前View 可能为空,请做好非空判断
     */
    public V getView(){
        if (mView == null) return null;
        return mView.get();
    }

    @Nullable
    public FragmentActivity getActivity(){
        if (activityRef == null) return null;
        return activityRef.get();
    }

    @Nullable
    public Fragment getFragment() {
        if (fragmentRef == null) return null;
        return fragmentRef.get();
    }

    private void onDetached() {
        if (mView != null){
            mView.clear();
            mView = null;
        }

        if (fragmentRef != null){
            fragmentRef.clear();
            fragmentRef = null;
        }

        if (activityRef != null){
            activityRef.clear();
            activityRef = null;
        }
    }
}
