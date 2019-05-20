package com.dale.net.callback;

import com.dale.net.Request;
import com.dale.net.exception.ErrorMessage;

/**
 * 请求的回调
 */

public interface OnCallBack<T> {

    void success(Request<T> request, T t) throws Exception;

    void error(Request<T> request, ErrorMessage error, T cache);
}
