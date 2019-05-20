package com.dale.net;

import java.io.IOException;
import java.lang.reflect.Array;

import io.reactivex.annotations.Nullable;



abstract class AbstractParameterHandler<T> {
    abstract void apply(RequestBuilder builder, @Nullable T value) throws IOException;

    final AbstractParameterHandler<Iterable<T>> iterable() {
        return new AbstractParameterHandler<Iterable<T>>() {
            @Override
            void apply(RequestBuilder builder, @Nullable Iterable<T> values)
                    throws IOException {
                if (values == null){
                    return; // Skip null values.
                }

                for (T value : values) {
                    AbstractParameterHandler.this.apply(builder, value);
                }
            }
        };
    }

    final AbstractParameterHandler<Object> array() {
        return new AbstractParameterHandler<Object>() {
            @Override
            void apply(RequestBuilder builder, @Nullable Object values) throws IOException {
                if (values == null) {
                    return; // Skip null values.
                }

                for (int i = 0, size = Array.getLength(values); i < size; i++) {
                    //noinspection unchecked
                    AbstractParameterHandler.this.apply(builder, (T) Array.get(values, i));
                }
            }
        };
    }

    static final class RelativeUrl extends AbstractParameterHandler<String> {

        @Override
        void apply(RequestBuilder builder, @Nullable String url) throws IOException {
            if (url == null){
                url = "";
            }
            String baseUrl = Util.getHostByUrl(url);
            if (baseUrl == null){
                baseUrl = "";
            }
            String relativeUrl = url.replace(baseUrl,"");
            builder.baseUrl(baseUrl);
            builder.url(relativeUrl);
        }
    }
    
}
