package com.dale.net.converter;


import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Desc:返回数据的解析, 具体实现看 {@link GsonJsonConverter}
 **/
public interface DataConverter {

    /**
     * desc: 把返回的数据，进行转换, 返回null表示这个请求异常
     */
    <T> T converter(String value, Type type) throws IOException;

    /**
     * desc: 异常信息反馈
     */
    String getErrorMessage();

    /**
     * desc: 异常的error code
     */
    int getErrorCode();
}
