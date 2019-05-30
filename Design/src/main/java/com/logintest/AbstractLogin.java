package com.logintest;


/**
 * create by Dale
 * create on 2019/5/30
 * description:
 */
public abstract class AbstractLogin<U extends IBaseUser> implements ILoginUser<U>{

    private Context mContext;

    public AbstractLogin(Context mContext){
        this.mContext = mContext;
    }

    public Context getContext() {
        return mContext;
    }
}
