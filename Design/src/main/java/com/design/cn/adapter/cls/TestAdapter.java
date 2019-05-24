package com.design.cn.adapter.cls;

/**
 * create by Dale
 * create on 2019/5/23
 * description:类接口适配器
 * 原先一个class有基本功能，但是不符合条件（ConcreteTarget，需要实现统一接口Target），新建一个接口（Target）；
 * Adapter 实现接口，集成Adaptee（目标扩展方法）
 */
public class TestAdapter {
    public static void main(String[] args) {
        Target target = new ConcreteTarget();
        target.request();

        target = new Adapter();
        target.request();
    }
}
