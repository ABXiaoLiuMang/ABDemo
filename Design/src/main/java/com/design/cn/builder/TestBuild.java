package com.design.cn.builder;

/**
 * create by Dale
 * create on 2019/5/23
 * description:
 */
public class TestBuild {
    public static void main(String[] args) {
        Product product = new Product.Builder()
                .buildCpu("鸿基")
                .buildMemory("200内存")
                .buildDispalyCard("Intent")
                .createProduct();
        System.out.println(product.toString());
    }
}
