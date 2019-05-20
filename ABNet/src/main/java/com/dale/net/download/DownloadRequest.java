package com.dale.net.download;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.dale.net.ABNet;
import com.dale.net.R;
import com.dale.net.Util;
import com.dale.net.bean.RxResponse;
import com.dale.net.callback.OnFileCallback;
import com.dale.net.exception.ABNetException;
import com.dale.net.exception.ErrorMessage;
import com.dale.net.utils.MainThreadExecutor;
import com.dale.net.utils.NetLog;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.File;
import java.io.IOException;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSource;
import okio.Okio;
import okio.Sink;
import okio.Source;
import retrofit2.HttpException;

/**
 * Desc:下载请求
 **/
public class DownloadRequest {

    private String url;
    private String savePath;
    private String fileName;
    private OnFileCallback callback;
    private Call mCall;
    private Request.Builder builder = new Request.Builder();
    public Response mResponse;
    private boolean isCancel;
    private String baseUrl;
    private String relativeUrl;
    private File file;
    @SuppressWarnings("WeakerAccess")
    public transient okhttp3.Request mRequest;

    DownloadRequest(DownloadRequestBuilder builder) {
        url = builder.url;
        savePath = builder.savePath;
        fileName = builder.fileName;
        if (url == null) {
            url = "";
        }
        baseUrl = Util.getHostByUrl(url);
        if (baseUrl == null) {
            baseUrl = "";
        }
        relativeUrl = url.replace(baseUrl, "");
    }

