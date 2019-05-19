package com.cn.common.senter;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * create by Dale
 * create on 2019/5/17
 * description:
 */
public class ABLifeRegistry {
    private Map<ABFragmentLifeObserver,ObserverWithYState> mObserverMap = null;
    private ABState initYState = ABState.INITIALIZED;
    private boolean mHandlingEvent = false;
    @MainThread
    public void addObserver(@NonNull ABFragmentLifeObserver observer){
        boolean needSync = mHandlingEvent && mObserverMap.size() > 0;//预防异常没有通知到重刷
        initObserverMap();
        if (mObserverMap.containsKey(observer))return;
        mObserverMap.put(observer,new ObserverWithYState(observer,initYState));
        if (needSync){
            sync();
        }
    }

    @MainThread
    public void removeObserver(@NonNull ABFragmentLifeObserver observer){
        if (mObserverMap == null) return;
        mObserverMap.remove(observer.getClass().getName());
    }

    private void initObserverMap(){
        if (mObserverMap == null) mObserverMap = new HashMap<>();
    }
    @MainThread
    public void clear(){
        try {
            if (mObserverMap != null){
                mObserverMap.clear();
                mObserverMap = null;
            }
        }catch (Exception e){
        }
    }

    @MainThread
    public void markState(@NonNull ABState state) {
        if (this.initYState != state){
            this.initYState = state;
            sync();
        }
    }

    private void sync(){
        try {
            if (mObserverMap == null) return;
            mHandlingEvent = true;
            Map<ABFragmentLifeObserver,ObserverWithYState> map = new HashMap<>(mObserverMap);
            for (ABFragmentLifeObserver key : map.keySet()) {
                mObserverMap.get(key).dispatchEvent(initYState);
            }
            mHandlingEvent = false;
        }catch (Exception e){
            mHandlingEvent = false;
        }
    }

    class ObserverWithYState {
        ABState mYState;
        private ABFragmentLifeObserver mLifeObserver;

        ObserverWithYState(ABFragmentLifeObserver mLifeObserver, ABState initialYState) {
            mYState = initialYState;
            this.mLifeObserver = mLifeObserver;
        }

        void dispatchEvent(ABState state){
            if (state != mYState){
                mYState = state;
                mLifeObserver.onLifeChange(mYState);
            }
        }
    }
}

