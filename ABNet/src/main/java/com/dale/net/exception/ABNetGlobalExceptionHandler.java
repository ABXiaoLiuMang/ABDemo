package com.dale.net.exception;


import com.dale.net.Request;

/**
 * Desc:全局异常处理器
 **/
public interface ABNetGlobalExceptionHandler {

    void exception(Request<?> request, ErrorMessage error);

    boolean needRetry(Request<?> request, ErrorMessage error);
}
