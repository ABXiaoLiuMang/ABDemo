package com.dale.net.exception;


import com.dale.net.Request;

public class ABNetError extends Throwable {
    private Request request;
    private Object cache;
    private ErrorMessage error;

    public ABNetError(Request request, ErrorMessage error , Object cache) {
        super(error.getMessage());
        this.request = request;
        this.cache = cache;
        this.error = error;
    }

    public Request getRequest() {
        return request;
    }

    public Object getCache() {
        return cache;
    }

    public ErrorMessage getError() {
        return error;
    }
}
