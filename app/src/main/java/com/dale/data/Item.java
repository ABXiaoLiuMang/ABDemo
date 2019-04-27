package com.dale.data;

/**
 * 文件描述:
 * 作者Dale:2019/4/26
 */
public class Item {

   private String head;

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    @Override
    public String toString() {
        return "Item{" +
                "head='" + head + '\'' +
                '}';
    }
}
