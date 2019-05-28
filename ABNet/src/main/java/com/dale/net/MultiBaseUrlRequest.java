package com.dale.net;

import com.dale.net.callback.OnCallBack;
import com.dale.net.exception.ErrorMessage;
import com.dale.net.request.NetCall;
import com.dale.net.utils.NetLog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class MultiBaseUrlRequest<T> extends Request<T> {

    private transient List<Request> childResquestList = new ArrayList<>();
    private transient int errorCount = 0;
    private transient boolean isFinish = false;
    private transient ScheduledExecutorService timerExecutor;
    private transient ChildRequestTask timerTask = null;
    private transient int indexBaseUrl = 0;
    private transient int baseUrlSize = 0;
    private transient long startRequestTime;
    MultiBaseUrlRequest(RequestBuilder<T> builder) {
        super(builder);
    }

    @Override
    public void send(OnCallBack<T> callBack) {
        this.callback = callBack;
        multiBaseUrlRequest();
    }

    private void multiBaseUrlRequest() {
        if (requestBuilder.intervalTime <= 0) {
            throw new RuntimeException(ABNet.getContext().getResources().getString(R.string.request_interval_short));
        }

        if (timerExecutor == null) {
            isFinish = false;
            indexBaseUrl = 0;
            baseUrlSize = requestBuilder.baseUrls.size();
            errorCount = 0;
            childResquestList.clear();
            timerExecutor = new ScheduledThreadPoolExecutor(2);
            timerTask = new ChildRequestTask();
            timerExecutor.scheduleWithFixedDelay(timerTask,0,requestBuilder.intervalTime, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void reRequest() {
        multiBaseUrlRequest();
    }

    private OnCallBack<T> mChildCallback;

    private void requestChild(String childBaseUrl) {
        NetCall<T> netCall = new RequestBuilder<>(requestBuilder.serviceMethod, requestBuilder.args)
                .baseUrl(childBaseUrl)
                .params(requestBuilder.oparams)
                .paramMap(requestBuilder.allStringParams)
                .paramFileMap(requestBuilder.fileMap)
                .headers(requestBuilder.headers)
                .jsonConverter(getJsonConverter())
                .connectTimeout(requestBuilder.connectTimeout)
                .readTimeout(requestBuilder.readTimeout)
                .writeTimeout(requestBuilder.writeTimeout);

        RequestBuilder<T> childRequestBuilder = (RequestBuilder<T>) netCall;

        Request<T> childRequest = new ChildRequest<>(childRequestBuilder);
        if (mChildCallback == null) {
            mChildCallback = new OnCallBack<T>() {
                @Override
                public void success(Request<T> request, T t) {
                    if (isFinish) {
                        return;
                    }
                    NetLog.i(request.requestBuilder.baseUrl + request.requestBuilder.url + "请求成功");
                    isFinish = true;
                    closeOthersRequest();
                    callbackSuccess(request, t);
                }

                @Override
                public void error(Request<T> request, ErrorMessage errorMessage, T cache) {
                    errorCount++;
                    //是否打断请求
                    boolean isIntercept = false;
                    errorMessage.setUrl(request.requestBuilder.baseUrl, request.requestBuilder.url);
                    NetLog.i(String.format("(%s%s) 请求失败 ： %s  , errorCount : %s : baseUrls size : %s",
                            request.requestBuilder.baseUrl, request.requestBuilder.url, errorMessage.getError().toString(), errorCount, requestBuilder.baseUrls.size()));
                    boolean isMaxErrorCount = errorCount >= requestBuilder.baseUrls.size() && !isFinish;
                    if (isMaxErrorCount || isIntercept) {
                        isFinish = true;
                        closeOthersRequest();
                        handlerError(errorMessage, null);
                    }
                }
            };
        }
        childRequest.send(mChildCallback);
        childResquestList.add(childRequest);
    }


    @Override
    protected void closeOthersRequest() {
        super.closeOthersRequest();
        try {
            cancelTimer();
            //防止ConcurrentModificationException异常
            List<Request> childList = new ArrayList<>(childResquestList);
            for (Request request : childList) {
                request.onCancelProgress();
            }

            childResquestList.clear();
        } catch (Exception e) {
             NetLog.e(e.toString());
        }
    }

    private void cancelTimer() {

        try{
            if (timerExecutor != null) {
                timerExecutor.shutdown();
                if (!timerExecutor.awaitTermination(0, TimeUnit.MILLISECONDS)){
                    timerExecutor.shutdownNow();
                }
            }
        }catch (Exception e){
             NetLog.e(e.toString());
            timerExecutor.shutdownNow();
        }
    }


    class ChildRequestTask implements Runnable {

        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
            NetLog.i(String.format("is finish ? %s , index : %s ,total baseurl : %s , timer : %s"
                    , isFinish, indexBaseUrl, baseUrlSize,startRequestTime == 0 ? 0 : startTime - startRequestTime));
            startRequestTime = startTime;
            if (isFinish || indexBaseUrl >= baseUrlSize) {
                cancelTimer();
                return;
            }
            //取消请求
            if (isCancel) {
                closeOthersRequest();
                return;
            }
            requestChild(requestBuilder.baseUrls.get(indexBaseUrl));
            indexBaseUrl++;
        }
    }

}
