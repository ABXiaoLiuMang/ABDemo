package com.dale.net.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


public class NetJsonUtils {

    static class Json{
        static final Gson GSON = new Gson();
        static final Gson O_TO_JSON = new GsonBuilder().disableHtmlEscaping().create();
    }

    public static String toJson(Object src) {
        if (src == null) return "";
        return Json.GSON.toJson(src);
    }

    public static Gson getGson(){
        return Json.GSON;
    }

    public static JsonObject toJsonTree(Object data){
        return Json.GSON.toJsonTree(data).getAsJsonObject();
    }


    /**
     * 如果只需要把Object中的某一个类型转成json字符串，可以借助typeOfSrc来完成
     * @param src 实体类
     * @param typeOfSrc For example,
     * to get the type for {@code Collection<Foo>}, you should use:
     * <pre>
     * Type typeOfSrc = new TypeToken<Collection<Foo>>(){}.getType();
     * @return json字符串
     */
    public String toJson(Object src, Type typeOfSrc) {
        return Json.O_TO_JSON.toJson(src,typeOfSrc);
    }

    public String toJson(JsonElement jsonElement) {
        return Json.O_TO_JSON.toJson(jsonElement);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        if (json == null || !json.startsWith("{") || classOfT == null) return null;
        try {
            return Json.GSON.fromJson(json, classOfT);
        }catch (Exception e){
            NetLog.e(e.toString());
            return null;
        }
    }

    public static <T> T fromJson(JsonElement json, Class<T> classOfT) {
        try {
            if (json == null) return null;
            return Json.GSON.fromJson(json, classOfT);
        }catch (Exception e){
            NetLog.e(e.toString());
            return null;
        }

    }

    /**
     * desc: 返回List类型
     */
    public static <T> List<T> fromJsonToList(String json, Class<T> classOfT) {
        if (json == null || !json.startsWith("[")) return null;
        try {
            return Json.GSON.fromJson(json, new TypeToken<List<T>>(){}.getType());
        }catch (Exception e){
            NetLog.e(e.toString());
            return null;
        }
    }

    /**
     * 当Json数据不只包含数组还存在其他参数时，如果只需要json中的某一段数据，需要借助TypeToken将期望解析成的数据类型传入到fromJson()方法中.
     * 例如：
     * List<Person> people = gson.fromJson(jsonData, new TypeToken<List<Person>>(){}.getType());
     * @param json json字符串
     * @param type 要解析的json对象的类型
     * @param <T> 返回的对象
     * @return
     */
    public static <T> T fromJson(String json, Type type) {
        try {
            return Json.GSON.fromJson(json, type);
        }catch (Exception e){
            NetLog.e(e.toString());
            return null;
        }

    }

    public static JsonObject fromJson(String jsonStr) {
        try {
            return new JsonParser().parse(jsonStr).getAsJsonObject();
        }catch (Exception e){
            NetLog.e(e.toString());
            return null;
        }

    }


}
