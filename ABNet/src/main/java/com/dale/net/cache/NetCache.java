package com.dale.net.cache;


import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Looper;
import android.text.TextUtils;

import com.dale.net.ABNet;
import com.dale.net.utils.JsonUtil;
import com.dale.net.utils.Md5Utils;
import com.dale.net.utils.NetLog;
import com.dale.net.utils.StringUtils;
import com.jakewharton.disklrucache.NetDiskLruCache;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.WeakHashMap;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * Desc:网络缓存类
 **/
public class NetCache {
    private static final int ENTRY_COUNT = 1;
    private NetDiskLruCache mDiskLruCache;
    private WeakHashMap<String,Entity> cacheMap = new WeakHashMap<>();
    static class SingleHolder{
        static final NetCache CACHE = new NetCache();
    }

    public static NetCache getInstance(){
        return SingleHolder.CACHE;
    }

    private NetCache() {
        try {
            File cacheFileDir = getCacheFile("ABNetCache");
            if (!cacheFileDir.exists()){
                cacheFileDir.mkdirs();
            }

            //不需要调用close方法,只有在切换缓存目录时才需要调用close方法
            this.mDiskLruCache = NetDiskLruCache.open(cacheFileDir, VersionCode(), ENTRY_COUNT, Integer.MAX_VALUE);
        } catch (IOException e) {
            NetLog.e(e.toString());
        }
    }

    /**
     * 获取缓存的路径 两个路径在卸载程序时都会删除，因此不会在卸载后还保留乱七八糟的缓存
     * 有SD卡时获取  /sdcard/Android/data/<application package>/cache
     * 无SD卡时获取  /data/data/<application package>/cache
     *
     * @param uniqueName 缓存目录下的细分目录，用于存放不同类型的缓存
     * @return 缓存目录 File
     */
    private File getCacheFile(String uniqueName) {
        String cachePath;
        cachePath = ABNet.getContext().getCacheDir().getPath();
        if (TextUtils.isEmpty(cachePath)){

            if ((Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                    || !Environment.isExternalStorageRemovable())
                    && ABNet.getContext().getExternalCacheDir() != null) {
                cachePath = ABNet.getContext().getExternalCacheDir().getPath();
            }else {
                cachePath = ABNet.getContext().getFilesDir().getPath();
            }
        }

        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 获得软件版本号
     */
    private int VersionCode() {
        int versioncode = 0;
        try {
            versioncode = ABNet.getContext().getPackageManager().getPackageInfo(
                    ABNet.getContext().getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
             NetLog.e(e.toString());
        }

        return versioncode;
    }

    public static String key(Object... params) {
        //重写,转String有异常情况
        return hex(Md5Utils.md5(StringUtils.buffer(params)));
    }

    private static final char[] HEX_DIGITS =
            { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /** Returns this byte string encoded in hexadecimal. */
    private static String hex(byte[] data) {
        char[] result = new char[data.length * 2];
        int c = 0;
        for (byte b : data) {
            result[c++] = HEX_DIGITS[(b >> 4) & 0xf];
            result[c++] = HEX_DIGITS[b & 0xf];
        }
        return new String(result);
    }

    /**
     * 获取缓存 editor
     *
     * @param key 缓存的key
     * @return editor
     * @throws IOException
     */
    private NetDiskLruCache.Editor edit(String key) throws IOException {
        if (mDiskLruCache != null) {
            return mDiskLruCache.edit(key);
        }
        return null;
    }

    /**
     * 根据 key 获取缓存缩略
     *
     * @param key 缓存的key
     * @return Snapshot
     */
    private NetDiskLruCache.Snapshot snapshot(String key) {
        if (mDiskLruCache != null) {
            try {
                return mDiskLruCache.get(key);
            } catch (IOException e) {
                 NetLog.e(e.toString());
            }
        }
        return null;
    }

    /**
     * 缓存 String
     *
     * @param key   缓存文件键值（MD5加密结果作为缓存文件名）
     *              @see #key(Object...)
     * @param value 缓存内容
     */
    public void put(String key, String value) {
        put(key,value,0);
    }

    /**
     * 缓存 String
     *
     * @param key   缓存文件键值（MD5加密结果作为缓存文件名）
     * @param value 缓存内容
     */
    public void put(final String key, String value, long cacheTime) {
        if (TextUtils.isEmpty(value)) {
            return;
        }
        Entity memoryCache = cacheMap.get(key);
        String valueMd5 = Md5Utils.md5String(value);
        if (memoryCache != null && !memoryCache.isOverdue()){
            if (TextUtils.equals(memoryCache.md5,valueMd5)) {
                return;
            }
        }
        final Entity cacheEntity = new Entity(value, System.currentTimeMillis(),cacheTime,valueMd5);
        cacheEntity.memoryCacheValue = value;
        cacheMap.put(key,cacheEntity);
        if (Looper.myLooper() == Looper.getMainLooper()){
            cacheEntity.key = key;
            Flowable.just(cacheEntity)
                    .observeOn(Schedulers.computation())
                    .map(new Function<Entity, String>() {
                        @Override
                        public String apply(Entity netCache) throws Exception {
                            excSave(netCache.key,cacheEntity);
                            return "";
                        }
                    }).subscribe();
        }else {
            excSave(key,cacheEntity);
        }

    }

    private void excSave(String key, Entity cacheEntity) {
        NetDiskLruCache.Editor editor = null;
        BufferedWriter writer = null;
        try {
            editor = edit(key);
            if (editor == null) {
                return;
            }

            String cacheString = JsonUtil.toJson(cacheEntity);
            OutputStream os = editor.newOutputStream(0);
            writer = new BufferedWriter(new OutputStreamWriter(os));
            writer.write(cacheString);
            editor.commit();
        } catch (IOException e) {
             NetLog.e(e.toString());
            try {
                if (editor != null){
                    editor.abort();
                }
            } catch (IOException e1) {
                NetLog.e(e.toString());
            }
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                 NetLog.e(e.toString());
            }
        }

        flush();
    }

    /**
     * 获取字符串缓存
     *
     * @param key cache'key
     * @return string
     */
    public String getString(String key) {
        Entity memoryCache = cacheMap.get(key);
        if (memoryCache == null){
            memoryCache = new Entity();
            memoryCache.memoryCacheValue = getCacheString(key);
            if (memoryCache.memoryCacheValue != null) {
                cacheMap.put(key,memoryCache);
            }
        }else if (!(memoryCache.memoryCacheValue instanceof String) || memoryCache.isOverdue()){
            cacheMap.remove(key);
            memoryCache = new Entity();
            memoryCache.memoryCacheValue = getCacheString(key);
            if (memoryCache.memoryCacheValue != null) {
                cacheMap.put(key,memoryCache);
            }
        }
        return (String) memoryCache.memoryCacheValue;


    }

    private String getCacheString(String key) {
        InputStream inputStream = getCacheInputStream(key);
        if (inputStream == null) {
            return null;
        }
        try {
            String cache = inputStream2String(inputStream);
            Entity cacheEntity = JsonUtil.fromJson(cache,Entity.class);
            if (cacheEntity.isOverdue()){
                remove(key);
                return null;
            }else {
                return cacheEntity.getData();
            }

        } catch (IOException e) {
             NetLog.e(e.toString());
            return null;
        } finally {
            try {
                inputStream.close();
            } catch (IOException e1) {
                NetLog.e(e1.toString());
            }
        }
    }

    /**
     * inputStream 转 String
     *
     * @param is 输入流
     * @return 结果字符串
     */
    private String inputStream2String(InputStream is) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuilder buffer = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            buffer.append(line);
        }
        return buffer.toString();
    }

