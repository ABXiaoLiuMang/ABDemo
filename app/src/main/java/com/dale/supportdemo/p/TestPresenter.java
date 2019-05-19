package com.dale.supportdemo.p;


import android.os.Handler;
import android.util.Log;

import com.cn.common.ui.BasePresenter;
import com.dale.supportdemo.DemoFragment;


/**
 * create by Dale
 * create on 2019/5/17
 * description:
 */
public class TestPresenter extends BasePresenter<DemoFragment> implements TestContract.IBindPresenter {

    @Override
    public void onCreate() {
        super.onCreate();
        getVerificationCode();
    }

    @Override
    public void getVerificationCode() {
       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               getView().getBindTextVlaue("gan  ni mei");
           }
       },2000);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("Dream","TestPresenter onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Dream","TestPresenter onDestroy");
    }
}
