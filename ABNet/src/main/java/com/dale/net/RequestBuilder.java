package com.dale.net;

import android.app.Dialog;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.dale.net.bean.RxResponse;
import com.dale.net.cache.CacheMode;
import com.dale.net.callback.OnCallBack;
import com.dale.net.converter.DataConverter;
import com.dale.net.interceptor.ParamsDynamicHandler;
import com.dale.net.request.NetCall;
import com.dale.net.utils.NetLog;
import com.lifecycle.Action;
import com.lifecycle.Life;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.MediaType;

public class RequestBuilder<T> implements NetCall<T>, LifecycleObserver {
    final ServiceMethod<T, ?> serviceMethod;
    final Object[] args;
    public String method;
    public String url;
    public String baseUrl;
    public Object oparams;
    public Map<String, File> fileMap = new HashMap<>();
    public Map<String, String> headers = new HashMap<>();
    public Map<String, String> allStringParams = new HashMap<>();
    public List<String> baseUrls;
    int cacheMode;
    long cacheTime;
    int retryCount;
    protected OnCallBack<T> callback;
    public int connectTimeout;
    public int readTimeout;
    public int writeTimeout;
    DataConverter jsonConverter;
    public long intervalTime;
    Request<T> request;
    public CookieJar cookieJar;
    public List<Interceptor> interceptors = new ArrayList<>();

    long retryPeriod = 0;

    /**
     * 是否过滤重复请求
     */
    boolean isSingleEnable = false;
    MediaType mMediaType;
    Lifecycle lifeCycle;
    public String cacheKey;
    ParamsDynamicHandler mParamsDynamicHandler;
    boolean isRemoveCallback = true;
    boolean isCanBackNull = false;
    RequestBuilder(ServiceMethod<T, ?> serviceMethod, Object[] args) {
        this.serviceMethod = serviceMethod;
        this.args = args;
        this.url = serviceMethod.relativeUrl;
        this.method = serviceMethod.httpMethod;
        this.interceptors.addAll(ABNet.getConfig().getInterceptors());
        this.cacheMode = ABNet.getConfig().getCacheMode();
        this.cacheTime = ABNet.getConfig().getCacheTime();
        this.cookieJar = ABNet.getConfig().getCookieJar();
        this.jsonConverter = ABNet.getConfig().getJsonConverter();
        this.cacheMode = ABNet.getConfig().getCacheMode();
        mParamsDynamicHandler = ABNet.getConfig().getParamsDynamicHandler();
        connectTimeout = ABNet.getConfig().getConnectTimeout();
        readTimeout = ABNet.getConfig().getReadTimeout();
        writeTimeout = ABNet.getConfig().getWriteTimeout();
        baseUrl = ABNet.getConfig().getBaseUrl();

        Map<String, String> commonParams = ABNet.getConfig().getParamsMap();
        if (commonParams != null && commonParams.size() != 0) {
            allStringParams.putAll(commonParams);
        }

        Map<String, String> commonHeaders = ABNet.getConfig().getHeaders();
        if (commonHeaders != null && commonHeaders.size() != 0) {
            headers.putAll(commonHeaders);
        }

        mMediaType = MediaType.parse("application/x-www-form-urlencoded;charset=utf-8");

        initParameter(serviceMethod.mParameterHandlers, args);
    }



    @SuppressWarnings("unchecked")
    private void initParameter(AbstractParameterHandler<?>[] mParameterHandlers, Object[] args) {
        AbstractParameterHandler<Object>[] handlers = (AbstractParameterHandler<Object>[]) mParameterHandlers;
        int argumentCount = args != null ? args.length : 0;
        if (argumentCount != handlers.length) {
            throw new IllegalArgumentException("Argument count (" + argumentCount
                    + ") doesn't match expected count (" + handlers.length + ")");
        }

        try {
            for (int p = 0; p < argumentCount; p++) {
                handlers[p].apply(this, args[p]);
            }
        } catch (IOException e) {
             NetLog.e(e.toString());
        }

    }

    @Override
    public NetCall<T> url(String url) {
        if (!TextUtils.isEmpty(url)) {
            if (!TextUtils.isEmpty(this.url)) {
                this.url += url;
            } else {
                this.url = url;
            }
        }
        return this;
    }

    @Override
    public NetCall<T> baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    @Override
    public NetCall<T> params(Object params) {
        this.oparams = params;
        return this;
    }

    @Override
    public NetCall<T> paramMap(Map<String, String> paramsMaps) {
        if (paramsMaps != null && paramsMaps.size() > 0) {
            this.allStringParams.putAll(paramsMaps);
        }
        return this;
    }

    @Override
    public NetCall<T> paramFileMap(Map<String, File> paramsMap) {
        if (paramsMap != null && paramsMap.size() > 0) {
            this.fileMap.putAll(paramsMap);
        }

        return this;
    }

    @Override
    public NetCall<T> params(String key, String value) {
        allStringParams.put(key, value);
        return this;
    }

    @Override
    public NetCall<T> params(String key, File value) {
        fileMap.put(key, value);
        return this;
    }

    @Override
    public NetCall<T> setParamsDynamicHandler(ParamsDynamicHandler paramsDynamicHandler) {
        mParamsDynamicHandler = paramsDynamicHandler;
        return this;
    }

