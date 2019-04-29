package com.dale.supportdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import com.dale.demo.R;
import com.me.yokeyword.fragmentation.ISupportFragment;

/**
 * 文件描述:
 * 作者Dale:2019/4/28
 */
public class PluginFragmentActivity extends BaseSupportActivity {

    private static final String KEY_PARAMS = "params";
    private static final String KEY_FRAGMENT = "fragment";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("Dream","PluginFragmentActivity onCreate");
        try{
            Intent intent = getIntent();
            if (intent == null) return;
            Bundle bundle = intent.getBundleExtra(KEY_PARAMS);
            String stringClass = intent.getStringExtra(KEY_FRAGMENT);
            if (TextUtils.isEmpty(stringClass)) {
                return;
            }
            Class<?> fClass = Class.forName(stringClass);
            Object instance = fClass.newInstance();

            if (instance instanceof ISupportFragment){
                Fragment fragment = (Fragment)instance;
                if (bundle != null){
                    fragment.setArguments(bundle);
                }
                loadRootFragment(android.R.id.content, (ISupportFragment) fragment,false,false);
            }


        }catch (Exception e){
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Dream","PluginFragmentActivity onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Dream","PluginFragmentActivity onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Dream","PluginFragmentActivity onStop");
    }

    @Override
    public void finish() {
        super.finish();
//        overridePendingTransition(R.anim.h_fragment_pop_enter, R.anim.h_fragment_exit);
    }
}
