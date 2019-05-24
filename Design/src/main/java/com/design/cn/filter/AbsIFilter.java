package com.design.cn.filter;

/**
 * create by Dale
 * create on 2019/5/23
 * description:
 */
public abstract class AbsIFilter implements IFilter {

  protected  IFilter nextFilter;

    @Override
    public boolean hasNext() {
        return nextFilter != null;
    }

    @Override
    public void setNext(IFilter filter) {
        this.nextFilter = filter;
    }
}
