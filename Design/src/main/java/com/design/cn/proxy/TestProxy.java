package com.design.cn.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * create by Dale
 * create on 2019/5/31
 * description:
 */
public class TestProxy {
    public static void main(String[] args) {
        //静态代理演示
        Person person1 = new Student("李四");
        Person proxy1 = new StutentProxy(person1);
        proxy1.giveMoney();
        proxy1.eat();

        System.out.println("----------------");

        //动态代理演示
        Person person = new Student("张三");
        InvocationHandler invocationHandler = new StuInvocationHandler<>(person);
        Person proxy = (Person) Proxy.newProxyInstance(Person.class.getClassLoader(), new Class<?>[]{Person.class}, invocationHandler);
        proxy.giveMoney();
        proxy.eat();
    }
}
