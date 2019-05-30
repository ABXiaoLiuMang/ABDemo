package com.logintest;

/**
 * create by Dale
 * create on 2019/5/30
 * description:
 */
public class WeixinUser implements IBaseUser {


    String name = "微信";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
