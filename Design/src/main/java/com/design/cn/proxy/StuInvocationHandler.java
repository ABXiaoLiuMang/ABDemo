package com.design.cn.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * create by Dale
 * create on 2019/5/31
 * description:
 */
public class StuInvocationHandler<T> implements InvocationHandler {

    T targe;

    public StuInvocationHandler(T targe) {
        this.targe = targe;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object object = method.invoke(targe,args);
        System.out.println("method:"+method.getName());
        return object;
    }
}
