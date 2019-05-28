package com.dale.net.manager;


import com.dale.net.cache.CacheMode;
import com.dale.net.converter.DataConverter;
import com.dale.net.exception.ABNetGlobalExceptionHandler;
import com.dale.net.interceptor.ParamsDynamicHandler;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.CookieJar;
import okhttp3.Dns;
import okhttp3.Interceptor;

/**
* description:公共参数  SSL 公共header timeout  日誌開關
**/
public interface NetConfig {
    /**
     * desc: 是否打印日志
     */
    NetConfig needLog(boolean allowLog);

    /**
     * 设置动态添加参数处理器
     */
    NetConfig setParamsDynamicHandler(ParamsDynamicHandler paramsDynamicHandler);
    /**
     * desc: 添加json转换器
     */
    NetConfig jsonConverter(DataConverter jsonConverter);


    /**
     * desc: https证书
     */
    NetConfig sslSocketFactory(SSLSocketFactory sslSocketFactory, X509TrustManager trustManager);

    /**
     * desc: 设置连接超时时间
     */
    NetConfig connectTimeout(int time);


    /**
     * desc: 设置读取数据超时时间
     */
    NetConfig readTimeout(int time);

    /**
     * desc: 设置发送数据超时时间
     */
    NetConfig writeTimeout(long time);

    /**
     * desc: 添加header
     */
    NetConfig addHeader(String name, String value);

    /**
     * desc: 添加公共参数
     */
    NetConfig params(String key, String value);


    /**
     * desc: 添加全局缓存模式
     */
    NetConfig cacheMode(CacheMode cacheMode);

    /**
     * desc: 设置全局缓存存放时间
     */
    NetConfig cacheTime(int cacheTime);

    /**
     * desc: 设置全局请求失败重试次数,默认不重试
     */
    NetConfig retryCount(int count);

    /**
     * desc: 全局默认使用base url
     */
    NetConfig baseUrl(String baseUrl);

    /**
     * desc: 异常时,全局处理
     */
    NetConfig setHttpGlobalExceptionHandler(ABNetGlobalExceptionHandler globalExceptionHandler);

    /**
     * desc: 拦截器
     */
    NetConfig addInterceptor(Interceptor interceptor);

    /**
     * desc: 设置全局cookie
     */
    NetConfig cookieJar(CookieJar cookieJar);

}
