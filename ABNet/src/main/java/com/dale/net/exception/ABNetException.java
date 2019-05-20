package com.dale.net.exception;

import com.google.gson.JsonParseException;

import retrofit2.Response;


public class ABNetException extends JsonParseException {
    private  int code;
    private  transient Response<?> response;
    public ABNetException(String msg) {
        super(msg);
    }



    public int getCode() {
        return code;
    }

    public Response<?> getResponse() {
        return response;
    }

    public void setResponse(Response<?> response) {
        this.response = response;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
