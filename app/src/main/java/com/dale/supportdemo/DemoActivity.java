package com.dale.supportdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.dale.demo.R;

/**
 * 文件描述:
 * 作者Dale:2019/4/28
 */
public class DemoActivity extends BaseSupportActivity {

    private static final String KEY_PARAMS = "params";
    private static final String KEY_FRAGMENT = "fragment";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        findViewById(R.id.btn_demo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go2(DemoFragment.class);
            }
        });

        ((Button)findViewById(R.id.btn_demo)).setText("DemoActivity");

//        setContentView(R.layout.fragment_base);
//        test();
    }

    public void go2(Class<? extends Fragment> toClass){
        Intent mIntent = new Intent();
        mIntent.putExtra(KEY_FRAGMENT,toClass.getCanonicalName());
        mIntent.setClass(DemoActivity.this,PluginFragmentActivity.class);
        startActivity(mIntent);
    }


    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private void test(){
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        DemoFragment fragment = new DemoFragment();
        fragmentTransaction.add(R.id.fragment_content,fragment).show(fragment).commit();
    }
}
