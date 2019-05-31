package com.tab;

import android.os.Bundle;

import com.cn.common.ui.AbstractMainFragment;
import com.dale.demo.R;
import com.dale.supportdemo.DemoFragment;
import com.dale.supportdemo.TestRereshFregment;
import com.me.yokeyword.fragmentation.ISupportFragment;
import com.net.TestNetFragment;

/**
 * create by Dale
 * create on 2019/5/30
 * description:
 */
public class MainFragment extends AbstractMainFragment {

    public static MainFragment newInstance() {
        
        Bundle args = new Bundle();
        
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    protected String[] getTabTexts() {
        return new String[]{"主页","购买","客服","排行榜","我的"};
    }

    @Override
    protected int[] getTabImageResIds() {
        return new int[] {R.drawable.home_buy,R.drawable.home_buy,R.drawable.home_buy,R.drawable.home_buy,R.drawable.home_buy,};
    }

    @Override
    protected Class<? extends ISupportFragment>[] getFragments() {
        return new Class[]{TabHomeFragment.class, TestRereshFregment.class, TestNetFragment.class, DemoFragment.class,TabMyFragment.class};
    }
}
