package com.dale.net.manager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;


class HttpBean {
    Retrofit retrofit;
    OkHttpClient client;

     HttpBean(Retrofit retrofit, OkHttpClient client) {
        this.retrofit = retrofit;
        this.client = client;
    }
}
