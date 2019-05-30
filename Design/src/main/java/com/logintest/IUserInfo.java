package com.logintest;

/**
 * create by Dale
 * create on 2019/5/30
 * description:
 */
public interface IUserInfo<U extends IBaseUser> {
    U getUserInfo();
    void saveUserInfo(U user);
}
