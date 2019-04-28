package com.dale.supportdemo;

import android.support.v4.app.ActivityCompat;

import com.me.yokeyword.fragmentation.ISupportFragment;
import com.me.yokeyword.fragmentation.SupportFragment;

/**
 * 文件描述:
 * 作者Dale:2019/4/28
 */
public class BaseSupportFragment extends SupportFragment {

    private boolean isBackActivity;


    public void startFragment(ISupportFragment toFragment) {
        start(toFragment);
    }

    /**
     * @param launchMode Similar to Activity's LaunchMode.
     */
    public void startFragment(final ISupportFragment toFragment, @LaunchMode int launchMode) {
        start(toFragment, launchMode);
    }

    /**
     * Launch an fragment for which you would like a result when it poped.
     */
    public void startFragmentForResult(ISupportFragment toFragment, int requestCode) {
        startForResult(toFragment, requestCode);
    }


    @Override
    public boolean onBackPressedSupport() {
        exitActivity();
        return true;
    }

    protected void exitActivity() {
        if (isBackActivity) {
            ActivityCompat.finishAfterTransition(getActivity());
        } else {
            finish();
        }
    }

    /**
     * 是否是退出activity
     *
     * @param backActivity true 表示退出activity false 表示返回上一层fragment
     */
    public void setBackActivity(boolean backActivity) {
        isBackActivity = backActivity;
    }

    protected void finish(){
        pop();
    }

}
