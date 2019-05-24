package com.design.cn.filter;

/**
 * create by Dale
 * create on 2019/5/23
 * description:
 */
public interface IPipeline {

    IFilter first();
    IFilter last();
    void setLast(IFilter filter);
    IPipeline addFilder(IFilter filter);
    void startDoShomting();
}
