1.腾讯bugly继承步骤
 a.添加两个引用
 implementation 'com.tencent.bugly:crashreport:2.8.6'
 implementation 'com.tencent.bugly:nativecrashreport:3.6.0'
 b.添加so支持库文件

        ndk {
// 设置支持的SO库架构
            abiFilters 'armeabi' //, 'x86', 'armeabi-v7a', 'x86_64', 'arm64-v8a'
        }
  c.添加权限
          <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
          <uses-permission android:name="android.permission.INTERNET" />
          <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
          <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
          <uses-permission android:name="android.permission.READ_LOGS" />
   d.Application中初始化
      CrashReport.initCrashReport(getApplicationContext(), "APPID", true);
   bug上报继承完成
```