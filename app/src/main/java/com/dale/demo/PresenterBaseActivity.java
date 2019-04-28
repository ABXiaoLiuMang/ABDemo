package com.dale.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 文件描述:
 * 作者Dale:2019/4/28
 */
public class PresenterBaseActivity <P extends BasePresenter> extends FragmentActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
    }

    protected P presenter;

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


    @SuppressWarnings("unchecked")
    private void initPresenter() {
        try {
            Class<?> childClass = this.getClass();
            Type childType = childClass.getGenericSuperclass();
            presenter = createPresenter(childType);
            while (presenter == null){
                childClass = childClass.getSuperclass();
                if (childClass == null){
                    break;
                }else {
                    if ("com.yabo.framework.app.YaboActivity".equalsIgnoreCase(childClass.getName())){
                        break;
                    }
                    childType = childClass.getGenericSuperclass();
                    presenter = createPresenter(childType);
                }
            }

            if (presenter != null){
//                presenter.setView(this);
            }
        } catch (Exception e) {
        }
    }
}
