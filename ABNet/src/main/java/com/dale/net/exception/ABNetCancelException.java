package com.dale.net.exception;

/**
 * create by Dale
 * create on 2019/5/19
 * description:
 */
public class ABNetCancelException extends Exception{
    public static ABNetCancelException create(){
        return new ABNetCancelException();
    }
}
