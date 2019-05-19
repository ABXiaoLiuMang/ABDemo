package com.cn.common.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.cn.common.util.ExitUtils;
import com.cn.common.view.StatusBarSuperUtil;
import com.me.yokeyword.fragmentation.ISupportFragment;
import com.me.yokeyword.fragmentation.SupportActivity;

/**
 * 文件描述:
 * 作者Dale:2019/4/28
 */
public class PluginFragmentActivity extends SupportActivity {
    public static final String KEY_PARAMS = "params";
    public static final String KEY_FRAGMENT = "fragment";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExitUtils.getInstance().addActivity(this);//记录打开的Act
        initSystemBar();
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

    protected void initSystemBar(){
        StatusBarSuperUtil.setColor(this, getResources().getColor(R.color.colorPrimary), 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ExitUtils.getInstance().removeActivity(this);//关闭的Act
    }
}
