package com.cn.common.ui;

import android.support.annotation.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * create by Dale
 * create on 2019/5/17
 * description:
 */
public class Util {
    static <P extends BasePresenter> P createPresenter(Type parameterizedType) throws Exception{
        if (parameterizedType instanceof ParameterizedType &&
                ((ParameterizedType) parameterizedType).getActualTypeArguments().length > 0) {
            Type[] types = ((ParameterizedType) parameterizedType).getActualTypeArguments();
            for (Type type : types) {
                if (type instanceof Class){
                    Class mPresenterClass = (Class) type;
                    Object instance = getInstance(mPresenterClass);
                    if (instance instanceof BasePresenter){
                        return (P) instance;
                    }
                }
            }

        }
        return null;
    }

    /**
     * 通过实例工厂去实例化相应类
     *
     * @return
     */
    @Nullable
    public static Object getInstance(Class clazz) throws Exception{
        switch (clazz.getName()){
            default: return  clazz.newInstance();
        }
    }
}
