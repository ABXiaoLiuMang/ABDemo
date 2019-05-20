package com.dale.net;

import com.dale.net.bean.RxResponse;
import com.dale.net.callback.OnCallBack;
import com.dale.net.exception.ErrorMessage;
import com.dale.net.exception.ABNetError;

import io.reactivex.FlowableEmitter;

/**
 * Desc:Rx专用callback
 **/
class RxResponseCallback<T> implements OnCallBack<T> {
    private FlowableEmitter<RxResponse<T>> emitter;

    RxResponseCallback(FlowableEmitter<RxResponse<T>> e) {
        this.emitter = e;
    }

    @Override
    public void success(Request<T> request, T t) {
       if (!emitter.isCancelled()){
           emitter.onNext(new RxResponse<T>().setRequest(request).setResult(t));
       }
//        emitter.onComplete();
    }

    @Override
    public void error(Request<T> request, ErrorMessage error, T cache) {
        if (!emitter.isCancelled())emitter.onError(new ABNetError(request,error,cache));
    }
}
