package com.cn.common.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cn.common.dialog.ABProgressDialog;
import com.cn.common.senter.ABLifeRegistry;
import com.cn.common.senter.ABState;
import com.me.yokeyword.fragmentation.SupportFragment;

import java.lang.reflect.Type;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * create by Dale
 * create on 2019/5/17
 * description:所有Fragment基类
 */
public abstract class BaseFragment<P extends BasePresenter> extends SupportFragment {
    protected P presenter;
    protected Activity mContext;
    protected Bundle bundle;
    protected View rootView;
    protected Unbinder unbinder;
    private ABProgressDialog progressDialog;
    private ABLifeRegistry mLifeRegistry = new ABLifeRegistry();

    protected abstract int getLayoutId();

    protected abstract void initViewsAndEvents();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
//        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false);
//        }else {
//            ViewGroup parent = (ViewGroup) rootView.getParent();
//            if (parent != null) {
//                parent.removeView(rootView);
//            }
//            if (container != null){
//                container.removeView(rootView);
//            }
//        }


        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initPresenter();
        if (presenter != null) {
            presenter.onCreate();
        }
        initViewsAndEvents();
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

    @CallSuper
    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        mLifeRegistry.markState(ABState.INVISIBLE);
        if (presenter != null) {
            presenter.onInVisible();
        }
    }

    @CallSuper
    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        mLifeRegistry.markState(ABState.VISIBLE);
        if (presenter != null) {
            presenter.onVisible();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (presenter != null) {
            presenter.onStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (presenter != null){
            presenter.onPause();
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    /**
     * 新启动Activity跳转方式
     * @param toClass 目标 Fragment
     */
    public void goActivityFragment(Class<? extends Fragment> toClass){
       this.goActivityFragment(toClass,null);
    }

    /**
     * 新启动Activity跳转方式
     * @param toClass 目标 Fragment
     */
    public void goActivityFragment(Class<? extends Fragment> toClass,Bundle bundle){
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
                    if ("com.cn.common.ui.BaseFragment".equalsIgnoreCase(childClass.getName())){
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

}
