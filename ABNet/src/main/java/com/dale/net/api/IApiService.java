package com.dale.net.api;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * API调用服务
 * version: 1.0
 */

public interface IApiService {

    @POST
    Call<ResponseBody> post(@HeaderMap Map<String, String> headers, @Url String url, @Body RequestBody request);

    @POST
    @Multipart
    Call<ResponseBody> post(@HeaderMap Map<String, String> headers, @Url String url, @PartMap Map<String, RequestBody> params);

    @GET
    Call<ResponseBody> get(@HeaderMap Map<String, String> headers, @Url String url, @QueryMap Map<String, String> params);

    @DELETE
    Call<ResponseBody> delete(@HeaderMap Map<String, String> headers, @Url String url, @QueryMap Map<String, String> params);

}
