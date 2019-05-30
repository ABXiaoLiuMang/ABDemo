package com.logintest;

/**
 * create by Dale
 * create on 2019/5/30
 * description:
 */
public class Test {
    public static void main(String[] args) {
        Context context = new Context();
        WeixinUser weixinUser = new WeixinUser();
        SystemUser systemUser = new SystemUser();
        IUserSystemFactory factory = new WeiXinLoginFactory(context);
        factory.getLoginUser().login(weixinUser);
        factory.getLoginUser().logout(weixinUser);
        System.out.println("-------------------------------------");
        IUserSystemFactory factory2 = new UserLoginSystemFactory(context);
        factory2.getLoginUser().login(systemUser);
        factory2.getLoginUser().logout(systemUser);
    }
}
