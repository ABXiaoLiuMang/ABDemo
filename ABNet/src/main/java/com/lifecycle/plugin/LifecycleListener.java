package com.lifecycle.plugin;


interface LifecycleListener {
    void onCreate();

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();
}
