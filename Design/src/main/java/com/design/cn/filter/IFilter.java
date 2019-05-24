package com.design.cn.filter;

/**
 * create by Dale
 * create on 2019/5/23
 * description:
 */
public interface IFilter {
     IFilter next();
     void setNext(IFilter filter);
     void doSomething(String string);
}
