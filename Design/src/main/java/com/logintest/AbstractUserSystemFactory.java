package com.logintest;


/**
 * create by Dale
 * create on 2019/5/30
 * description:
 */
public abstract class AbstractUserSystemFactory implements IUserSystemFactory {

    private Context mContext;

    public AbstractUserSystemFactory(Context mContext){
        this.mContext = mContext;
    }

    public Context getContext() {
        return mContext;
    }

}
