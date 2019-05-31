package com.dale.supportdemo;

import android.view.View;

import com.cn.common.ui.AbstractTabFragment;
import com.cn.common.ui.BaseFragment;
import com.cn.common.util.ABConfig;
import com.dale.demo.R;
import com.dale.supportdemo.p.DemoPresenter;
import com.dale.supportdemo.p.TestContract;

/**
 * 文件描述:中国刑侦一号案,惊天铁案
 * 作者Dale:2019/4/28
 */
public class DemoFragment extends AbstractTabFragment<DemoPresenter> implements TestContract.IBindView{

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_demo;
    }

    @Override
    protected void initViewsAndEvents() {
        showProgressDialog(bundle.getString(ABConfig.KEY_TEXT));
//        setBackActivity(true);
        rootView.findViewById(R.id.btn_demo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFragment(TestRereshFregment.newInstance());
            }
        });
    }


    @Override
    public void getBindTextVlaue(String text) {

//        goActivityFragment(TestRereshFregment.class);
        dismissProgressDialog();
    }
}
