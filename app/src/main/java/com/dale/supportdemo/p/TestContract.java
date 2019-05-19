package com.dale.supportdemo.p;

/**
 * create by Dale
 * create on 2019/5/17
 * description:
 */
public class TestContract {
    interface IBindPresenter {
        void getVerificationCode();
    }

    public interface IBindView  {
        void getBindTextVlaue(String text);
    }
}
