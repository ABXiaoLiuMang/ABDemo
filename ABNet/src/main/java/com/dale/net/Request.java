package com.dale.net;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.dale.net.api.IApiService;
import com.dale.net.bean.RxResponse;
import com.dale.net.callback.OnCallBack;
import com.dale.net.converter.DataConverter;
import com.dale.net.exception.ABNetCancelException;
import com.dale.net.exception.ABNetException;
import com.dale.net.exception.ErrorMessage;
import com.dale.net.manager.RetrofitManager;
import com.dale.net.utils.JsonUtil;
import com.dale.net.utils.NetLog;
import com.dale.net.utils.StringUtils;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Desc:请求体
 **/
public class Request<T> {
    transient boolean isCancel = false;

    transient String cacheKey;
    public transient Call<ResponseBody> call;
    @SuppressWarnings("WeakerAccess")
    protected transient Subscription requestScription;
    @SuppressWarnings("WeakerAccess")
    public transient okhttp3.Response mResponse;
    @SuppressWarnings("WeakerAccess")
    public transient okhttp3.Request mRequest;
    public transient RequestBuilder<T> requestBuilder;
    public OnCallBack<T> callback;

    Request(RequestBuilder<T> builder) {
        this.cacheKey = builder.cacheKey;
        this.requestBuilder = builder;
    }


    /**
     * desc: 网络库专用,不可调用,可能会崩溃
     */
    public String getBaseUrl() {
        String baseUrl = requestBuilder.baseUrl;
        ErrorMessage errorMessage;
        if (TextUtils.isEmpty(baseUrl)) {
            errorMessage = createError(ErrorMessage.REQUEST_ERROR_CODE,
                     "baseUrl 为空",
                    new NullPointerException(requestBuilder.url + " 的baseUrl 为空"));
            handlerError(errorMessage, null);
            return "";
        }

        if (TextUtils.isEmpty(baseUrl) || !baseUrl.startsWith("http")) {
            errorMessage = createError(ErrorMessage.REQUEST_ERROR_CODE,
                     "不支持非http协议请求",
                    new IllegalArgumentException(requestBuilder.baseUrl + requestBuilder.url + "不支持非http协议请求"));
            handlerError(errorMessage, null);
        }

        return baseUrl;
    }

    ErrorMessage createError(int code, String message, Throwable error) {
        return new ErrorMessage(code, message, error).setRequest(mRequest).setResponse(mResponse);
    }

    String getUrl() {
//        if (StringUtils.isEmpty(url)) {
//            throw new NullPointerException("url is empty");
//        }
        if (requestBuilder.url == null) {
            requestBuilder.url = "";
        }
        return requestBuilder.url;
    }

    @SuppressWarnings("unused")
    public void reRequest() {
        send(requestBuilder.callback);
    }

    public void send(OnCallBack<T> callBack) {
        this.callback = callBack;
        if (TextUtils.isEmpty(cacheKey)) {
            this.cacheKey = CacheControl.createCacheKey(this);
        }
        if (TextUtils.isEmpty(getBaseUrl())) {
            return;
        }
        if (!Util.isAvailable(ABNet.getContext())) {
            String cache = CacheControl.interceptCacheAfterRequest(this, 404, null);
            ErrorMessage errorMessage = createError(ErrorMessage.NET_ERROR_CODE,
                    cache,
                    new Throwable(ABNet.getContext().getResources().getString(R.string.no_network)));
            errorMessage.setUrl(requestBuilder.baseUrl, requestBuilder.url);
            handlerError(errorMessage, cache);
            return;
        }


        if (ABNet.requestsStatus.contains(cacheKey) && requestBuilder.isSingleEnable) {
            NetLog.i(String.format("已拦截重复%s请求%s%s!", requestBuilder.method, requestBuilder.baseUrl, requestBuilder.url));
            return;
        }
        ABNet.requestsStatus.add(cacheKey);
        long startTime = System.currentTimeMillis();
        if (CacheControl.useCacheBeforeRequest(this)) {
            ABNet.requestsStatus.remove(cacheKey);
            return;
        }

        NetLog.i(String.format("判断是否使用缓存所花时间:%s", System.currentTimeMillis() - startTime));
        startTime = System.currentTimeMillis();
        Retrofit retrofit = RetrofitManager.getRetrofit(this, ABNet.getConfig());
        IApiService service = retrofit.create(IApiService.class);
        if (requestBuilder.mParamsDynamicHandler != null) {
            requestBuilder.mParamsDynamicHandler.intercept(this);
        }
        switch (requestBuilder.method) {
            case Util.GET:
                call = service.get(requestBuilder.headers, getUrl(), requestBuilder.allStringParams);
                execute();
                break;
            case Util.POST:
                post(service);
                break;
            case Util.DELETE:
                call = service.delete(requestBuilder.headers, getUrl(), requestBuilder.allStringParams);
                execute();
            default:
                break;
        }

        NetLog.i(String.format("发送请求所花时间:%s", System.currentTimeMillis() - startTime));

    }

