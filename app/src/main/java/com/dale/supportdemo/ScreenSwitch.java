package com.dale.supportdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dale.demo.R;


/**
 * Activity界面跳转
 */
public class ScreenSwitch {

    public static void switchActivity(Context context, Class<?> descClass, Bundle bundle) {
//        Class<?> mClass = context.getClass();
//        if (mClass == descClass) {
//            return;
//        }
        try {
            Intent intent = new Intent();
            intent.setClass(context, descClass);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            ((Activity) context).startActivityForResult(intent, 0);
            ((Activity) context).overridePendingTransition(R.anim.h_fragment_enter, R.anim.h_fragment_exit);
        } catch (Exception e) {
        }
    }


    public static void finish(Activity context) {
        context.finish();
        context.overridePendingTransition(R.anim.h_fragment_pop_enter, R.anim.h_fragment_pop_exit);
    }

}
