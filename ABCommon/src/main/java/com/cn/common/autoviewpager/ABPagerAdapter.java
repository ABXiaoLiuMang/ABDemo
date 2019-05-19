package com.cn.common.autoviewpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.ArrayList;

public abstract class ABPagerAdapter<T> extends PagerAdapter {
    ArrayList<View> viewList = new ArrayList<>();
    ArrayList<T> data = new ArrayList<>();
    protected Context mContext;
    OnItemClickListener onItemClickListener;
    public ABPagerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void initData(ArrayList<T> _list){
        data.addAll(_list);
        for(T t : _list){
            viewList.add(getItemView(t));
        }
    }

    public T getItem(int postion){
        return  data.get(postion);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //对ViewPager页号求模取出View列表中要显示的项
        position %= viewList.size();
        final int _position = position;
        if (position < 0) {
            position = viewList.size() + position;
        }
        View view = viewList.get(position);
        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
        ViewParent vp = view.getParent();
        if (vp != null) {
            ViewGroup parent = (ViewGroup) vp;
            parent.removeView(view);
        }
        container.addView(view);
        if(onItemClickListener != null){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(ABPagerAdapter.this,v, _position);
                }
            });
        }
        return view;
    }

    protected abstract View getItemView(T data);

    public interface OnItemClickListener{
        void onItemClick(ABPagerAdapter var1, View var2, int var3);
    }
}