package com.design.cn.adapter.cls;

/**
 * create by Dale
 * create on 2019/5/23
 * description:
 */
public class ConcreteTarget implements Target {
    @Override
    public void request() {
        System.out.println("基本接口实现，只要普通功能");
    }
}
