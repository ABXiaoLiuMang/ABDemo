package com.dale.net;


import com.dale.net.api.IApiService;
import com.dale.net.callback.OnCallBack;
import com.dale.net.exception.ErrorMessage;
import com.dale.net.manager.RetrofitManager;


class ChildRequest<T> extends Request<T> {

    ChildRequest(RequestBuilder<T> builder) {
        super(builder);
    }

    @Override
    public void send(OnCallBack<T> callBack) {
        this.callback = callBack;
        this.cacheKey = CacheControl.createCacheKey(this);

        if (!Util.isAvailable(ABNet.getContext())) {
            ErrorMessage errorMessage = createError(ErrorMessage.NET_ERROR_CODE,
                    null,
                    new Throwable(ABNet.getContext().getResources().getString(R.string.no_network)));
            errorMessage.setUrl(getBaseUrl() , getUrl());
            String cache = CacheControl.interceptCacheAfterRequest(this,404,null);
            handlerError(errorMessage, cache);
            return;
        }


        if (CacheControl.useCacheBeforeRequest(this)){
            return;
        }

        IApiService service = RetrofitManager.getRetrofit(this, ABNet.getConfig())
                .create(IApiService.class);


        switch (requestBuilder.method) {
            case Util.GET:
                call = service.get(requestBuilder.headers, getUrl(), requestBuilder.allStringParams);
                execute();
                break;
            case Util.POST:
                post(service);
                break;
            default:
                break;
        }
    }

    @Override
    protected void handlerError(ErrorMessage errorMessage, String cache) {
        if (requestScription != null){
            requestScription.cancel();
        }

        if (isCancel) {
            return;
        }

        if (callback != null) {
            try {
                callback.error(this, errorMessage, getRealResponseBody(cache));
            } catch (Exception e) {
                callback.error(this, errorMessage, null);
            }
        }
    }


}
