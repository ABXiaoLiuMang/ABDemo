package com.design.cn.bridge;

/**
 * create by Dale
 * create on 2019/5/23
 * description:桥接（Bridge）模式，是结构性模式
 * 将抽象化与现实化脱藕，使得二者可以独立的变化，也就是说将他们之间的强关联变成弱关联，
 * 也就是指在一个软件系统的抽象化和实现化之间使用组合/聚合关系，而不是继承关系，从而使两者可以独立的改变。
 * 将手机类型 和手机版本解耦
 */
public class TestBridge {
    public static void main(String[] args) {

        BasePhone basePhone = new PhoneApple(new VersionApple());
        basePhone.getPhoneInfo();

        BasePhone basePhone1 = new PhoneAndroid(new VersionAndroid());
        basePhone1.getPhoneInfo();
    }
}
