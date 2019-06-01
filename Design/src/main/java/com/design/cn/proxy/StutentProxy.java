package com.design.cn.proxy;

/**
 * create by Dale
 * create on 2019/5/31
 * description:
 */
public class StutentProxy implements Person {
    private Person person;//被代理的对象（学生）

    public StutentProxy(Person person) {
        this.person = person;
    }

    @Override
    public void giveMoney() {
        person.giveMoney();
    }

    @Override
    public void eat() {
        person.eat();
    }
}
