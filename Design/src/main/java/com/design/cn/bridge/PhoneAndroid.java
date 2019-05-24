package com.design.cn.bridge;

/**
 * create by Dale
 * create on 2019/5/23
 * description:
 */
public class PhoneAndroid extends BasePhone {

    public PhoneAndroid(BasePhoneVersion basePhoneVersion) {
        super(basePhoneVersion);
    }

    @Override
    public String phoneType() {
        return "安卓手机";
    }
}
