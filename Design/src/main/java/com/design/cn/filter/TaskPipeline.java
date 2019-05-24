package com.design.cn.filter;

/**
 * create by Dale
 * create on 2019/5/23
 * description:
 */
public class TaskPipeline implements IPipeline {

    IFilter mFirst,mLast;

    @Override
    public IFilter first() {
        return mFirst;
    }

    @Override
    public IFilter last() {
        return mLast;
    }

    @Override
    public void setLast(IFilter filter) {
       this.mLast = filter;
    }

    @Override
    public IPipeline addFilder(IFilter filter) {
        if (this.mFirst == null) {
            this.mFirst = filter;
        } else {
            IFilter current = this.mFirst;
            while (current != null) {
                if (current.next() == null) {
                    current.setNext(filter);
                    break;
                }
                current = current.next();
            }
        }
        return this;
    }

    @Override
    public void startDoShomting() {
        mFirst.doSomething("go home");
    }
}
