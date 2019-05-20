package com.dale.net.callback;

import com.dale.net.exception.ErrorMessage;
import com.dale.net.utils.NetLog;
import com.google.gson.JsonParseException;

import org.apache.http.conn.ConnectTimeoutException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;


public abstract class BaseCallBack {

    protected long duration;

    public BaseCallBack() {
        init();
    }

    protected void init() {
        duration = System.currentTimeMillis();
    }

    /**
     * 请求成功，但是泛型数据为null时提示
     */
    protected void responseNull() {
        fails(ErrorFactory.getHttpError(ABStatusCode.HttpStatus.REQUEST_DATA_ERROR, "服务器数据异常"));
    }

    /**
     * 捕获异常后提示信息
     *
     * @param e 异常
     */
    protected void codeException(Exception e) {
        fails(ErrorFactory.getHttpError(ABStatusCode.HttpStatus.PROJECT_ERROR, "程序异常")
                .setRootException(e));
        NetLog.e(e.toString());
    }

    /**
     * 请求失败处理
     */
    protected void handleErrorResult(ErrorMessage errorMessage) {
        NetLog.i(errorMessage.getError() != null ? errorMessage.getError().getMessage() : "系统繁忙，请稍后重试");

        Throwable throwable = errorMessage.getError();
        if (throwable == null) {
            fails(ErrorFactory.getHttpError(errorMessage.getCode(), errorMessage.getMessage())
                    .setRequest(errorMessage.getRequest())
                    .setResponse(errorMessage.getResponse())
                    .setRootException(errorMessage.getError()));
            return;
        }

        String errorMsg = "";
        //超时
        if (throwable instanceof SocketTimeoutException
                || throwable instanceof ConnectTimeoutException) {
            errorMsg = "请求超时，请重试。";
            errorMessage.setCode(ABStatusCode.HttpStatus.HTTP_TIME_OUT);
        } else if (throwable instanceof JsonParseException) {
            errorMsg = "数据异常，解析失败";
            errorMessage.setCode(ABStatusCode.HttpStatus.HTTP_HANDLER_ERROR);
        } else if (throwable instanceof UnknownHostException) {
            errorMsg = "未知的主机";
            errorMessage.setCode(ABStatusCode.HttpStatus.UNKNOW_HOST);
        } else {
            errorMsg = HttpStatusUtils.getErrorMessage(errorMessage.getCode() + "");
        }

        errorMessage.setMessage(errorMsg);
        fails(ErrorFactory.getHttpError(errorMessage.getCode(), errorMsg)
                .setRequest(errorMessage.getRequest())
                .setResponse(errorMessage.getResponse())
                .setRootException(errorMessage.getError()));
    }


    /**
     * 返回失败结果
     *
     * @param error 错误信息
     */
    public abstract void fails(ABError error);
}