    public void send(final OnFileCallback callback) {
        this.callback = callback;
        if (!Util.isAvailable(ABNet.getContext())) {

            ErrorMessage errorMessage = createError(ErrorMessage.NET_ERROR_CODE,
                    ABNet.getContext().getResources().getString(R.string.no_network),
                    new Throwable(ABNet.getContext().getResources().getString(R.string.no_network)));
            errorMessage.setUrl("", url);
            handlerError(errorMessage);
            return;
        }

        if (!checkUrl()) {
            return;
        }
        if (!checkFilePath()) {
            return;
        }
        if (!checkFileName()) {
            return;
        }

        Flowable.create(new FlowableOnSubscribe<RxResponse<File>>() {
            @Override
            public void subscribe(FlowableEmitter<RxResponse<File>> emitter) throws Exception {
                OkHttpClient client = ABNet.getClient();
                builder.url(url);
                final Request request = builder.get().build();
                mCall = client.newCall(request);
                mResponse = mCall.execute();
                mRequest = mCall.request();
                if (mResponse != null) {
                    try {
                        if (mCall.isCanceled()) {
                            ErrorMessage errorMessage = createError(ErrorMessage.REQUEST_ERROR_CODE,
                                    "Canceled!",
                                    new IOException("Canceled!"));
                            errorMessage.setUrl("", url);
                            handlerError(errorMessage);
                            return;
                        }


                        if (!mResponse.isSuccessful()) {
                            ErrorMessage errorMessage = createError(ErrorMessage.REQUEST_ERROR_CODE,
                                    "request failed , reponse's code is : " + mResponse.code(),
                                    new IOException("request failed , reponse's code is : " + mResponse.code()));
                            errorMessage.setUrl("", url);
                            handlerError(errorMessage);
                            return;
                        }

                        File file = saveFile(mResponse);
                        if (file == null) {
                            emitter.onError(new ABNetException("下载失败"));
                        }else {
                            emitter.onNext(new RxResponse<File>().setResult(file));
                        }
                    } catch (Exception e) {
                        ErrorMessage errorMessage = createError(ErrorMessage.REQUEST_ERROR_CODE, e.getMessage(), e);
                        errorMessage.setUrl("", url);
                        handlerError(errorMessage);
                    } finally {
                        if (mResponse.body() != null) {
                            mResponse.body().close();
                        }

                    }
                } else {
                    emitter.onError(new ABNetException("请求异常"));
                }

            }
        }, BackpressureStrategy.LATEST)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RxResponse<File>>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(RxResponse<File> fileRxResponse) {
                file = fileRxResponse.result();
                handlerSuccess();
            }

            @Override
            public void onError(Throwable t) {
                if (isCancel) {
                    return;
                }
                int code = ErrorMessage.REQUEST_ERROR_CODE;

                ErrorMessage errorMessage = createError(code,  t.getMessage(), t);
                if (mCall != null){
                    mRequest = mCall.request();
                    String requestUrl = mRequest.url().toString();
                    if (!TextUtils.isEmpty(requestUrl)) {
                        String host = Util.getHostByUrl(requestUrl);
                        errorMessage.setUrl(host, requestUrl.replace(host, ""));
                    } else {
                        errorMessage.setUrl(baseUrl, url);
                    }
                }

                if (t instanceof HttpException) {
                    HttpException httpException = (HttpException) t;
                    mResponse = httpException.response().raw();
                    errorMessage.setCode(httpException.code());
                }else if (t instanceof ABNetException){
                    ABNetException abNetException = (ABNetException) t;
                    retrofit2.Response response = abNetException.getResponse();
                    if (response != null){
                        mResponse = response.raw();
                        errorMessage.setCode(abNetException.getCode());
                    }
                }else if (t instanceof IOException){
                    errorMessage.setCode(ErrorMessage.REQUEST_ERROR_CODE);
                }else if (t instanceof RuntimeException){
                    errorMessage.setCode(ErrorMessage.RESPONSE_HANDLER_ERROR);
                }

                errorMessage.setRequest(mRequest).setResponse(mResponse);
                handlerError(errorMessage);
            }

            @Override
            public void onComplete() {

            }
        });

    }

    private boolean checkFilePath() {
        if (TextUtils.isEmpty(savePath)) {
            ErrorMessage errorMessage = createError(ErrorMessage.REQUEST_ERROR_CODE,
                    "请调用savePath()方法设置文件存储路径",
                    new NullPointerException("请调用savePath()方法设置文件存储路径"));
            handlerError(errorMessage);
            return false;
        }
        return true;
    }

    private boolean checkFileName() {
        if (TextUtils.isEmpty(fileName)) {
            ErrorMessage errorMessage = createError(ErrorMessage.REQUEST_ERROR_CODE,
                    "请调用fileName()方法设置文件存储路径",
                    new NullPointerException("请调用fileName()方法设置文件存储路径"));
            handlerError(errorMessage);
            return false;
        }
        return true;
    }

    private void handlerSuccess() {
        if (callback != null && !isCancel) {
            try {
                callback.success(DownloadRequest.this, file);
            } catch (Exception e) {
                NetLog.e(e.toString());
            }
        }
    }

    private boolean checkUrl() {

        ErrorMessage errorMessage;
        if (TextUtils.isEmpty(baseUrl)) {
            errorMessage = createError(
                    ErrorMessage.REQUEST_ERROR_CODE,
                    "baseUrl 为空",
                    new NullPointerException("baseUrl 为空")
            );
            handlerError(errorMessage);
            return false;
        }

        if (TextUtils.isEmpty(baseUrl) || !baseUrl.startsWith("http")) {
            errorMessage = createError(
                    ErrorMessage.REQUEST_ERROR_CODE,
                    "不支持非http协议请求",
                    new IllegalArgumentException("不支持非http协议请求")
            );
            handlerError(errorMessage);
            return false;
        }

        return true;
    }

    private void handlerError(final ErrorMessage errorMessage) {
        MainThreadExecutor.getInstance().execute(new Runnable() {

            @Override
            public void run() {
                if (callback != null && !isCancel) {
                    callback.error(DownloadRequest.this, errorMessage);
                }
            }
        });

    }


    private File saveFile(Response response) throws IOException {
        //生成存放文件的file
        File dir = new File(savePath);
        if (!dir.exists()) {
            boolean mkdirs = dir.mkdirs();
            if (!mkdirs) {
                String errMsg = String.format("创建%s文件失败", savePath);
                NetLog.e(errMsg);
                ErrorMessage errorMessage = createError(ErrorMessage.REQUEST_ERROR_CODE,
                        errMsg,
                        new IOException(errMsg));
                errorMessage.setUrl("", url);
                handlerError(errorMessage);
                return null;
            }
        }
        File file = new File(dir, fileName);
        //输出流
        Sink sink = Okio.sink(file);
        //输入流
        ResponseBody responseBody = response.body();
        if (responseBody == null) {
            //关流
            okhttp3.internal.Util.closeQuietly(sink);
            throw new IOException("response body is null");
        }
        Source source = Okio.source(responseBody.byteStream());
        //文件总大小
        final long totalSize = responseBody.contentLength();
        //写入到本地存储空间中
        BufferedSink bufferedSink = Okio.buffer(sink);

        //写出，并且使用代理监听写出的进度。回调UI线程的接口
        bufferedSink.writeAll(new ForwardingSource(source) {
            long sum = 0;
            int oldRate = 0;

            @Override
            public long read(@NonNull Buffer sink, long byteCount) throws IOException {
                long readSize = super.read(sink, byteCount);
                if (readSize != -1L) {
                    sum += readSize;

                    final int rate = Math.round(sum * 1F / totalSize * 100F);
                    if (oldRate != rate) {
                        MainThreadExecutor.getInstance().execute(new Runnable() {
                            @Override
                            public void run() {
                                if (callback != null && !isCancel) {
                                    callback.onProgress(rate * 1F / 100, totalSize);
                                }
                            }
                        });
                        oldRate = rate;
                    }

                }
                return readSize;
            }
        });


        //刷新数据
        bufferedSink.flush();

        //关流
        okhttp3.internal.Util.closeQuietly(sink);

        //关流
        okhttp3.internal.Util.closeQuietly(source);

        return file;
    }


    public void cancel() {
        isCancel = true;
        if (mCall != null) {
            mCall.cancel();
        }
    }


    private ErrorMessage createError(int code, String message, Throwable error) {
        return new ErrorMessage(code, message, error).setRequest(mRequest).setResponse(mResponse);
    }
}
