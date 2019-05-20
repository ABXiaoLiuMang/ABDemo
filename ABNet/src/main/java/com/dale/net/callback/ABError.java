package com.dale.net.callback;

import android.support.annotation.Nullable;

import okhttp3.Request;
import okhttp3.Response;

/**
 * create by Dale
 * create on 2019/5/20
 * description:
 */
public class ABError extends Exception{

    public int code;
    public String errorMessage;
    private Response mResponse;
    private Request mRequest;
    private Throwable rootException;
    public ABError(String message) {
        super(message);
        this.errorMessage = message;
    }

    public ABError setCode(int code) {
        this.code = code;
        return this;
    }

    @SuppressWarnings("unused")
    public ABError setMessage(String message) {
        this.errorMessage = message;
        return this;
    }

    @SuppressWarnings("unused")
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }

    public ABError setResponse(Response response) {
        mResponse = response;
        return this;
    }

    public ABError setRequest(Request request) {
        mRequest = request;
        return this;
    }

    public ABError setRootException(Throwable rootException) {
        this.rootException = rootException;
        return this;
    }

    public Response getResponse() {
        return mResponse;
    }

    public Request getRequest() {
        return mRequest;
    }


    @Nullable
    public Throwable getRootException() {
        return rootException;
    }
}
