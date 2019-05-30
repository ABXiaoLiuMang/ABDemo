package com.dale.net;


import com.dale.net.cache.CacheMode;
import com.dale.net.converter.DataConverter;
import com.dale.net.converter.GsonJsonConverter;
import com.dale.net.interceptor.ParamsDynamicHandler;
import com.dale.net.manager.NetConfig;
import com.dale.net.utils.NetLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.CookieJar;
import okhttp3.Interceptor;


public class NetConfigImpl implements NetConfig {
    private boolean needLog = false;
    private DataConverter jsonConverter;
    private SSLSocketFactory sslSocketFactory;
    private X509TrustManager trustManager;
    private int connectTimeout = 10000;
    private int readTimeout = 10000;
    private int writeTimeout = 10000;
    private Map<String, String> paramsMap = new HashMap<>();
    private Map<String, String> headers = new HashMap<>();
    private int cacheMode = CacheMode.NO_CACHE.mode();
    /**
     * 缓存时间 0或者负数表示永久
     */
    private int cacheTime = 0;
    private int retryCount = 0;
    private String baseUrl;
    private List<Interceptor> interceptors = new ArrayList<>();
    private CookieJar cookieJar;
    private ParamsDynamicHandler mParamsDynamicHandler;

    NetConfigImpl() {
    }

    @Override
    public NetConfig needLog(boolean allowLog) {
        this.needLog = allowLog;
        NetLog.allowD = allowLog;
        return this;
    }

    @Override
    public NetConfig setParamsDynamicHandler(ParamsDynamicHandler paramsDynamicHandler) {
        mParamsDynamicHandler = paramsDynamicHandler;
        return this;
    }

    @Override
    public NetConfig jsonConverter(DataConverter jsonConverter) {
        this.jsonConverter = jsonConverter;
        return this;
    }


    @Override
    public NetConfig sslSocketFactory(SSLSocketFactory sslSocketFactory, X509TrustManager trustManager) {
        this.sslSocketFactory = sslSocketFactory;
        this.trustManager = trustManager;
        return this;
    }

    @Override
    public NetConfig connectTimeout(int time) {
        connectTimeout = Util.checkTimeOut("connect time out",time);
        return this;
    }

    @Override
    public NetConfig readTimeout(int time) {
        readTimeout = Util.checkTimeOut("read time out",time);
        return this;
    }

    @Override
    public NetConfig writeTimeout(long time) {
        writeTimeout = Util.checkTimeOut("write time out",time);
        return this;
    }

    @Override
    public NetConfig addHeader(String name, String value) {
        this.headers.put(name,value);
        return this;
    }

    @Override
    public NetConfig params(String key, String value) {

        this.paramsMap.put(key,value);
        return this;
    }

    @Override
    public NetConfig cacheMode(CacheMode cacheMode) {
        this.cacheMode = cacheMode.mode();
        return this;
    }

    @Override
    public NetConfig cacheTime(int cacheTime) {
        this.cacheTime = cacheTime;
        return this;
    }

    @Override
    public NetConfig retryCount(int count) {
        this.retryCount = count;
        return this;
    }

    @Override
    public NetConfig baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }


    @Override
    public NetConfig addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
        return this;
    }

    @Override
    public NetConfig cookieJar(CookieJar cookieJar) {
        if (cookieJar == null) throw new NullPointerException("cookieJar is null");
        this.cookieJar = cookieJar;
        return this;
    }

    public CookieJar getCookieJar() {
        return cookieJar;
    }

    public List<Interceptor> getInterceptors() {
        return interceptors;
    }

    public boolean isNeedLog() {
        return needLog;
    }

    public DataConverter getJsonConverter() {
        if (jsonConverter == null){
            //此处需要赋值一个默认的json转换器
            jsonConverter = new GsonJsonConverter();
        }
        return jsonConverter;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return sslSocketFactory;
    }

    public X509TrustManager getTrustManager() {
        return trustManager;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }

    public Map<String, String> getParamsMap() {
        return paramsMap;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public int getCacheMode() {
        return cacheMode;
    }

    public int getCacheTime() {
        return cacheTime;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public ParamsDynamicHandler getParamsDynamicHandler() {
        return mParamsDynamicHandler;
    }


    @Override
    public String toString() {
        return "NetConfigImpl{" +
                "needLog=" + needLog +
                ", jsonConverter=" + jsonConverter +
                ", sslSocketFactory=" + sslSocketFactory +
                ", trustManager=" + trustManager +
                ", connectTimeout=" + connectTimeout +
                ", readTimeout=" + readTimeout +
                ", writeTimeout=" + writeTimeout +
                ", paramsMap=" + paramsMap +
                ", headers=" + headers +
                ", cacheMode=" + cacheMode +
                ", cacheTime=" + cacheTime +
                ", retryCount=" + retryCount +
                ", baseUrl='" + baseUrl + '\'' +
                ", interceptors=" + interceptors +
                ", cookieJar=" + cookieJar +
                ", mParamsDynamicHandler=" + mParamsDynamicHandler +
                '}';
    }
}
