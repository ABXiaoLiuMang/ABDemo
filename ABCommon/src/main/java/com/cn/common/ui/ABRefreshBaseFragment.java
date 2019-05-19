package com.cn.common.ui;

import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

/**
 * create by Dale
 * create on 2019/5/17
 * description:下拉刷新基类
 */
public abstract class ABRefreshBaseFragment<T, P extends BasePresenter> extends BaseFragment implements OnRefreshListener, OnLoadMoreListener, BaseQuickAdapter.OnItemClickListener {

    protected BaseQuickAdapter<T, BaseViewHolder> listAdapter;
    protected SmartRefreshLayout refreshLayout;
    protected RecyclerView recyclerview;
    private Mode mode = Mode.getDefault();
    protected P childPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_refresh;
    }

    @Override
    protected void initViewsAndEvents() {
        if(presenter != null){
            childPresenter = (P) presenter;
        }
        recyclerview = rootView.findViewById(R.id.recyclerview);
        refreshLayout = rootView.findViewById(R.id.refreshLayout);
        switch (mode){
            case PULL_FROM_END://上拉加载更多
                refreshLayout.setOnLoadMoreListener(this);
                refreshLayout.setEnableRefresh(false);
                refreshLayout.setEnableLoadMore(true);
                break;
            case PULL_FROM_START://顶部下拉刷新
                refreshLayout.setOnRefreshListener(this);
                refreshLayout.setEnableRefresh(true);
                refreshLayout.setEnableLoadMore(false);
                break;
            case BOTH://上下都刷新
                refreshLayout.setOnRefreshListener(this);
                refreshLayout.setOnLoadMoreListener(this);
                break;
            case DISABLED://上下都不刷新
                refreshLayout.setEnableLoadMore(false);
                refreshLayout.setEnableRefresh(false);
                break;
        }

        listAdapter = getListAdapter();
        listAdapter.setOnItemClickListener(this);
        recyclerview.setLayoutManager(getLayoutManager());
        RecyclerView.ItemDecoration itemDecoration = getItemDecoration();
        if (itemDecoration != null) {
            recyclerview.addItemDecoration(itemDecoration);
        }
        recyclerview.setAdapter(listAdapter);
    }

    protected void setMode(Mode mode) {
        this.mode = mode;
    }

    /**
     * @return ListView适配器
     */
    protected abstract BaseQuickAdapter<T, BaseViewHolder> getListAdapter();

    protected abstract RecyclerView.LayoutManager getLayoutManager();

    protected abstract RecyclerView.ItemDecoration getItemDecoration();

    protected enum Mode {

        DISABLED,

        PULL_FROM_START,

        PULL_FROM_END,

        BOTH;

        static Mode getDefault() {
            return BOTH;
        }
    }
}
