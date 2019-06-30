package com.dale.supportdemo;

import android.os.Process;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;

import com.apkfuns.logutils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cn.common.ui.BaseActivity;
import com.cn.common.util.ExitUtils;
import com.cn.common.view.ABToast;
import com.dale.demo.R;

import java.util.List;

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
//        findViewById(R.id.btn_demo).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putString(ABConfig.KEY_TEXT, "正在加载...");
//                goActivityFragment(DemoFragment.class, bundle);
//            }
//        });
//
//        ((Button) findViewById(R.id.btn_demo)).setText("DemoActivity");
        BaseQuickAdapter baseQuickAdapter;
        loadRootFragment(R.id.root_layout,new DemoFragment() );


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
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            Fragment fragment = fragments.get(fragments.size() - 1);
            pop();
        } else {
            exitApp(2000);
        }
    }

    private long mExitTime;
    public void exitApp(long doubleClickTime) {
        try {
            if (doubleClickTime <= 0){
                ExitUtils.getInstance().finishAll();
                Process.killProcess(android.os.Process.myPid());
                System.gc();
                System.exit(0);
            }else if ((System.currentTimeMillis() - mExitTime) > doubleClickTime) {
                ABToast.show("再按一次退出程序");
                mExitTime = System.currentTimeMillis();
            } else {
                ExitUtils.getInstance().finishAll();
                Process.killProcess(android.os.Process.myPid());
                System.gc();
                System.exit(0);
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
    }

}
