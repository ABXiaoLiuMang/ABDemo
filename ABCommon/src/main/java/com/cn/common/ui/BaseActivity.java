package com.cn.common.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.cn.common.dialog.ABProgressDialog;
import com.cn.common.senter.ABLifeRegistry;
import com.cn.common.util.ExitUtils;
import com.cn.common.view.StatusBarSuperUtil;
import com.me.yokeyword.fragmentation.SupportActivity;

import java.lang.reflect.Type;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * create by Dale
 * create on 2019/5/17
 * description:
 */
public abstract class BaseActivity<P extends BasePresenter> extends SupportActivity {
    protected P presenter;
    protected Activity mContext;
    protected Bundle bundle;
    protected Unbinder unbinder;
    private ABProgressDialog progressDialog;
    private ABLifeRegistry mLifeRegistry = new ABLifeRegistry();

    protected abstract int getLayoutId();

    protected abstract void initViewsAndEvents();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ExitUtils.getInstance().addActivity(this);//记录打开的Act
        initSystemBar();
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        bundle = getIntent().getExtras();
        initPresenter();
        if (presenter != null) {
            presenter.onCreate();
        }
        initViewsAndEvents();
    }


    @Override
    public void onStart() {
        super.onStart();
        if (presenter != null) {
            presenter.onStart();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (presenter != null) {
            presenter.onRestart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.onResume();
        }
        if (presenter != null) {
            presenter.onVisible();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (presenter != null) {
            presenter.onPause();
        }
        if (presenter != null) {
            presenter.onInVisible();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (presenter != null){
            presenter.onStop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLifeRegistry.clear();
        if (presenter != null){
            presenter.onDestroy();
        }

        if (unbinder != null) {
            unbinder.unbind();
        }
        ExitUtils.getInstance().removeActivity(this);//关闭的Act
    }


    protected void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ABProgressDialog(mContext);
        }
        progressDialog.show();
    }

    protected void showProgressDialog(String string) {
        progressDialog = new ABProgressDialog(mContext).setTextProgress(string);
        progressDialog.show();
    }

    protected void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void goActivity(Class<? extends Activity> descClass) {
        this.goActivity(descClass,null);
    }

    public void goActivity(Class<? extends Activity> descClass, Bundle bundle) {
        try {
            Intent intent = new Intent();
            intent.setClass(mContext, descClass);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            mContext.startActivityForResult(intent, 0);
            mContext.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        } catch (Exception e) {
        }
    }

    public void goFragment(Class<? extends Fragment> toClass){
        this.goFragment(toClass,null);
    }

    public void goFragment(Class<? extends Fragment> toClass,Bundle bundle){
        Intent mIntent = new Intent();
        mIntent.putExtra(PluginFragmentActivity.KEY_FRAGMENT,toClass.getCanonicalName());
        if(bundle != null){
            mIntent.putExtra(PluginFragmentActivity.KEY_PARAMS,bundle);
        }
        mIntent.setClass(mContext, PluginFragmentActivity.class);
        startActivity(mIntent);
    }

    private void initPresenter() {
        try {
            Class<?> childClass = this.getClass();
            Type childType = childClass.getGenericSuperclass();
            presenter = Util.createPresenter(childType);
            while (presenter == null){
                childClass = childClass.getSuperclass();
                if (childClass == null){
                    break;
                }else {
                    if ("com.cn.common.ui.BaseActivity".equalsIgnoreCase(childClass.getName())){
                        break;
                    }
                    childType = childClass.getGenericSuperclass();
                    presenter = Util.createPresenter(childType);
                }
            }

            if (presenter != null){
                presenter.setView(this);
            }
        } catch (Exception e) {
        }
    }

    protected void initSystemBar(){
        StatusBarSuperUtil.setColor(this, getResources().getColor(R.color.colorPrimary), 0);
    }

}
