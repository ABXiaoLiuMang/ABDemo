package com.logintest;

/**
 * create by Dale
 * create on 2019/5/30
 * description:
 */
public interface ILoginUser <U extends IBaseUser> {
    void login(U user);
    void logout(U user);
}
