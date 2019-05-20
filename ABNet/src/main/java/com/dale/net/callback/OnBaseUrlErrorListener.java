package com.dale.net.callback;


import com.dale.net.exception.ErrorMessage;

/**
 * Desc:请求失败的baseUrl
 **/
public interface OnBaseUrlErrorListener {
    boolean onError(ErrorMessage errorMessage, int code);
}
