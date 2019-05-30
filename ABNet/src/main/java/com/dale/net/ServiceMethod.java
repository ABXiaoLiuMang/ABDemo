package com.dale.net;


import com.dale.net.http.DELETE;
import com.dale.net.http.GET;
import com.dale.net.http.POST;
import com.dale.net.http.Url;
import com.dale.net.request.RequestAdapter;
import com.dale.net.utils.Utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;


final class ServiceMethod<R, T> {
    final String httpMethod;
    final String relativeUrl;
    final RequestAdapter<R, T> requestAdapter;
    final AbstractParameterHandler<?>[] mParameterHandlers;

    private ServiceMethod(Builder<R, T> builder) {
        relativeUrl = builder.relativeUrl;
        httpMethod = builder.httpMethod;
        requestAdapter = builder.requestAdapter;
        mParameterHandlers = builder.mParameterHandlers;
    }


    static final class Builder<T, R> {
        final Annotation[] methodAnnotations;
        final Type[] parameterTypes;
        final Annotation[][] parameterAnnotationsArray;
        final Method method;
        String relativeUrl;
        Type responseType;
        String httpMethod;
        RequestAdapter<T, R> requestAdapter;
        private boolean gotUrl = false;//是否拿到Url
        private AbstractParameterHandler<?>[] mParameterHandlers;
        public Builder(Method method) {
            this.method = method;
            this.methodAnnotations = method.getAnnotations();
            this.parameterTypes = method.getGenericParameterTypes();
            this.parameterAnnotationsArray = method.getParameterAnnotations();
        }

        public ServiceMethod build() {
            requestAdapter = createRequestAdapter();
            responseType = requestAdapter.responseType();

            for (Annotation annotation : methodAnnotations) {
                parseMethodAnnotation(annotation);
            }

            if (httpMethod == null) {
                throw methodError("请在http请求的方法加上@GET 或 @POST 或 @DELETE注解");
            }

            int parameterCount = parameterAnnotationsArray.length;
            mParameterHandlers = new AbstractParameterHandler<?>[parameterCount];
            for (int p = 0; p < parameterCount; p++) {
                Type parameterType = parameterTypes[p];

                Annotation[] parameterAnnotations = parameterAnnotationsArray[p];
                if (parameterAnnotations == null) {
                    break;
                }

                mParameterHandlers[p] = parseParameter(p,parameterType,parameterAnnotations);
            }

            return new ServiceMethod<>(this);
        }

        private void parseMethodAnnotation(Annotation annotation) {
            if (annotation instanceof GET) {
                parseHttpMethodAndPath(Util.GET, ((GET) annotation).value(), false);
            } else if (annotation instanceof POST) {
                parseHttpMethodAndPath(Util.POST, ((POST) annotation).value(), true);
            }else if (annotation instanceof DELETE){
                parseHttpMethodAndPath(Util.DELETE, ((DELETE) annotation).value(), true);
            }
        }

        private AbstractParameterHandler<?> parseParameter(
                int p, Type parameterType, Annotation[] annotations) {
            AbstractParameterHandler<?> result = null;
            for (Annotation annotation : annotations) {
                AbstractParameterHandler<?> annotationAction = null;
                if (annotation instanceof Url){
                    if (gotUrl){
                        throw parameterError(p,"方法里只能有一个@Url");
                    }
                    if (relativeUrl != null){
                        throw parameterError(p,"使用了@Url,请不要在 @%s 上传参",httpMethod);
                    }

                    gotUrl = true;
                    if (parameterType == String.class){
                        annotationAction =  new AbstractParameterHandler.RelativeUrl();
                    }else {
                        throw parameterError(p,"@Url 的参数数据类型必须是String类型的");
                    }
                }

                if (annotationAction == null) continue;

                result = annotationAction;
            }

            return result;
        }

        private void parseHttpMethodAndPath(String httpMethod, String value, boolean hasBody) {
            if (this.httpMethod != null) {
                throw methodError("Only one HTTP method is allowed. Found: %s and %s.",
                        this.httpMethod, httpMethod);
            }
            this.httpMethod = httpMethod;

            if (value.isEmpty()) {
                return;
            }

            this.relativeUrl = value;
        }


        private RequestAdapter<T, R> createRequestAdapter() {
            Type returnType = method.getGenericReturnType();
            if (Utils.hasUnresolvableType(returnType)) {
                throw methodError(
                        "Method return type must not include a type variable or wildcard: %s", returnType);
            }
            if (returnType == void.class) {
                throw methodError("Service methods cannot return void.");
            }
            Annotation[] annotations = method.getAnnotations();
            try {
                //noinspection unchecked
                return (RequestAdapter<T, R>) requestAdapter(returnType, annotations);
            } catch (RuntimeException e) { // Wide exception range because factories are user code.
                throw methodError(e, "Unable to create call adapter for %s", returnType);
            }
        }

        private RequestAdapter<?, ?> requestAdapter(Type returnType, Annotation[] annotations) {
            Utils.checkNotNull(returnType, "returnType == null");
            Utils.checkNotNull(annotations, "annotations == null");

            return DefaultRequestAdapterFactory.INSTANCE.get(returnType, annotations);
        }

        RuntimeException methodError(String message, Object... args) {
            return methodError(null, message, args);
        }

        private RuntimeException methodError(Throwable cause, String message, Object... args) {
            message = String.format(message, args);
            return new IllegalArgumentException(message
                    + "\n    for method "
                    + method.getDeclaringClass().getSimpleName()
                    + "."
                    + method.getName(), cause);
        }

        private RuntimeException parameterError(int p, String message, Object... args) {
            return methodError(message + " (parameter #" + (p + 1) + ")", args);
        }
    }


}
