package com.dale.net.interceptor;

import android.support.annotation.NonNull;

import com.dale.net.ABNet;
import com.dale.net.utils.NetLog;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Desc:日志打印类
 **/
public class ABLoggingInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        if (!ABNet.getConfig().isNeedLog()){
            return chain.proceed(request);
        }
        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;
        Connection connection = chain.connection();
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
        StringBuffer logBuffer = new StringBuffer();
        logBuffer.append("开始发送");
        logBuffer.append(request.method());
        logBuffer.append("请求:\n");
        logBuffer.append("请求路径:");
        logBuffer.append(request.url());
        logBuffer.append(" ");
        logBuffer.append(protocol);
        logBuffer.append("\n");
        if (hasRequestBody) {
            // Request body headers are only present when installed as a network interceptor. Force
            // them to be included (when available) so there values are known.
            if (requestBody.contentType() != null) {
                logBuffer.append("Content-Type: ");
                logBuffer.append(requestBody.contentType());
                logBuffer.append("\n");
            }
            if (requestBody.contentLength() != -1) {
                logBuffer.append("Content-Length: ");
                logBuffer.append(requestBody.contentLength());
                logBuffer.append("\n");
            }
        }

        Headers headers = request.headers();
        for (int i = 0, count = headers.size(); i < count; i++) {
            String name = headers.name(i);
            // Skip headers from the request body as they are explicitly logged above.
            if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                logBuffer.append(name);
                logBuffer.append(": ");
                logBuffer.append(headers.value(i));
                logBuffer.append("\n");
            }
        }

        if (hasRequestBody && !bodyEncoded(request.headers())){
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);

            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }

            if (isPlaintext(buffer)) {
                if (charset != null){
                    logBuffer.append("请求参数: ");
                    logBuffer.append(buffer.readString(charset));
                    logBuffer.append("\n\n");
                }
            }
        }

        long t1 = System.nanoTime();//请求发起的时间
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();//收到响应的时间
        //这里不能直接使用response.body().string()的方式输出日志
        //因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一个新的response给应用层处理
        ResponseBody responseBody = response.peekBody(1024 * 1024);
        logBuffer.append("接收响应[");
        logBuffer.append(response.request().url());
        logBuffer.append("]:\n");
        logBuffer.append("请求所花时间:");
        logBuffer.append((t2-t1) /1e6d);
        logBuffer.append("\n");
        Headers resHeaders = response.headers();
        for (int j = 0, rescount = resHeaders.size(); j < rescount; j++) {
            String name = resHeaders.name(j);
            // Skip headers from the request body as they are explicitly logged above.
            logBuffer.append(name);
            logBuffer.append(": ");
            logBuffer.append(resHeaders.value(j));
            logBuffer.append("\n");
        }
        logBuffer.append("响应状态码:");
        logBuffer.append(response.code());
        logBuffer.append("\n");
        logBuffer.append("\n 返回数据:\n");
        String body = unicodeToUTF_8(responseBody.string());
        logBuffer.append(body != null && body.length() > 2800 * 2 ? body.substring(0,2800 * 2) + ",未完..." : body);
        logBuffer.append("\n*****************end");
        logBuffer.append(request.method());
        logBuffer.append(" ");
        logBuffer.append(request.url());
        logBuffer.append(" ");
        logBuffer.append(protocol);
        logBuffer.append("*********************");

        NetLog.i(logBuffer.toString());

        return response;
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    private boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            // Truncated UTF-8 sequence.
            return false;
        }
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

    private static String unicodeToUTF_8(String src) {
        if (null == src) {
            return null;
        }
        StringBuilder out = new StringBuilder();
        try {
            for (int i = 0; i < src.length();) {
                char c = src.charAt(i);
                if (i + 6 < src.length() && c == '\\' && src.charAt(i + 1) == 'u') {
                    String hex = src.substring(i + 2, i + 6);
                    out.append((char) Integer.parseInt(hex, 16));
                    i = i + 6;
                } else {
                    out.append(src.charAt(i));
                    ++i;
                }
            }
        }catch (Exception e){
            NetLog.e(e.toString());
        }
        return out.toString();
    }
}
