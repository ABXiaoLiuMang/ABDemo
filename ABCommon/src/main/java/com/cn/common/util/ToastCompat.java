package com.cn.common.util;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Date: 2019/1/22
 * Time: 11:08
 * author:div
 */
public class ToastCompat extends Toast {
    public ToastCompat(Context context) {
        super(context);
    }

    @Override

    public void show() {
        if (checkIfNeedToHack()) {
            tryToHack();
        }
        super.show();
    }


    protected boolean checkIfNeedToHack() {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1;
    }

    private void tryToHack() {
        try {
            Object mTN = getFieldValue(this, "mTN");
            if (mTN != null) {
                boolean isSuccess = false;
                //a hack to some device which use the code between android 6.0 and android 7.1.1
                Object rawShowRunnable = getFieldValue(mTN, "mShow");
                if (rawShowRunnable != null && rawShowRunnable instanceof Runnable) {
                    isSuccess = setFieldValue(mTN, "mShow", new InternalRunnable((Runnable) rawShowRunnable));
                }

                // hack to android 7.1.1,these cover 99% devices.
                if (!isSuccess) {
                    Object rawHandler = getFieldValue(mTN, "mHandler");
                    if (rawHandler != null && rawHandler instanceof Handler) {
                        isSuccess = setFieldValue(rawHandler, "mCallback", new InternalHandlerCallback((Handler) rawHandler));
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private class InternalRunnable implements Runnable {
        private final Runnable mRunnable;

        public InternalRunnable(Runnable mRunnable) {
            this.mRunnable = mRunnable;
        }

        @Override
        public void run() {
            try {
                this.mRunnable.run();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }


    private class InternalHandlerCallback implements Handler.Callback {
        private final Handler mHandler;

        public InternalHandlerCallback(Handler mHandler) {
            this.mHandler = mHandler;
        }

        @Override
        public boolean handleMessage(Message msg) {
            try {
                mHandler.handleMessage(msg);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return true;
        }
    }

    private static boolean setFieldValue(Object object, String fieldName, Object newFieldValue) {
        Field field = getDeclaredField(object, fieldName);
        if (field != null) {
            try {
                int accessFlags = field.getModifiers();
                if (Modifier.isFinal(accessFlags)) {
                    Field modifiersField = Field.class.getDeclaredField("accessFlags");
                    modifiersField.setAccessible(true);
                    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
                }
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                field.set(object, newFieldValue);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    private static Object getFieldValue(Object obj, final String fieldName) {
        Field field = getDeclaredField(obj, fieldName);
        return getFieldValue(obj, field);
    }

    private static Object getFieldValue(Object obj, Field field) {
        if (field != null) {
            try {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                return field.get(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    private static Field getDeclaredField(final Object obj, final String fieldName) {
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                Field field = superClass.getDeclaredField(fieldName);
                return field;
            } catch (NoSuchFieldException e) {
                continue;// new add
            }
        }
        return null;
    }
}
