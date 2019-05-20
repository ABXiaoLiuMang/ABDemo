package com.net;

import com.dale.net.Request;
import com.dale.net.bean.RxResponse;
import com.dale.net.callback.ABError;
import com.dale.net.callback.BaseCallBack;
import com.dale.net.callback.ErrorFactory;
import com.dale.net.callback.HttpStatusUtils;
import com.dale.net.exception.ABNetError;
import com.dale.net.exception.ErrorMessage;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import okhttp3.Response;


public abstract class OnCommonCallback<T> extends BaseCallBack implements Subscriber<RxResponse<BaseEntity<T>>> {

    private okhttp3.Request mOkRequest;
    private Response mOkResponse;
    public OnCommonCallback() {
        super();
    }

    @Override
    public void onSubscribe(Subscription s) {
        s.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(RxResponse<BaseEntity<T>> baseEntityRxResponse) {
        try{
            //返回结果为空，提示默认信息
            BaseEntity<T> tBaseEntity = baseEntityRxResponse.result();
            if (tBaseEntity == null) {
                responseNull();
                return;
            }
            Request<BaseEntity<T>> request = baseEntityRxResponse.request();
            //请求成功
            if (tBaseEntity.isOk()) {
                onSuccess(tBaseEntity.getData());
                onSuccess(tBaseEntity, tBaseEntity.getData());
            } else {
                //根据错误码返回提示信息
                String msg = HttpStatusUtils.getErrorMessage(tBaseEntity.getStatus_code(), tBaseEntity.getMessage());
                fails(ErrorFactory.getServiceError(tBaseEntity.getStatusCodeInt(),msg)
                        .setRequest(request.mRequest)
                        .setResponse(request.mResponse));
            }

        }catch (Exception e){
            codeException(e);
        }
    }

    @Override
    public void onError(Throwable t) {

        if (t instanceof ABNetError){
            ABNetError error = (ABNetError) t;
            ErrorMessage errorMessage = error.getError();
            handleErrorResult(errorMessage);
        }else if (t instanceof ABError){
            fails((ABError) t);
        }
    }

    @Override
    public void onComplete() { }

    public abstract void onSuccess(T t) throws Exception;

    public void onSuccess(BaseEntity baseEntity, T t){

    }
}
