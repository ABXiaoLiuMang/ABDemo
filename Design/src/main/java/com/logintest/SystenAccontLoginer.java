package com.logintest;


/**
 * create by Dale
 * create on 2019/5/30
 * description:
 */
public class SystenAccontLoginer extends AbstractLogin<SystemUser>{
    public SystenAccontLoginer(Context mContext) {
        super(mContext);
    }

    @Override
    public void login(SystemUser user) {
        System.out.println("我是系统登录:"+user.getName());
    }

    @Override
    public void logout(SystemUser user) {
        System.out.println("我是系统退出:"+user.getName());
    }
}
