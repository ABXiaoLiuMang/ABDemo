package com.logintest;

/**
 * create by Dale
 * create on 2019/5/30
 * description:
 */
public interface IRegister<U extends IBaseUser> {
    void registerAccout(U user);
}
