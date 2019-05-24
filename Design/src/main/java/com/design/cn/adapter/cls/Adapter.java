package com.design.cn.adapter.cls;

/**
 * create by Dale
 * create on 2019/5/23
 * description:
 */
public class Adapter extends Adaptee implements Target {
    @Override
    public void request() {
        super.specificRequest();
    }
}
