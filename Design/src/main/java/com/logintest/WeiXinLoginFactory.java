package com.logintest;


/**
 * create by Dale
 * create on 2019/5/30
 * description:
 */
public class WeiXinLoginFactory extends AbstractUserSystemFactory {
    public WeiXinLoginFactory(Context mContext) {
        super(mContext);
    }

    @Override
    public ILoginUser getLoginUser() {
        return new WeiXinLogin(getContext());
    }

    @Override
    public IFindPwder getFindPwder() {
        return null;
    }

    @Override
    public IRegister getRegister() {
        return null;
    }

    @Override
    public IUserInfo getUserInfo() {
        return null;
    }
}
