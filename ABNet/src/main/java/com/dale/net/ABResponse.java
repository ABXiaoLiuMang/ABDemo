package com.dale.net;


import retrofit2.Response;


class ABResponse<T> {
     T t;
    Response<?> response;

    ABResponse(T t, Response<?> response) {
        this.t = t;
        this.response = response;
    }
}
