package com.dale.supportdemo;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.baserecyclerviewadapterhelper.adapter.TestAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cn.common.ui.ABRefreshBaseFragment;
import com.dale.supportdemo.p.TestPresenter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;

/**
 * create by Dale
 * create on 2019/5/17
 * description:
 */
public class TestRereshFregment extends ABRefreshBaseFragment<String, TestPresenter> {
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
        return null;
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        listAdapter.addData(getDatas());

        refreshLayout.finishLoadmore();      //加载完成
// refreshlayout.cl
//        refreshLayout.finishLoadMoreWithNoMoreData();  //全部加载完成,没有数据了调用此方法
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        listAdapter.setNewData(getDatas());
        //请求数据
        refreshLayout.finishRefresh();  //刷新完成
    }


    private ArrayList<String> getDatas(){
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            list.add("");
        }
        return list;

    }
}
