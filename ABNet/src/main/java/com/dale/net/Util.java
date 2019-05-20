package com.dale.net;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;

import com.dale.net.utils.NetLog;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;


public class Util {

    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String DELETE = "DELETE";
    /**
     * The Utf 8.
     */
    public static final Charset UTF8 = Charset.forName("UTF-8");

    /**
     * Check time out int.
     *
     * @param name     the name
     * @param duration the duration
     * @return the int
     */
    static int checkTimeOut(String name , long duration) {
        if (duration < 0) throw new IllegalArgumentException(name + " < 0");
        if (duration > Integer.MAX_VALUE) throw new IllegalArgumentException(name + " too large.");
        if (duration == 0) throw new IllegalArgumentException(name + " too small.");
        return (int) duration;
    }

    /**
     * Check time out boolean.
     *
     * @param duration the duration
     * @return the boolean
     */
    static boolean checkTimeOut(long duration) {
        if (duration < 0) return false;
        if (duration > Integer.MAX_VALUE) return false;
        if (duration == 0) return false;
        return true;
    }

    /**
     * Check not null t.
     *
     * @param <T>     the type parameter
     * @param object  the object
     * @param message the message
     * @return the t
     */
    static <T> T checkNotNull(@Nullable T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }

    /**
     * Is available boolean.
     *
     * @param var0 the var 0
     * @return the boolean
     */
    public static boolean isAvailable(Context var0) {
        NetworkInfo var1 = getActiveNetworkInfo(var0);
        return var1 != null && var1.isAvailable();
    }

    private static NetworkInfo getActiveNetworkInfo(Context var0) {
        try {
            return ((ConnectivityManager) var0.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        }catch (Exception e){
            NetLog.e(e.toString());
            return null;
        }
    }

    /**
     * Create url from params string.
     *
     * @param url    the url
     * @param params the params
     * @return the string
     */
    static String createUrlFromParams(String url, Map<String, String> params) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(url);
            if (url.indexOf('&') > 0 || url.indexOf('?') > 0) {
                sb.append("&");
            } else {
                sb.append("?");
            }
            for (Map.Entry<String, String> urlParams : params.entrySet()) {
                String urlValues = urlParams.getValue();
                //对参数进行 utf-8 编码,防止头信息传中文
                //String urlValue = URLEncoder.encode(urlValues, UTF8.name());
                sb.append(urlParams.getKey()).append("=").append(urlValues).append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        } catch (Exception e) {
            NetLog.e(e.getMessage());
        }
        return url;
    }

    /**
     * Gets raw type.
     *
     * @param type the type
     * @return the raw type
     */
    static Class<?> getRawType(Type type) {
        checkNotNull(type, "type == null");

        if (type instanceof Class<?>) {
            // Type is a normal class.
            return (Class<?>) type;
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;

            // I'm not exactly sure why getRawType() returns Type instead of Class. Neal isn't either but
            // suspects some pathological case related to nested classes exists.
            Type rawType = parameterizedType.getRawType();
            if (!(rawType instanceof Class)) throw new IllegalArgumentException();
            return (Class<?>) rawType;
        }
        if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            return Array.newInstance(getRawType(componentType), 0).getClass();
        }
        if (type instanceof TypeVariable) {
            // We could use the variable's bounds, but that won't work if there are multiple. Having a raw
            // type that's more general than necessary is okay.
            return Object.class;
        }
        if (type instanceof WildcardType) {
            return getRawType(((WildcardType) type).getUpperBounds()[0]);
        }

        throw new IllegalArgumentException("Expected a Class, ParameterizedType, or "
                + "GenericArrayType, but <" + type + "> is of type " + type.getClass().getName());
    }

    /**
     * Returns the generic supertype for {@code supertype}. For example, given a class {@code
     * IntegerSet}*, the result for when supertype is {@code Set.class} is {@code Set<Integer>} and the
     * result when the supertype is {@code Collection.class} is {@code Collection<Integer>}.
     *
     * @param context   the context
     * @param rawType   the raw type
     * @param toResolve the to resolve
     * @return the generic supertype
     */
    static Type getGenericSupertype(Type context, Class<?> rawType, Class<?> toResolve) {
        if (toResolve == rawType) return context;

        // We skip searching through interfaces if unknown is an interface.
        if (toResolve.isInterface()) {
            Class<?>[] interfaces = rawType.getInterfaces();
            for (int i = 0, length = interfaces.length; i < length; i++) {
                if (interfaces[i] == toResolve) {
                    return rawType.getGenericInterfaces()[i];
                } else if (toResolve.isAssignableFrom(interfaces[i])) {
                    return getGenericSupertype(rawType.getGenericInterfaces()[i], interfaces[i], toResolve);
                }
            }
        }

        // Check our supertypes.
        if (!rawType.isInterface()) {
            while (rawType != Object.class) {
                Class<?> rawSupertype = rawType.getSuperclass();
                if (rawSupertype == toResolve) {
                    return rawType.getGenericSuperclass();
                } else if (toResolve.isAssignableFrom(rawSupertype)) {
                    return getGenericSupertype(rawType.getGenericSuperclass(), rawSupertype, toResolve);
                }
                rawType = rawSupertype;
            }
        }

        // We can't resolve this further.
        return toResolve;
    }

    /**
     * Validate service interface.
     *
     * @param <T>     the type parameter
     * @param service the service
     */
    static <T> void validateServiceInterface(Class<T> service) {
        if (!service.isInterface()) {
            throw new IllegalArgumentException("API 声明类必须是interfaces.");
        }
        // Prevent API interfaces from extending other interfaces. This not only avoids a bug in
        // Android (http://b.android.com/58753) but it forces composition of API declarations which is
        // the recommended pattern.
        if (service.getInterfaces().length > 0) {
            throw new IllegalArgumentException("API声明接口不能继承其它interfaces.");
        }
    }

    /**
     * Get host by url string.
     *
     * @param urlString the url string
     * @return the string
     */
    public  static String getHostByUrl(String urlString){
          if (urlString == null) {
              return "";
          }


        try {
            URL url = new URL(urlString);
            String protocol = url.getProtocol();
            String host = url.getHost();
            int port = url.getPort();
            if (port == -1){
                return String.format("%s://%s/",protocol,host);
            }else {
                return String.format("%s://%s:%s/",protocol,host,port);
            }
        } catch (MalformedURLException e) {
            NetLog.e(e.toString());
        }
        return "";
    }


}
