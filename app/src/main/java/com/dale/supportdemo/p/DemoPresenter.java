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
public class DemoPresenter extends BasePresenter<DemoFragment> implements TestContract.IBindPresenter {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Dream","DemoPresenter onCreate");
        getVerificationCode();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Dream","DemoPresenter onResume");
    }

    @Override
    public void onInVisible() {
        super.onInVisible();
        Log.d("Dream","DemoPresenter onInVisible");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Dream","DemoPresenter onStart");
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Log.d("Dream","DemoPresenter onRestart");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Dream","DemoPresenter onPause");
    }

    @Override
    public void onVisible() {
        super.onVisible();
        Log.d("Dream","DemoPresenter onVisible");
    }

    @Override
    public void getVerificationCode() {
       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               getView().getBindTextVlaue("gan ni mei");
           }
       },2000);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("Dream","DemoPresenter onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Dream","DemoPresenter onDestroy");
    }
}
