package com.dale.supportdemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.baserecyclerviewadapterhelper.adapter.TestAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cn.common.ui.ABRefreshBaseFragment;
import com.dale.demo.R;
import com.dale.supportdemo.p.TestContract;
import com.dale.supportdemo.p.TestPresenter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;

/**
 * create by Dale
 * create on 2019/5/17
 * description:
 */
public class TestRereshFregment extends ABRefreshBaseFragment<String, TestPresenter> implements TestContract.IBindView {


    public static TestRereshFregment newInstance() {
        Bundle args = new Bundle();
        TestRereshFregment fragment = new TestRereshFregment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_testreresh;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshLayout.autoRefresh();
    }

    @Override
    protected void initViewsAndEvents() {
        setMode(Mode.BOTH);
        super.initViewsAndEvents();
    }

    @Override
    public BaseQuickAdapter<String, BaseViewHolder> getListAdapter() {
        return new TestAdapter();
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(mContext);
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
    }

    @Override
    public void onLoadMore(final RefreshLayout refreshlayout) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                page++;
                listAdapter.addData(getDatas());
                refreshLayout.finishLoadMore();      //加载完成
                if(page ==4){
                    refreshlayout.finishLoadMoreWithNoMoreData();
                }
            }
        }, 2000);



    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                page = 0;
                listAdapter.setNewData(getDatas());
                //请求数据
                refreshLayout.finishRefresh();  //刷新完成
            }
        }, 2000);
    }

    int page = 0;
    int count = 10;

    private ArrayList<String> getDatas() {
        ArrayList<String> list = new ArrayList<>();
        int i = page * count;
        for (int j = i; j < page * count + count; j++) {
            list.add("我艹：" + j);
        }
        return list;

    }

    @Override
    public void getBindTextVlaue(String text) {
        listAdapter.addData(getDatas());
    }
}
