package com.dale.net.converter;


import com.dale.net.utils.JsonUtil;

import java.io.IOException;
import java.lang.reflect.Type;


public class GsonJsonConverter implements DataConverter {

    @Override
    public <T> T converter(String value, Type type) throws IOException {
        return JsonUtil.fromJson(value,type);
    }

    @Override
    public String getErrorMessage() {
        return "";
    }

    @Override
    public int getErrorCode() {
        return -1;
    }
}
