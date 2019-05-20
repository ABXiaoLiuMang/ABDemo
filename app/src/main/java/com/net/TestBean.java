package com.net;

import java.util.List;

/**
 * create by Dale
 * create on 2019/5/20
 * description:
 */
public class TestBean {

    /**
     * list : []
     * play_time : 0
     */

    private int play_time;
    private List<?> list;

    public int getPlay_time() {
        return play_time;
    }

    public void setPlay_time(int play_time) {
        this.play_time = play_time;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "TestBean{" +
                "play_time=" + play_time +
                ", list=" + list +
                '}';
    }
}
