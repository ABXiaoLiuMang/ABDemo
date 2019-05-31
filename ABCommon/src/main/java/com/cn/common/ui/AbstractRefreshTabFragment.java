package com.cn.common.ui;

import android.support.v4.app.Fragment;

import com.me.yokeyword.fragmentation.ISupportFragment;

/**
 * create by Dale
 * create on 2019/5/30
 * description: 主界面切换tab的刷新基类
 */
public abstract class AbstractRefreshTabFragment<T, P extends BasePresenter> extends ABRefreshBaseFragment<T, P> {
    public void startFragment(ISupportFragment toFragment) {
        Fragment fragment = getParentFragment();
        if (fragment instanceof AbstractMainFragment) {
            ((AbstractMainFragment) getParentFragment()).startFragment(toFragment);
        } else {
            super.startFragment(toFragment);
        }
    }

}
