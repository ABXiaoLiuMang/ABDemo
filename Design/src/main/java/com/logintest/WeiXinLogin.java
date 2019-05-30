package com.logintest;


/**
 * create by Dale
 * create on 2019/5/30
 * description:
 */
public class WeiXinLogin extends AbstractLogin<WeixinUser>{
    public WeiXinLogin(Context mContext) {
        super(mContext);
    }

    @Override
    public void login(WeixinUser user) {
        System.out.println("我是微信登录："+ user.getName());
    }

    @Override
    public void logout(WeixinUser user) {
        System.out.println("我是微信退出登录:"+user.getName());
    }
}
