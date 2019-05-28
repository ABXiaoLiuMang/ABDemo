package com.dale.net.request;


import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.dale.net.Request;
import com.dale.net.bean.RxResponse;
import com.dale.net.cache.CacheMode;
import com.dale.net.callback.OnBaseUrlErrorListener;
import com.dale.net.callback.OnCallBack;
import com.dale.net.converter.DataConverter;
import com.dale.net.interceptor.ParamsDynamicHandler;

import java.io.File;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.CookieJar;
import okhttp3.Dns;
import okhttp3.Interceptor;
import okhttp3.MediaType;

/**
 * Desc:
 **/
public interface NetCall<T> {

    /**
     * 往url之后继续拼接路径使用,可以重复调用,
     */
    NetCall<T> url(String url);

    /**
     * 为这个请求单独配置baseUrl.
     */
    NetCall<T> baseUrl(String baseUrl);

    /**
     * application/json 的方式发送post请求
     * @param params 需要发送的实体类
     */
    NetCall<T> params(Object params);

    /**
     * 使用key-value的方式请求网络数据
     * @param paramsMap map集合
     */
    NetCall<T> paramMap(Map<String, String> paramsMap);

    /**
     * 传输文件的参数
     * @param paramsMap key-file 的map集合
     */
    NetCall<T> paramFileMap(Map<String, File> paramsMap);

    /**
     * 使用key-value的方式请求网络数据,可以重读调用继续添加数据
     * @param key 请求的key
     * @param value 请求的value
     */
    NetCall<T> params(String key, String value);

    /**
     * 发送文件,可以重读调用继续添加数据
     * @param key key
     * @param value 发送的file
     */
    NetCall<T> params(String key, File value);

    /**
     * 为请求单独设置mediaType
     * @param mediaTypeString mediaType字符串
     */
    NetCall<T> mediaType(String mediaTypeString);

    /**
     * 为请求单独设置mediaType
     * @param mediaType mediaType
     */
    NetCall<T> mediaType(MediaType mediaType);

    /**
     * 多域名轮询请求
     * @param baseUrls 域名组
     * @param intervalTime 轮询请求的间隔
     */
    NetCall<T> baseUrls(List<String> baseUrls, long intervalTime);

    /**
     * 多域名轮询请求
     * @param baseUrls 域名组
     * @param intervalTime 轮询请求的间隔
     */
    NetCall<T> baseUrls(String[] baseUrls, long intervalTime);

    /**
     * 多域名轮询请求时,报异常的域名监听
     */
    NetCall<T> setOnBaseUrlErrorListener(OnBaseUrlErrorListener onBaseUrlErrorListener);

    /**
     * 为这个请求单独设置一个json解析器,
     * 如何实现DataConverter,
     */
    NetCall<T> jsonConverter(DataConverter jsonConverter);

    /**
     * desc: 添加header
     *
     */
    NetCall<T> addHeader(String name, String value);

    /**
     * desc: 添加header
     *
     */
    NetCall<T> headers(Map<String, String> headers);


    /**
     * desc: 设置缓存模式
     * 参数看{@link CacheMode}
     * 如果需要设置缓存的有效时间,请使用{@link #cacheTime(long)}
     * 如果需要获取缓存的大小,可以使用{ABNet#getCacheSize()}
     * 如果需要清除到缓存,可以使用{ABNet#clearCache()}
     */
    NetCall<T> cacheMode(CacheMode cacheMode);

    /**
     * desc: 设置缓存存放时间
     *
     */
    NetCall<T> cacheTime(long cacheTime);

    /**
     * desc: 设置请求失败重试次数,默认不重试
     *
     */
    NetCall<T> retryCount(int count, long retryPeriod);


    /**
     * desc: 设置连接超时时间
     */
    NetCall<T> connectTimeout(int time);


    /**
     * desc: 设置读取数据超时时间
     */
    NetCall<T> readTimeout(int time);

    /**
     * desc: 设置发送数据超时时间
     */
    NetCall<T> writeTimeout(long time);

    /**
     * @param cookieJar 具体实现请google
     */
    NetCall<T> cookieJar(CookieJar cookieJar);

    /**
     * 为此请求单独设置一个拦截器
     */
    NetCall<T> addInterceptor(Interceptor interceptor);

    /**
     * activity生命周期监听
     * @param activity
     */
    NetCall<T> lifecycle(FragmentActivity activity);

    /**
     * activity生命周期监听
     * @param fragment
     */
    NetCall<T> lifecycle(Fragment fragment);

    /**
     * 监听对话框的生命周期
     */
    NetCall<T> lifecycle(Dialog dialog);

    /**
     * 发送请求
     */
    Request<T> send(OnCallBack<T> callBack);

    /**
     * 当此请求还没有得到反馈时,是否不在继续请求
     * 默认false,设置为true 表示此请求还没得到反馈时,再次请求将被拦截
     */
    NetCall<T> isSingleEnable(boolean isSingle);

    /**
     * 返回RxJava流的方式处理
     * @return onNext 返回{@link RxResponse} onError
     */
    Flowable<RxResponse<T>> getRxResult();

    /**
     * 取消当前请求
     */
    void cancelRequest();

    /**
     * 设置自定义的缓存key
     */
    NetCall<T> setCacheKey(String cacheKey);

    /**
     * 设置动态添加参数处理器
     */
    NetCall<T> setParamsDynamicHandler(ParamsDynamicHandler paramsDynamicHandler);

    /**
     * 当activity或者fragment移除时,是否移除callback
     */
    NetCall<T> setRemoveCallbackEnble(boolean isRemove);

    /**
     * 返回的实体类是否可以为null 如果设置为true,当http code为200,responseBody为空时,会返回success(null)
     * @param isCan true 表示可以 默认是false
     */
    NetCall<T> isCanBackNullEnble(boolean isCan);

}
