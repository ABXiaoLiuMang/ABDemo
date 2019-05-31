package com.tab;

import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;

import com.apkfuns.logutils.LogUtils;
import com.cn.common.view.ABToast;
import com.me.yokeyword.fragmentation.SupportActivity;
import com.me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import com.me.yokeyword.fragmentation.anim.FragmentAnimator;

import java.util.List;

public class MainActivity extends SupportActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadRootFragment(android.R.id.content, MainFragment.newInstance());
    }


    @Override
    public void onBackPressedSupport() {
        // 对于 4个类别的主Fragment内的回退back逻辑,已经在其onBackPressedSupport里各自处理了
        super.onBackPressedSupport();
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count > 1) {
            pop();
        } else {
            exitApp(2000);
        }
    }

    private long mExitTime;
    public void exitApp(long doubleClickTime) {
        try {
            if (doubleClickTime <= 0){
                Process.killProcess(android.os.Process.myPid());
                System.gc();
                System.exit(0);
            }else if ((System.currentTimeMillis() - mExitTime) > doubleClickTime) {
                ABToast.show("再按一次退出程序");
                mExitTime = System.currentTimeMillis();
            } else {
                Process.killProcess(android.os.Process.myPid());
                System.gc();
                System.exit(0);
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
    }

    /**
     * 解决Fragment中的onActivityResult()方法无响应问题。
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getSupportFragmentManager().getFragments() != null && getSupportFragmentManager().getFragments().size() > 0) {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            for (Fragment mFragment : fragments) {
                mFragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    public void test(){
        ABToast.show("-->"+ getSupportFragmentManager().getBackStackEntryCount());
    }

}
