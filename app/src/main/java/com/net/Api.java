package com.net;

import com.dale.net.http.GET;
import com.dale.net.http.POST;
import com.dale.net.http.Url;
import com.dale.net.request.NetCall;

/**
 * create by Dale
 * create on 2019/5/20
 * description:
 */
public interface Api {

    /**
     * 指定域名和后面地址方式
     * @param url baseUrl + api/v1/authtoken
     * @return
     */
    @POST
    NetCall<BaseEntity<LoginBean>> testPostUrl(@Url String url);


    /**
     * 配置域名 +  api/v1/notice
     * @return
     */
    @POST("api/v1/authtoken")
    NetCall<BaseEntity<LoginBean>> testPost();

    /**
     * 配置域名 +  api/v1/notice
     * @return
     */
    @GET("api/v1/notice")
    NetCall<BaseEntity<TestBean>> testGet();
}