    protected void post(IApiService service) {
        if (requestBuilder.fileMap.size() != 0) {
            executeHasFileRequest(service);
        } else {
            executeJsonRequest(service);
        }
    }

    @SuppressWarnings("WeakerAccess")
    protected void execute() {
        printLog();
        Flowable.just(this)
                .observeOn(Schedulers.computation())
                .map(mRequestHandler)
                .observeOn(AndroidSchedulers.from(Looper.getMainLooper()))
                .subscribe(reqeustResultHandler);
    }

    /**
     * 请求的处理器
     */
    private Function<Request<T>, ABResponse<T>> mRequestHandler = new Function<Request<T>, ABResponse<T>>() {
        @Override
        public ABResponse<T> apply(Request<T> tRequest) throws Exception {
            NetLog.i(String.format("请求成功 当前线程(%s) ",
                    Thread.currentThread().getName()));
            if (isCancel) {
                ABNet.requestsStatus.remove(cacheKey);
                throw ABNetCancelException.create();
            }
            Response<ResponseBody> response = call.execute();
            if (isCancel) {
                ABNet.requestsStatus.remove(cacheKey);
                throw ABNetCancelException.create();
            }
            mRequest = call.request();
            if (response != null) {
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    if (body == null) {
                        ABNet.requestsStatus.remove(cacheKey);
                        ABNetException exception = new ABNetException(requestBuilder.baseUrl + requestBuilder.url + "请求网络异常");
                        exception.setResponse(response);
                        exception.setCode(ErrorMessage.RESPONSE_BODY_EMPTY);
                        throw exception;
                    }
                    if (isCancel) {
                        ABNet.requestsStatus.remove(cacheKey);
                        throw ABNetCancelException.create();
                    }
                    String result = body.string();
                    if (isCancel) {
                        ABNet.requestsStatus.remove(cacheKey);
                        throw ABNetCancelException.create();
                    }
                    String realResult = CacheControl.interceptCacheAfterRequest(Request.this, 200, result);
                    if (realResult != null) {
                        if (isCancel) {
                            ABNet.requestsStatus.remove(cacheKey);
                            throw ABNetCancelException.create();
                        }
                        T t = getRealResponseBody(result);
                        if (requestBuilder.isCanBackNull) {
                            return new ABResponse<>(t, response);
                        } else {
                            if (t != null) {
                                return new ABResponse<>(t, response);
                            } else {
                                ABNetException exception = new ABNetException("数据无法解析");
                                exception.setResponse(response);
                                exception.setCode(ErrorMessage.DATA_PARSE_ERROR);
                                throw exception;
                            }
                        }

                    } else {
                        throw ABNetCancelException.create();
                    }
                } else {
                    throw new HttpException(response);
                }
            } else {
                ABNet.requestsStatus.remove(cacheKey);
                ABNetException exception = new ABNetException("请求网络异常");
                exception.setCode(ErrorMessage.RESPONSE_BODY_EMPTY);
                throw exception;
            }
        }
    };

    /**
     * 请求的最后结果处理器
     */
    private Subscriber<ABResponse<T>> reqeustResultHandler = new Subscriber<ABResponse<T>>() {
        @Override
        public void onSubscribe(Subscription s) {
            s.request(Long.MAX_VALUE);
            requestScription = s;
        }

        @Override
        public void onNext(ABResponse<T> abResponse) {
            T t = abResponse.t;
            mResponse = abResponse.response.raw();
            NetLog.i(String.format("t %s", t == null ? " is null" : "is not null"));
            finishRequest();
            if (isCancel) {
                return;
            }

            if (requestBuilder.isCanBackNull) {
                callbackSuccess(Request.this, t);
            } else {
                if (t != null) {
                    callbackSuccess(Request.this, t);
                }
            }

        }

        @Override
        public void onError(Throwable t) {
            finishRequest();

            if (t instanceof ABNetCancelException) {
                return;
            }

            if (isCancel) {
                return;
            }
            int code = ErrorMessage.REQUEST_ERROR_CODE;
            String cache = CacheControl.interceptCacheAfterRequest(Request.this, code, null);

            ErrorMessage errorMessage = createError(code, t.getMessage(), t);
            if (call != null) {
                mRequest = call.request();
                String requestUrl = mRequest.url().toString();
                if (!TextUtils.isEmpty(requestUrl)) {
                    String host = Util.getHostByUrl(requestUrl);
                    errorMessage.setUrl(host, requestUrl.replace(host, ""));
                } else {
                    errorMessage.setUrl(requestBuilder.baseUrl, requestBuilder.url);
                }
            }

            if (t instanceof HttpException) {
                HttpException httpException = (HttpException) t;
                mResponse = httpException.response().raw();
                errorMessage.setCode(httpException.code());
            } else if (t instanceof ABNetException) {
                ABNetException ABNetException = (ABNetException) t;
                Response response = ABNetException.getResponse();
                if (response != null) {
                    mResponse = response.raw();
                    errorMessage.setCode(ABNetException.getCode());
                }
            } else if (t instanceof IOException) {
                errorMessage.setCode(ErrorMessage.REQUEST_ERROR_CODE);
            } else if (t instanceof RuntimeException) {
                errorMessage.setCode(ErrorMessage.RESPONSE_HANDLER_ERROR);
            }

            errorMessage.setRequest(mRequest).setResponse(mResponse);
            handlerError(errorMessage, cache);
        }

        @Override
        public void onComplete() {

        }
    };

    private void finishRequest() {
        ABNet.requestsStatus.remove(cacheKey);
    }

    private void printLog() {
        if (ABNet.getConfig().isNeedLog()) {
            NetLog.i(String.format("****************准备 发送%s请求  %s%s**************************\n请求参数: %s",
                    requestBuilder.method, requestBuilder.baseUrl, requestBuilder.url, requestBuilder.oparams == null ? toStringParamsMap() : JsonUtil.toJson(requestBuilder.oparams)));
        }
    }

    private String toStringParamsMap() {
        StringBuffer stringBuffer = new StringBuffer(String.format("params :%s", requestBuilder.allStringParams.toString()));
        stringBuffer.append("file params : [");
        Set<Map.Entry<String, File>> entities = requestBuilder.fileMap.entrySet();
        for (Map.Entry<String, File> entity : entities) {
            String key = entity.getKey();
            File value = entity.getValue();
            stringBuffer.append("{");
            stringBuffer.append("key =");
            stringBuffer.append(key);
            stringBuffer.append(",file path =");
            stringBuffer.append(value.getPath());
            stringBuffer.append("}");
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }

    protected void handlerError(ErrorMessage errorMessage, String cache) {
        if (requestScription != null) {
            requestScription.cancel();
        }
        if (isCancel) {
            return;
        }

        if (callback != null && !isCancel) {
            try {
                callback.error(this, errorMessage, getRealResponseBody(cache));
            } catch (Exception e) {
                try {
                    callback.error(this, errorMessage, null);
                } catch (Exception e1) {
                    NetLog.e(e1.toString());
                }
            }
        }

    }


    protected void callbackSuccess(Request<T> request, T t) {
        //String类型直接返回
        if (callback != null && !isCancel) {
            try {
                callback.success(request, t);
            } catch (Exception e) {
                String cache = CacheControl.interceptCacheAfterRequest(this, 404, null);
                ErrorMessage errorMessage = createError(ErrorMessage.RESPONSE_HANDLER_ERROR,
                        e.getMessage(),
                        e);
                errorMessage.setUrl(requestBuilder.baseUrl, requestBuilder.url);
                handlerError(errorMessage, cache);
            }
        }
    }

    @SuppressWarnings("WeakerAccess")
    protected T getRealResponseBody(String result) throws Exception {
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        Type respType = requestBuilder.serviceMethod.requestAdapter.responseType();
        if (StringUtils.isStringType(respType)) {
            return (T) result;
        } else {
            return getJsonConverter().converter(result, respType);
        }
    }

    /**
     * desc: 包含文件的请求
     *
     */
    private void executeHasFileRequest(IApiService service) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        Set<Map.Entry<String, File>> fileEntities = requestBuilder.fileMap.entrySet();

        for (Map.Entry<String, File> entity : fileEntities) {
            String key = entity.getKey();
            File file = entity.getValue();
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("application/octet-stream"), file);
            builder.addFormDataPart(key, file.getName(), requestFile);
        }

        if (requestBuilder.allStringParams != null && requestBuilder.allStringParams.size() != 0) {
            Set<Map.Entry<String, String>> allStringParamsEntities = requestBuilder.allStringParams.entrySet();

            for (Map.Entry<String, String> entity : allStringParamsEntities) {
                builder.addFormDataPart(entity.getKey(), entity.getValue());
            }
        }
        RequestBody requestBodies = builder.build();
        requestBuilder.headers.remove("Content-Type");
        call = service.post(requestBuilder.headers, getUrl(), requestBodies);
        execute();
    }


    /**
     * desc: 使用json的方式请求
     *
     */
    private void executeJsonRequest(IApiService service) {
        RequestBody request;
        //根据传的参数,判断使用哪种mediaType请求
        if (requestBuilder.oparams != null) {
            request = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JsonUtil.toJson(requestBuilder.oparams));
            requestBuilder.headers.put("Content-Type", "application/json");
        } else {
            request = RequestBody.create(requestBuilder.mMediaType, createParams());
            requestBuilder.headers.put("Content-Type", requestBuilder.mMediaType.toString());
        }

        call = service.post(requestBuilder.headers, requestBuilder.url, request);
        execute();
    }

    /**
     * 把map转化成请求参数
     */
    private String createParams() {
        if (requestBuilder.allStringParams == null || requestBuilder.allStringParams.size() == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        Set<Map.Entry<String, String>> allStringParamsEntities = requestBuilder.allStringParams.entrySet();

        for (Map.Entry<String, String> entity : allStringParamsEntities) {
            String key = entity.getKey();
            builder.append(key).append("=").append(entity.getValue()).append("&");
        }
        String params = builder.toString();
        return params.substring(0, params.length() - 1);
    }

    @SuppressWarnings("WeakerAccess")
    public void onCancelProgress() {
        try {
            ABNet.requestsStatus.remove(cacheKey);
            isCancel = true;
        } catch (Exception e) {
            NetLog.e(e.toString());
        }

    }

    protected void closeOthersRequest() {
    }

    /**
     * desc: activity或者fragment销毁时调用的方法
     *
     */
    void destroy() {
        callback = null;
    }

    /**
     * 返回RxJava流的方式处理
     *
     * @return onNext 返回{@link RxResponse} onError
     */
    @SuppressWarnings("WeakerAccess")
    protected Flowable<RxResponse<T>> getRxResult() {
        return Flowable.create(new FlowableOnSubscribe<RxResponse<T>>() {
            @Override
            public void subscribe(FlowableEmitter<RxResponse<T>> e) throws Exception {
                send(new RxResponseCallback<>(e));
            }
        }, BackpressureStrategy.LATEST)
                .subscribeOn(AndroidSchedulers.from(Looper.getMainLooper()));
    }

    @SuppressWarnings("WeakerAccess")
    protected DataConverter getJsonConverter() {
        if (requestBuilder.jsonConverter != null) {
            return requestBuilder.jsonConverter;
        }
        requestBuilder.jsonConverter = ABNet.getConfig().getJsonConverter();
        return requestBuilder.jsonConverter;
    }

}
