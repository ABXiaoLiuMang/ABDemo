package com.dale.net;

import android.text.TextUtils;

import com.dale.net.cache.CacheMode;
import com.dale.net.cache.NetCache;
import com.dale.net.exception.ErrorMessage;
import com.dale.net.utils.JsonUtil;
import com.dale.net.utils.StringUtils;

import java.lang.reflect.Type;


class CacheControl {


    public static <T> String createCacheKey(Request<T> request) {
        return NetCache.key(
                StringUtils.buffer(request.getBaseUrl(),
                        request.getUrl(),
                        request.requestBuilder.cacheTime,
                        JsonUtil.toJson(request.requestBuilder.oparams),
                        JsonUtil.toJson(request.requestBuilder.allStringParams),
                        JsonUtil.toJson(request.requestBuilder.headers),
                        request.requestBuilder.connectTimeout,
                        request.requestBuilder.readTimeout,
                        request.requestBuilder.writeTimeout,
                        request.requestBuilder.jsonConverter.getClass().getName())
        );
    }

    /**
     * desc: 请求网络之前,判断是否使用缓存
     */
    public static <T> boolean useCacheBeforeRequest(Request<T> request){
        if (request.requestBuilder.cacheMode == CacheMode.NO_CACHE.mode()
                || request.requestBuilder.cacheMode == CacheMode.REQUEST_FAILED_READ_CACHE.mode()) {
            return false;
        }
        String sCache = NetCache.getInstance().getString(request.cacheKey);
        if (TextUtils.isEmpty(sCache)) {
            return false;
        }
        callbackCache(request,sCache);
        switch (request.requestBuilder.cacheMode){
            //先使用缓存，不管是否存在，仍然请求网络false
            case 4:
                return false;
            //如果缓存不存在才请求网络，否则使用缓存
            case 3:
                return true;
            //请求网络失败后，读取缓存
            default:
                return false;
        }
    }

    /**
     * desc: 请求网络之后,拦截处理缓存
     */
    public static <T> String interceptCacheAfterRequest(Request<T> request, int code, String response){
        if (request.requestBuilder.cacheMode == CacheMode.NO_CACHE.mode()) {
            return response;
        }
        if (code == 200){
            NetCache.getInstance().put(request.cacheKey,response,request.requestBuilder.cacheTime);
            return response;
        }
        String sCache = NetCache.getInstance().getString(request.cacheKey);
        if (TextUtils.isEmpty(sCache)) {
            return response;
        }
        switch (request.requestBuilder.cacheMode){
            //请求网络失败后，读取缓存
            case 2:
                return sCache;
            //对比缓存,不一样则返回,一样则返回null
            case 5:
                String cacheMd5 = NetCache.key(sCache);
                String resMd5 = NetCache.key(response);
                if (TextUtils.equals(resMd5,cacheMd5)){
                    return null;
                }
                return response;
            //如果缓存不存在才请求网络，否则使用缓存
            //先使用缓存，不管是否存在，仍然请求网络false
            case 3:
            case 4:
            default:
                return response;
        }
    }

    private static <T>void callbackCache(Request<T> request, String cache) {
        Type respType = request.requestBuilder.serviceMethod.requestAdapter.responseType();
        //String类型直接返回
        if (StringUtils.isStringType(respType)) {
            if (request.callback != null && !request.isCancel) {
                try {
                    request.callback.success(request,(T) cache);
                } catch (Exception e) {
                    ErrorMessage errorMessage = request.createError(ErrorMessage.RESPONSE_HANDLER_ERROR,
                            cache,
                            e);
                    errorMessage.setUrl(request.requestBuilder.baseUrl, request.requestBuilder.url);
                    request.handlerError(errorMessage, cache);
                }
            }
        } else {
            if (request.callback != null && !request.isCancel) {
                try {
                    request.callback.success(request, (T) JsonUtil.fromJson(cache, respType));
                } catch (Exception e) {
                    ErrorMessage errorMessage = request.createError(
                            ErrorMessage.RESPONSE_HANDLER_ERROR,
                            cache,
                            e
                    );
                    errorMessage.setUrl(request.requestBuilder.baseUrl, request.requestBuilder.url);
                    request.handlerError(errorMessage, cache);
                }
            }
        }
    }
}
