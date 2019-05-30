package com.logintest;

/**
 * create by Dale
 * create on 2019/5/30
 * description:
 */
public class SystemUser implements IBaseUser {


    String name = "系统";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
