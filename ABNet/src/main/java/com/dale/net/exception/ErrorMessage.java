package com.dale.net.exception;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Desc:错误信息集合
 **/
public class ErrorMessage {
    /**
     * 网络异常
     */
    public static final int NET_ERROR_CODE = 11001;
    /**
     * 请求异常
     */
    public static final int REQUEST_ERROR_CODE = 11005;
    /**
     * 返回结果处理异常
     */
    public static final int RESPONSE_HANDLER_ERROR = 11007;

    /**
     * 数据解析异常
     */
    public static final int DATA_PARSE_ERROR = 11000;

    /**
     * response body 为空
     */
    public static final int RESPONSE_BODY_EMPTY = 22001;
    /**
     * 错误状态码,http状态码 或者 服务器返回的状态码
     */
    private int code;

    /**
     * 出错的baseUrl 集合
     */
    private List<String> baseUrlList = new ArrayList<>();
    private String baseUrl;
    /**
     * 错误的信息 可能为空
     */
    private String message;
    /**
     * 异常信息 可能为空
     */
    private Throwable error;
    private String url;
    private Response mResponse;
    private Request mRequest;
    public ErrorMessage(int code, String message, Throwable error) {
        this.code = code;
        this.message = message;
        this.error = error;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Headers getHeaders() {
        if (mResponse == null) {
            return null;
        }
        return mResponse.headers();
    }

    @Nullable
    public Response getResponse() {
        return mResponse;
    }

    public ErrorMessage setResponse(Response response) {
        mResponse = response;
        return this;
    }

    @Nullable
    public Request getRequest() {
        return mRequest;
    }

    public ErrorMessage setRequest(Request request) {
        mRequest = request;
        return this;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public List<String> getBaseUrlList() {
        return baseUrlList;
    }

    public void setBaseUrl(List<String> baseUrl) {
        this.baseUrlList.addAll(baseUrl);
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrlList.add(baseUrl);
    }



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public String getUrl() {
        return baseUrl + url;
    }

    public void setUrl(String baseUrl, String url) {
        this.baseUrl = baseUrl;
        this.url = url;
    }
}
