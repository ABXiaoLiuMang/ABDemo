package com.design.cn.filter;

/**
 * create by Dale
 * create on 2019/5/23
 * description:
 */
public class TwoFilter extends AbsIFilter {

    @Override
    public void doSomething(String string) {
        System.out.println(getClass().getName()+"->"+string);
        if(hasNext()){
            nextFilter.doSomething(string);
        }
    }
}
