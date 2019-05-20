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
                -1,-1,-1,
                config.getReadTimeout(),config.getConnectTimeout(),config.getWriteTimeout(),config.getDns());
        sClientMap.put(clientKey,client);
        return client;
    }

    private static OkHttpClient getClient(Request<?> request, NetConfigImpl config){
        String clientKey = getClientKey(config,request);
        OkHttpClient client = sClientMap.get(clientKey);
        if (client != null) {
            NetLog.i(request.getBaseUrl() + request.requestBuilder.url + "使用缓存中的client");
            return client;
        }
        //create new retrofit object and put retrofit to retrofitMap
        client = createHttpClient(config,request.requestBuilder.cookieJar,request.requestBuilder.interceptors,
                request.requestBuilder.followRedirects,request.requestBuilder.followSslRedirects,request.requestBuilder.retryOnConnectionFailure,
                request.requestBuilder.readTimeout,request.requestBuilder.connectTimeout,request.requestBuilder.writeTimeout
                ,request.requestBuilder.dns);
        sClientMap.put(clientKey,client);
        NetLog.i(request.getBaseUrl() + request.requestBuilder.url +"使用新创建的client");
        return client;
    }

    public static Retrofit getRetrofit(Request<?> request, NetConfigImpl config) {
        String clientKey = getClientKey(config,request);
        HttpBean httpBean = retrofitMap.get(request.getBaseUrl());

        if (httpBean != null) {
            NetLog.i(request.getBaseUrl() + request.requestBuilder.url +"使用缓存中的retrofit");
            return httpBean.retrofit;
        }
        //create new retrofit object and put retrofit to retrofitMap
        OkHttpClient client = getClient(request,config);
        httpBean = createHttpBean(client,request.getBaseUrl());
        retrofitMap.put(request.getBaseUrl(),httpBean);
        sClientMap.put(clientKey,client);
        NetLog.i(request.getBaseUrl() + request.requestBuilder.url + "使用新创建的retrofit");
        return httpBean.retrofit;
    }

    private static OkHttpClient createHttpClient(NetConfigImpl config, CookieJar cookieJar, List<Interceptor> interceptors,
                                                 int followRedirects, int followSslRedirects, int retryOnConnectionFailure,
                                                 int readTimeout, int connectTimeout, int writeTimeout, Dns dns){
        OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        if (config.isNeedLog()){
            httpBuilder.addInterceptor(getLogInterceptor(config));
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

        if (followRedirects >= 0){
            httpBuilder.followRedirects(followRedirects == 0);
        }

        if (followSslRedirects >= 0){
            httpBuilder.followSslRedirects(followSslRedirects == 0);
        }

        if (retryOnConnectionFailure >= 0){
            httpBuilder.retryOnConnectionFailure(retryOnConnectionFailure == 0);
        }

        if (dns != null){
           httpBuilder.dns(dns);
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
        try {

            SSLSocketFactory sslFactory = config.getSslSocketFactory();
            X509TrustManager trustManager = config.getTrustManager();

            String bufferKey = StringUtils.buffer(
                    config.getConnectTimeout(),
                    config.getReadTimeout(),
                    config.getWriteTimeout(),
                    config.getCookieJar() != null ? config.getCookieJar().getClass().getName() : "",
                    -1,
                    -1,
                    -1,
                    config.getDns() != null ? config.getDns().getClass().getName() : "",
                    sslFactory != null ? sslFactory.getClass().getName(): "",
                    trustManager != null ? trustManager.getClass().getName() : ""
            );

            if (config.getInterceptors() != null){
                for (Interceptor interceptor : config.getInterceptors()) {
                    bufferKey = StringUtils.buffer(bufferKey,interceptor.getClass().getName());
                }
            }

            return NetCache.key(bufferKey);
        }catch (Exception e){
            NetLog.e(e.toString());
        }
        return NetCache.key("default_client");
    }

    private static String getClientKey(NetConfigImpl config, Request<?> request){
        try {
            RequestBuilder<?> builder = request.requestBuilder;
            SSLSocketFactory sslFactory = config.getSslSocketFactory();
            X509TrustManager trustManager = config.getTrustManager();

            String bufferKey = StringUtils.buffer(
                    builder.connectTimeout,
                    builder.readTimeout,
                    builder.writeTimeout,
                    builder.cookieJar != null ? builder.cookieJar.getClass().getName() : "",
                    builder.followRedirects,
                    builder.followSslRedirects,
                    builder.retryOnConnectionFailure,
                    builder.dns != null ? builder.dns.getClass().getName() : "",
                    sslFactory != null ? sslFactory.getClass().getName(): "",
                    trustManager != null ? trustManager.getClass().getName() : "");


            if (builder.interceptors != null){
                for (Interceptor interceptor : builder.interceptors) {
                    bufferKey = StringUtils.buffer(bufferKey,interceptor.getClass().getName());
                }
            }



            return NetCache.key(bufferKey);
        }catch (Exception e){
            NetLog.e(e.toString());
        }
        return NetCache.key("default_client");
    }

    private static Interceptor getLogInterceptor(NetConfigImpl config) {
        Interceptor interceptor = config.getLogInterceptor();
        if (interceptor == null){
            //此处缺少默认日志拦截器
            interceptor = new ABLoggingInterceptor();
        }
        return interceptor;
    }
}
