package com.design.cn.adapter.obj;

import com.design.cn.adapter.cls.Adaptee;
import com.design.cn.adapter.cls.Target;

/**
 * create by Dale
 * create on 2019/5/23
 * description:
 */
public class Adapter implements Target {

    Adaptee adaptee;

    public Adapter(Adaptee adaptee){
        this.adaptee = adaptee;
    }

    @Override
    public void request() {
        adaptee.specificRequest();
    }
}
