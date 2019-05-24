package com.design.cn.factory;

/**
 * create by Dale
 * create on 2019/5/23
 * description:
 */
public class TestFactory {
    public static void main(String[] args) {
        IFactory factory = new Factory();
        factory.crateIProduct1A().show();
        factory.crateIProduct1B().show();
        factory.crateIProduct2A().show();
        factory.crateIProduct2B().show();
    }
}
