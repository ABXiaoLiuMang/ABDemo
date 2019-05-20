package com.dale.net;

import android.app.Application;
import android.support.annotation.Nullable;

import com.dale.net.cache.NetCache;
import com.dale.net.download.DownloadRequestBuilder;
import com.dale.net.manager.NetConfig;
import com.dale.net.manager.RetrofitManager;
import com.dale.net.utils.NetLog;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.OkHttpClient;


public class ABNet {
    private static NetConfigImpl mConfig = null;
    private static Application mContext;
    private static final Map<Method, ServiceMethod<?, ?>> serviceMethodCache = new ConcurrentHashMap<>();
    static List<String> requestsStatus = new ArrayList<>();


    /**
     * 初始化ABNet. 只需在{@link Application#onCreate()}方法中调用一次
     * 如果还想在其它位置对配置进行重新设值,请使用{@link ABNet#getConfig()}
     */
    public static NetConfig initConfig(Application application) {
        mContext = application;
        if (mConfig == null) {
            mConfig = new NetConfigImpl();
        }
        return mConfig;
    }

    /**
     * 获取当前配置,重新设值
     */
    public static NetConfigImpl getConfig() {
        //判断哪个值没有初始化的再此处设置默认值
        if (mConfig == null) {
            throw new RuntimeException("You cannot call ABNet.initConfig() in Application !");
        }
        return mConfig;
    }


    public static Application getContext() {
        return mContext;
    }


    /**
     * 动态代理获取NetCall的实现类
     */
    @SuppressWarnings("unchecked")
    public static <T> T create(final Class<T> service) {
        Util.validateServiceInterface(service);
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[]{service},
                new InvocationHandler() {

                    @Override
                    public Object invoke(Object proxy, Method method, @Nullable Object[] args)
                            throws Throwable {
                        // If the method is a method from Object then defer to normal invocation.
//                        LogUtils.e("method : %s, %s,%s,args  size : %s",
//                                method.toString(), method.toGenericString(), method.getName(),
//                                args == null ? "empty" : args.length);
                        ServiceMethod<Object, Object> serviceMethod =
                                (ServiceMethod<Object, Object>) loadServiceMethod(method);
                        RequestBuilder<Object> builder = new RequestBuilder<>(serviceMethod, args);
                        return serviceMethod.requestAdapter.adapt(builder);
                    }
                });
    }

    public static DownloadRequestBuilder download(){
        return new DownloadRequestBuilder();
    }

    private static ServiceMethod<?, ?> loadServiceMethod(Method method) {
        ServiceMethod<?, ?> result = serviceMethodCache.get(method);
        if (result != null) return result;

        synchronized (serviceMethodCache) {
            result = serviceMethodCache.get(method);
            if (result == null) {
                result = new ServiceMethod.Builder<>(method)
                        .build();
                serviceMethodCache.put(method, result);
            }
        }
        return result;
    }

    /**
     * 获取已经缓存的大小
     */
    public static long getCacheSize() {
        return NetCache.getInstance().size();
    }

    /**
     * 清空缓存
     */
    public static void clearCache() {
        try {
            NetCache.getInstance().delete();
        } catch (IOException e) {
            NetLog.e(e.toString());
        }
    }

    /**
     * 获取共用的OkHttpClient
     */
    @Deprecated
    public static OkHttpClient getClient(String baseUrl){
        return RetrofitManager.getClient(getConfig());
    }

    /**
     * 获取共用的OkHttpClient
     */
    public static OkHttpClient getClient(){
        return RetrofitManager.getClient(getConfig());
    }
}
