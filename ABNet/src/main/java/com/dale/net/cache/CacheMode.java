package com.dale.net.cache;

public enum CacheMode {

    /** 不使用缓存 默认不缓存*/
    NO_CACHE(1),

    /** 请求网络失败后，读取缓存 */
    REQUEST_FAILED_READ_CACHE(2),

    /** 如果缓存不存在才请求网络，否则使用缓存 */
    IF_NONE_CACHE_REQUEST(3),

    /** 先使用缓存，不管是否存在，仍然请求网络 */
    FIRST_CACHE_THEN_REQUEST(4),

    /**
     * 先使用缓存，不管是否存在，仍然请求网络，CallBack回调不一定是两次，
     * 如果发现请求的网络数据和缓存数据是一样的，就不会再返回网络的回调,既回调一次。
     * 否则不相同仍然会回调两次。
     * （目的是为了防止数据没有发生变化，也需要回调两次导致界面无用的重复刷新）
     * */
    CACHE_AND_REMOTE_DISTINCT(5);

    private int mode;
    CacheMode(int mode) {
        this.mode = mode;
    }

    public int mode(){
        return mode;
    }
}
