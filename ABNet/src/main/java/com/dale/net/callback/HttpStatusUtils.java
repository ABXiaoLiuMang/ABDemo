package com.dale.net.callback;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * create by Dale
 * create on 2019/5/20
 * description:
 */
public class HttpStatusUtils {
    public static String showMessage(int code, String msg) {
//        msg = String.format("%s(%s)", msg, (code + 5) * 2);
        return msg;
    }


    /**
     * 据状态码返回提示的错误信息
     *
     * @param code 状态码
     */
    public static String getErrorMessage(String code) {
        return getErrorMessage(code, "");
    }

    /**
     * 根据状态码返回提示的错误信息
     *
     * @param code 状态码
     * @param msg  错误信息
     */
    public static String getErrorMessage(String code, String msg) {
        if (!TextUtils.isEmpty(msg)) {
            return msg;
        }

        String errorMsg;
        switch (code) {
            default:
                int _code = 0;
                try {
                    _code = Integer.valueOf(code);
                } catch (Exception e) {}
                errorMsg = getHttpErrorMessage(_code);
                break;
        }
        return errorMsg;
    }

    /**
     * 根据http请求返回提示信息
     *
     * @param code 状态码
     */
    private static String getHttpErrorMessage(int code) {
        String msg;
        switch (code) {
            case ABStatusCode.HttpStatus.HTTP_NET_ERROR:
                msg = "网络异常，请重试";
                break;
            case ABStatusCode.HttpStatus.HTTP_HANDLER_ERROR:
                msg = "数据异常，解析失败";
                break;
            default:
                msg = HttpStatusUtils.showMessage(code, "系统繁忙，请稍后重试");
                break;
        }
        return msg;
    }

    @SuppressWarnings("unused")
    public static Map<String, String> urlSplit(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        Map<String, String> mapRequest = new HashMap<>(16);
        String[] arrSplit;
        String strUrlParam = TruncateUrlPage(url);
        if (strUrlParam == null) {
            return mapRequest;
        }
        arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual;
            arrSplitEqual = strSplit.split("[=]");
            //解析出键值
            if (arrSplitEqual.length > 1) {
                //正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
            } else {
                if (TextUtils.isEmpty(arrSplitEqual[0])) {
                    //只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }

    private static String TruncateUrlPage(String strURL) {
        String strAllParam = null;
        String[] arrSplit;
        strURL = strURL.trim().toLowerCase();
        arrSplit = strURL.split("[?]");
        if (strURL.length() > 1) {
            if (arrSplit.length > 1) {
                for (int i = 1; i < arrSplit.length; i++) {
                    strAllParam = arrSplit[i];
                }
            }
        }
        return strAllParam;
    }
}
