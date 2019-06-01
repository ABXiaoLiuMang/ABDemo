package com.design.cn.proxy;

/**
 * create by Dale
 * create on 2019/5/31
 * description:
 */
public class Student implements Person {

    public Student(String name) {
        this.name = name;
    }

    String name;
    @Override
    public void giveMoney() {
        System.out.println(name+"->交了50块班费");
    }

    @Override
    public void eat() {
        System.out.println(name+"->吃馒头");
    }
}
