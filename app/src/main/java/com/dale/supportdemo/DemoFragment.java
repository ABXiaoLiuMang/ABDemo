package com.dale.supportdemo;

import android.widget.Button;

import com.cn.common.ui.BaseFragment;
import com.cn.common.util.ABConfig;
import com.dale.demo.R;
import com.dale.supportdemo.p.DemoPresenter;
import com.dale.supportdemo.p.TestContract;

/**
 * 文件描述:
 * 作者Dale:2019/4/28
 */
public class DemoFragment extends BaseFragment<DemoPresenter> implements TestContract.IBindView{

    @Override
    protected int getLayoutId() {
        return R.layout.activity_demo;
    }

    @Override
    protected void initViewsAndEvents() {
        showProgressDialog(bundle.getString(ABConfig.KEY_TEXT));
        ((Button)rootView.findViewById(R.id.btn_demo)).setText("DemoFragment");
        setBackActivity(true);
    }


    @Override
    public void getBindTextVlaue(String text) {
        startFragment(TestRereshFregment.newInstance());
        dismissProgressDialog();
    }
}
