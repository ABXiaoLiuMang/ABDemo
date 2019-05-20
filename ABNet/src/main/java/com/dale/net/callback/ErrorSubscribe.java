package com.dale.net.callback;

import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

/**
 * create by Dale
 * create on 2019/5/20
 * description:
 */
public class ErrorSubscribe<T> implements FlowableOnSubscribe<T> {

    private ABError error;

    ErrorSubscribe(ABError error) {
        this.error = error;
    }

    @Override
    public void subscribe(FlowableEmitter<T> emitter) throws Exception {
        emitter.onError(error);
    }
}
