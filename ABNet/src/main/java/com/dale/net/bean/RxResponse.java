package com.dale.net.bean;


import com.dale.net.Request;


public class RxResponse<T> {

    T t;
    Request<T> request;


    public T result() {
        return t;
    }

    public RxResponse<T> setResult(T t) {
        this.t = t;
        return this;
    }

    public Request<T> request() {
        return request;
    }

    public RxResponse<T> setRequest(Request<T> request) {
        this.request = request;
        return this;
    }
}
