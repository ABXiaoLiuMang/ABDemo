package com.design.cn.bridge;

/**
 * create by Dale
 * create on 2019/5/23
 * description:
 */
public class PhoneApple extends BasePhone {

    public PhoneApple(BasePhoneVersion basePhoneVersion) {
        super(basePhoneVersion);
    }

    @Override
    public String phoneType() {
        return "苹果手机";
    }
}