    @Override
    public NetCall<T> setRemoveCallbackEnble(boolean isRemove) {
        this.isRemoveCallback = isRemove;
        return this;
    }

    @Override
    public NetCall<T> isCanBackNullEnble(boolean isCan) {
        isCanBackNull = isCan;
        return this;
    }

    @Override
    public NetCall<T> mediaType(String mediaTypeString) {
        return mediaType(MediaType.parse(mediaTypeString));
    }

    @Override
    public NetCall<T> mediaType(MediaType mediaType) {
        this.mMediaType = mediaType;
        return this;
    }

    @Override
    public NetCall<T> baseUrls(List<String> baseUrls, long intervalTime) {
        this.baseUrls = baseUrls;
        this.intervalTime = intervalTime;
        return this;
    }

    @Override
    public NetCall<T> baseUrls(String[] baseUrls, long intervalTime) {
        this.baseUrls = Arrays.asList(baseUrls);
        this.intervalTime = intervalTime;
        return this;
    }

    @Override
    public RequestBuilder<T> isSingleEnable(boolean isSingleEnable) {
        this.isSingleEnable = isSingleEnable;
        return this;
    }

    @Override
    public RequestBuilder<T> jsonConverter(DataConverter jsonConverter) {
        this.jsonConverter = jsonConverter;
        return this;
    }

    /**
     * desc: 添加header
     *
     */
    @Override
    public RequestBuilder<T> addHeader(String name, String value) {
        headers.put(name, value);
        return this;
    }

    /**
     * desc: 添加header
     *
     */
    @Override
    public RequestBuilder<T> headers(Map<String, String> headers) {
        this.headers.putAll(headers);
        return this;
    }


    /**
     * desc: 设置缓存模式
     *
     */
    @Override
    public RequestBuilder<T> cacheMode(CacheMode cacheMode) {
        this.cacheMode = cacheMode.mode();
        return this;
    }

    /**
     * desc: 设置缓存存放时间
     *
     */
    @Override
    public RequestBuilder<T> cacheTime(long cacheTime) {
        this.cacheTime = Util.checkTimeOut("cache time ", cacheTime);
        return this;
    }

    /**
     * desc: 设置请求失败重试次数,默认不重试
     *
     */
    @Override
    public RequestBuilder<T> retryCount(int count, long retryPeriod) {
        this.retryCount = count;
        this.retryPeriod = retryPeriod;
        return this;
    }

    /**
     * desc: 设置连接超时时间
     *
     */
    @Override
    public RequestBuilder<T> connectTimeout(int time) {
        connectTimeout = Util.checkTimeOut("connect time out", time);
        return this;
    }


    /**
     * desc: 设置读取数据超时时间
     *
     */
    @Override
    public RequestBuilder<T> readTimeout(int time) {
        readTimeout = Util.checkTimeOut("read time out", time);
        return this;
    }

    /**
     * desc: 设置发送数据超时时间
     *
     */
    @Override
    public RequestBuilder<T> writeTimeout(long time) {
        writeTimeout = Util.checkTimeOut("write time out", time);
        return this;
    }

    @Override
    public RequestBuilder<T> cookieJar(CookieJar cookieJar) {
        if (cookieJar == null) {
            throw new NullPointerException("cookieJar == null");
        }
        this.cookieJar = cookieJar;
        return this;
    }

    @Override
    public RequestBuilder<T> addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
        return this;
    }

    private void registerLifecycle(Context context) {
        if (context instanceof FragmentActivity) {
            ((FragmentActivity) context).getLifecycle().addObserver(this);
        }
    }

    @Override
    public NetCall<T> lifecycle(FragmentActivity activity) {
        if (activity == null) {
            return this;
        }

        lifeCycle = activity.getLifecycle();
        lifeCycle.addObserver(this);
        return this;
    }

    @Override
    public NetCall<T> lifecycle(Fragment fragment) {
        if (fragment == null) {
            return this;
        }
        if (lifeCycle != null) {
            lifeCycle.removeObserver(this);
            lifeCycle = null;
        }
        lifeCycle = fragment.getLifecycle();
        lifeCycle.addObserver(this);
        return this;
    }

    @Override
    public RequestBuilder<T> lifecycle(Dialog dialog) {
        Life.beginTransaction(dialog)
                .setDestroy(new Action() {
                    @Override
                    public void accept() {
                        cancelRequest();
                    }
                });
        return this;
    }

    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void cancelRequest() {
        if (request != null) {
            request.onCancelProgress();
            request.closeOthersRequest();
            request.destroy();
        }
    }

    @Override
    public RequestBuilder<T> setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
        return this;
    }

    @Override
    public Flowable<RxResponse<T>> getRxResult() {
        if (baseUrls != null) {
            request = new MultiBaseUrlRequest<>(this);
        } else {
            request = new Request<>(this);
        }
        return request.getRxResult();
    }

    @Override
    public Request<T> send(OnCallBack<T> callBack) {
        this.callback = callBack;
        //判断是否多域名,进行多域名访问策略
        if (baseUrls != null) {
            request = new MultiBaseUrlRequest<>(this);
        } else {
            request = new Request<>(this);
        }
        request.send(callBack);
        return request;
    }
}
