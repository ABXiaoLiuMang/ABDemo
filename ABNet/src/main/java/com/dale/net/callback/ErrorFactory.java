package com.dale.net.callback;

import com.dale.net.utils.JsonUtil;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;


public class ErrorFactory {
    public static <T> Flowable<T> create(ABError error) {
        return Flowable.create(new ErrorSubscribe<T>(error), BackpressureStrategy.LATEST)
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> Flowable<T> createRequestError(String msg) {
        return Flowable.create(new ErrorSubscribe<T>(getHttpError(-1, msg)), BackpressureStrategy.LATEST)
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * http请求的异常 和其它异常
     *
     * @param errCode 错误码
     * @param error   错误信息
     * @return Exception
     */
    public static ABError getHttpError(int errCode, String error) {
        return new ABHttpError(error).setCode(errCode);
    }

    /**
     * 服务器返回的异常
     *
     * @param errCode 错误码
     * @param error   错误信息
     * @return Exception
     */
    public static ABError getServiceError(int errCode, String error){
        return getServiceError(errCode,error,null);
    }
    /**
     * 服务器返回的异常
     * @param errCode 错误码
     * @param error 错误信息
     * @return Exception
     */
    public static ABError getServiceError(int errCode, String error,Object src){
        return new ABServiceError(error).setOriginMessage(JsonUtil.toJson(src)).setCode(errCode);
    }

    public static void throwRunTimeException(String message){
        throw new RuntimeException(message);
    }
}