    /**
     * 获取 缓存数据的 InputStream
     *
     * @param key cache'key
     * @return InputStream
     */
    private InputStream getCacheInputStream(String key) {
        InputStream in;
        NetDiskLruCache.Snapshot snapshot = snapshot(key);
        if (snapshot == null) {
            return null;
        }
        in = snapshot.getInputStream(0);
        return in;
    }

    /**
     * 同步记录文件
     */
    public void flush() {
        if (mDiskLruCache != null) {
            try {
                mDiskLruCache.flush();
            } catch (IOException e) {
                 NetLog.e(e.toString());
            }
        }
    }

    private class Entity{
        /**
         * 缓存日期
         */
        private long saveDateMil;
        private String data;
        private long cacheTime;
        private String md5;
        transient Object memoryCacheValue;
        public String key;
        public Entity(String data, long saveDateMil, long cacheTime, String md5) {
            this.saveDateMil = saveDateMil;
            this.data = data;
            this.cacheTime = cacheTime;
            this.md5 = md5;
        }

        public Entity() {
        }

        /**
         * desc: 是否过期
         */
        public boolean isOverdue() {
            return cacheTime > 0 && System.currentTimeMillis() - saveDateMil > cacheTime;
        }

        public String getData() {
            return data;
        }
    }

    public boolean remove(String key) {
        try {
            return mDiskLruCache.remove(key);
        } catch (IOException e) {
            NetLog.e(e.toString());
        }
        return false;
    }

    public void close() throws IOException {
        if (!isClosed()){
            mDiskLruCache.close();
        }
    }

    public void delete() throws IOException {
        mDiskLruCache.delete();
    }

    public boolean isClosed()
    {
        return mDiskLruCache.isClosed();
    }

    public long size()
    {
        return mDiskLruCache.size();
    }

    public void setMaxSize(long maxSize)
    {
        mDiskLruCache.setMaxSize(maxSize);
    }

    public File getDirectory()
    {
        return mDiskLruCache.getDirectory();
    }

    public long getMaxSize()
    {
        return mDiskLruCache.getMaxSize();
    }



}
