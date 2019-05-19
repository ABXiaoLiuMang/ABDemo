package com.dale.supportdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cn.common.ui.BaseActivity;
import com.cn.common.util.ABConfig;
import com.dale.demo.R;

/**
 * 文件描述: 测试全局一个activity
 * 作者Dale:2019/4/28
 */
public class DemoActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_demo;
    }

    @Override
    protected void initViewsAndEvents() {
        findViewById(R.id.btn_demo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(ABConfig.KEY_TEXT, "wo gan");
                goActivityFragment(DemoFragment.class, bundle);
            }
        });

        ((Button) findViewById(R.id.btn_demo)).setText("DemoActivity");
    }
}
