package com.cn.common.ui;

import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

/**
 * create by Dale
 * create on 2019/5/17
 * description:
 */
public abstract class ABRefreshBaseFragment<T, P extends BasePresenter> extends BaseFragment implements OnRefreshListener, OnLoadmoreListener {

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
        childPresenter = (P) presenter;
        recyclerview = rootView.findViewById(R.id.recyclerview);
        refreshLayout = rootView.findViewById(R.id.refreshLayout);
        switch (mode){
            case PULL_FROM_END:
                refreshLayout.setOnLoadmoreListener(this);
                break;
            case PULL_FROM_START://顶部下拉刷新
                refreshLayout.setOnRefreshListener(this);
                break;
            case BOTH://上下都刷新
                refreshLayout.setOnRefreshListener(this);
                refreshLayout.setOnLoadmoreListener(this);
                break;
            case DISABLED://上下都不刷新
                break;
        }

        listAdapter = getListAdapter();
        recyclerview.setLayoutManager(getLayoutManager());
        RecyclerView.ItemDecoration itemDecoration = getItemDecoration();
        if (itemDecoration != null) {
            recyclerview.addItemDecoration(itemDecoration);
        }
        recyclerview.setAdapter(listAdapter);
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    /**
     * @return ListView适配器
     */
    public abstract BaseQuickAdapter<T, BaseViewHolder> getListAdapter();

    public abstract RecyclerView.LayoutManager getLayoutManager();

    public abstract RecyclerView.ItemDecoration getItemDecoration();

    public static enum Mode {

        DISABLED(0x0),

        PULL_FROM_START(0x1),

        PULL_FROM_END(0x2),

        BOTH(0x3);

        static Mode getDefault() {
            return PULL_FROM_START;
        }

        private int mIntValue;

        Mode(int modeInt) {
            mIntValue = modeInt;
        }
    }
}
