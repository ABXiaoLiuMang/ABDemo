package com.dale.net.manager;

import com.dale.net.NetConfigImpl;
import com.dale.net.Request;
import com.dale.net.RequestBuilder;
import com.dale.net.cache.NetCache;
import com.dale.net.interceptor.ABLoggingInterceptor;
import com.dale.net.utils.NetLog;
import com.dale.net.utils.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.CookieJar;
import okhttp3.Dns;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;


public class RetrofitManager {

    private static Map<String, HttpBean> retrofitMap = new ConcurrentHashMap<>();
    private static Map<String, OkHttpClient> sClientMap = new ConcurrentHashMap<>();

    public static OkHttpClient getClient(NetConfigImpl config){
        String clientKey = getClientKey(config);
        OkHttpClient client = sClientMap.get(clientKey);
        if (client != null) {
            return client;
        }
        client = createHttpClient(config,config.getCookieJar(),config.getInterceptors(),
                config.getReadTimeout(),config.getConnectTimeout(),config.getWriteTimeout());
        sClientMap.put(clientKey,client);
        return client;
    }

    private static OkHttpClient getClient(Request<?> request, NetConfigImpl config){
        String clientKey = getClientKey(config);
        OkHttpClient client = sClientMap.get(clientKey);
        if (client != null) {
            NetLog.i(request.getBaseUrl() + request.requestBuilder.url + "使用缓存中的client");
            return client;
        }

        client = createHttpClient(config,request.requestBuilder.cookieJar,request.requestBuilder.interceptors,
                request.requestBuilder.readTimeout,request.requestBuilder.connectTimeout,request.requestBuilder.writeTimeout);
        sClientMap.put(clientKey,client);
        NetLog.i(request.getBaseUrl() + request.requestBuilder.url +"使用新创建的client");
        return client;
    }

    public static Retrofit getRetrofit(Request<?> request, NetConfigImpl config) {
        String clientKey = getClientKey(config);
        HttpBean httpBean = retrofitMap.get(request.getBaseUrl());

        if (httpBean != null) {
            NetLog.i(request.getBaseUrl() + request.requestBuilder.url +"使用缓存中的retrofit");
            return httpBean.retrofit;
        }

        OkHttpClient client = getClient(request,config);
        httpBean = createHttpBean(client,request.getBaseUrl());
        retrofitMap.put(request.getBaseUrl(),httpBean);
        sClientMap.put(clientKey,client);
        NetLog.i(request.getBaseUrl() + request.requestBuilder.url + "使用新创建的retrofit");
        return httpBean.retrofit;
    }

    private static OkHttpClient createHttpClient(NetConfigImpl config, CookieJar cookieJar, List<Interceptor> interceptors,
                                                 int readTimeout, int connectTimeout, int writeTimeout){
        OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        if (config.isNeedLog()){
            httpBuilder.addInterceptor(new ABLoggingInterceptor());
        }

        if (config.getSslSocketFactory() != null && config.getTrustManager() != null){
            httpBuilder.sslSocketFactory(config.getSslSocketFactory(),config.getTrustManager());
        }

        if (cookieJar != null){
            httpBuilder.cookieJar(cookieJar);
        }

        for (Interceptor interceptor : interceptors) {
            httpBuilder.addInterceptor(interceptor);
        }

        return httpBuilder
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                //设置超时
                .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS)
                .build();
    }

    private static HttpBean createHttpBean(OkHttpClient client, String baseUrl){
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
        Retrofit retrofit = retrofitBuilder
                //设置网络请求的Url地址
                .baseUrl(baseUrl)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return new HttpBean(retrofit,client);
    }

    private static String getClientKey(NetConfigImpl config){
        return NetCache.key(config.toString());
    }
}
