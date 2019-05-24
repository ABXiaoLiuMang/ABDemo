package com.design.cn.filter;


/**
 * create by Dale
 * create on 2019/5/23
 * description:
 */
public class TestFilter {
    public static void main(String[] args) {
        IPipeline pipeline = new TaskPipeline()
                .addFilder(new OneFilter())
                .addFilder(new TwoFilter())
                .addFilder(new ThreeFilter())
                .addFilder(new FourFilter());
        pipeline.startDoShomting();
    }
}
