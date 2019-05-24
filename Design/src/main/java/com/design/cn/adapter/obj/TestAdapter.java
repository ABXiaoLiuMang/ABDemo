package com.design.cn.adapter.obj;

import com.design.cn.adapter.cls.Adaptee;
import com.design.cn.adapter.cls.ConcreteTarget;
import com.design.cn.adapter.cls.Target;

/**
 * create by Dale
 * create on 2019/5/23
 * description:类接口适配器
 * 原先一个class有基本功能，但是不符合条件（ConcreteTarget，需要实现统一接口Target），新建一个接口（Target）；
 * Adapter 实现接口，构造方法中传入需要扩展的Adaptee
 */
public class TestAdapter {
    public static void main(String[] args) {
        Target target = new ConcreteTarget();
        target.request();

        Adaptee adaptee = new Adaptee();
        target = new Adapter(adaptee);
        target.request();
    }
}
