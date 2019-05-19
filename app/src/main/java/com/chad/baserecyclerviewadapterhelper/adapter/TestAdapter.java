package com.chad.baserecyclerviewadapterhelper.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dale.demo.R;

/**
 * 文 件 名: AnimationAdapter
 * 创 建 人: Allen
 * 创建日期: 16/12/24 15:33
 * 邮   箱: AllenCoder@126.com
 * 修改时间：
 * 修改备注：
 */
public class TestAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public TestAdapter() {
        super(R.layout.item_test);
    }


    @Override
    protected void convert(BaseViewHolder helper, String item) {
           helper.setText(R.id.tv_test,item);
    }
}
