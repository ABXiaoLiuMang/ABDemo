package com.design.cn.factory;

/**
 * create by Dale
 * create on 2019/5/23
 * description:
 */
public class Factory implements IFactory{
    @Override
    public IProduct1 crateIProduct1A() {
        return new CradeProduct1A();
    }

    @Override
    public IProduct1 crateIProduct1B() {
        return new CradeProduct1B();
    }

    @Override
    public IProduct2 crateIProduct2A() {
        return new CradeProduct2A();
    }

    @Override
    public IProduct2 crateIProduct2B() {
        return new CradeProduct2B();
    }
}
