package com.net;

import android.text.TextUtils;

import com.dale.net.ABNet;
import com.dale.net.bean.RxResponse;
import com.dale.net.callback.ErrorFactory;
import com.dale.net.request.NetCall;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.dale.net.callback.ABStatusCode.HttpStatus.REQUEST_PARAMS_ERROR;

/**
 * create by Dale
 * create on 2019/5/20
 * description:
 */
public class ApiSource {

    public static NetCall<BaseEntity<LoginBean>> testPostUrl() {
        String path = ABNet.getConfig().getBaseUrl() + "api/v1/authtoken";
        return ABNet.create(Api.class)
                .testPostUrl(path)
                .isSingleEnable(true);
    }

    public static Flowable<RxResponse<BaseEntity<LoginBean>>> testPost(String userName, String passWord) {
        if(TextUtils.isEmpty(userName)){
            return ErrorFactory.create(ErrorFactory.getHttpError(REQUEST_PARAMS_ERROR, " "));
        }
        return ABNet.create(Api.class)
                .testPost()
                .params("username",userName)
                .params("password",passWord)
                .isSingleEnable(true)
                .getRxResult()
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static NetCall<BaseEntity<TestBean>> testGet() {
        return ABNet.create(Api.class)
                .testGet()
                .isSingleEnable(true);
    }


}
