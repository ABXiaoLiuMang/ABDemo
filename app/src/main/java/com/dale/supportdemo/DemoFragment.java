package com.dale.supportdemo;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cn.common.ui.BaseFragment;
import com.cn.common.util.ABConfig;
import com.dale.demo.R;
import com.dale.supportdemo.p.TestContract;
import com.dale.supportdemo.p.TestPresenter;

/**
 * 文件描述:
 * 作者Dale:2019/4/28
 */
public class DemoFragment extends BaseFragment<TestPresenter> implements TestContract.IBindView{


    @Override
    protected int getLayoutId() {
        return R.layout.activity_demo;
    }

    @Override
    protected void initViewsAndEvents() {
        rootView.findViewById(R.id.btn_demo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Dream","DemoFragment onViewCreated");
                startFragment(new SecondFragment());
            }
        });

        ((Button)rootView.findViewById(R.id.btn_demo)).setText("DemoFragment");
        setBackActivity(true);
    }


    @Override
    public void getBindTextVlaue(String text) {
        showProgressDialog(text+"-"+bundle.getString(ABConfig.KEY_TEXT));
    }
}
