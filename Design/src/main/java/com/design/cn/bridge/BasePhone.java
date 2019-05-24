package com.design.cn.bridge;

/**
 * create by Dale
 * create on 2019/5/23
 * description:
 */
public abstract class BasePhone<T extends BasePhoneVersion> {

    T basePhoneVersion;

    public BasePhone(T basePhoneVersion) {
        this.basePhoneVersion = basePhoneVersion;
    }

    public void getPhoneInfo(){
        System.out.println(phoneType()+"->"+basePhoneVersion.phoneVersion());
    }

    public abstract String phoneType();
}
