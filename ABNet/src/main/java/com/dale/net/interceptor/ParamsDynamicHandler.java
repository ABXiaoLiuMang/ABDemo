package com.dale.net.interceptor;


import com.dale.net.Request;

/**
 * Desc: * 主要功能是针对参数：<br>
 * 1.可以获取到全局公共参数和局部参数，统一进行签名sign<br>
 * 2.可以自定义动态添加参数，类似时间戳timestamp是动态变化的，token（登录了才有），参数签名等<br>
 * 3.参数值是经过UTF-8编码的<br>
 * 4.默认提供询问是否动态签名（签名需要自定义），动态添加时间戳等<br>
 **/
public interface ParamsDynamicHandler {
    void intercept(Request<?> request);
}
