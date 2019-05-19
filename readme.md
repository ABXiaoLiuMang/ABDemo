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

********************************************************
2.腾讯热更新继承步骤
官网地址：http://www.tinkerpatch.com/Docs/SDK
a.在项目的build.gradle中，添加tinker-plugin的依赖
   classpath "com.tencent.bugly:tinker-support:1.1.2"
b.然后在app的gradle文件app/build.gradle，我们需要添加tinker的库依赖以及apply tinker的gradle插件
   implementation 'com.tencent.tinker:tinker-android-lib:1.9.6'
   implementation 'com.tencent.bugly:crashreport_upgrade:1.3.5'
   apply from: 'tinkerpatch.gradle'
c.为了方便管理，定义一个脚本管理热更新配置。
   1.命名为apply from: 'tinkerpatch.gradle'，在module buuld.gradle中引用
   2.新建tinkerpatch.gradle脚本文件
   3.添加权限
     <uses-permission android:name="android.permission.INTERNET"/>
     <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
     <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
   4. Application中初始化

    @Override
    public void onCreate() {
        super.onCreate();
//        第三个参数为SDK调试模式开关，调试模式的行为特性如下：
//        输出详细的Bugly SDK的Log
//        每一条Crash都会被立即上报
//                自定义日志将会在Logcat中输出
//        建议在测试阶段建议设置成true，发布时设置为false。
        CrashReport.initCrashReport(getApplicationContext(), "04de93395e", true);//初始化日志上报
        Bugly.init(getApplicationContext(),"04de93395e",true);//初始化热更新

    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Beta.installTinker(); // 安装热修复 tinker
    }


 注意：
 热更新和日志上报用  implementation 'com.tencent.bugly:crashreport_upgrade:1.3.5' ，
 只是上报用：    implementation 'com.tencent.bugly:crashreport:2.8.6'
 //基准包 如：1.0.0-base   每次更新增加1 1.0.1-patch，注意后缀和加一
Android N（7.0）以后版本需要配置如下：
        <provider
            android:name="android.support.v4.content.FileProvider"
            android:authorities="${applicationId}.fileProvider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/provider_paths"/>
        </provider>

        如果配置已经运用第三方Provider，可以继承实现，如下：
        <provider
            android:name=".utils.BuglyFileProvider"
            android:authorities="${applicationId}.fileProvider"
            android:exported="false"
            android:grantUriPermissions="true"
            tools:replace="name,authorities,exported,grantUriPermissions">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/provider_paths"
                tools:replace="name,resource"/>
        </provider>


 app->build->xxx运行基准包
 app->tinker-support-xxx运行补丁包  这里切记，容易出错

 1.SDK初始化
 注意：如果您之前使用过Bugly SDK错误日志上报功能，请将以下这句注释掉。一定去掉，不然上传补丁包时会报基准包找不到的错误
 CrashReport.initCrashReport(getApplicationContext(), "注册时申请的APPID", false);

 采用统一初始化方法：
 Bugly.init(getApplicationContext(), "注册时申请的APPID", false);

 2.配置FileProvider
 注意：如果您想兼容Android N或者以上的设备，必须要在AndroidManifest.xml文件中配置FileProvider来访问共享路径的文件。
  <provider
  android:name="android.support.v4.content.FileProvider"
  android:authorities="${applicationId}.fileProvider"
  android:exported="false" android:grantUriPermissions="true">
     <meta-data
  android:name="android.support.FILE_PROVIDER_PATHS"
  android:resource="@xml/provider_paths"/>
 </provider>
 3.上面文档中 autoGenerateTinkerId = true，设置的基准包和补丁包的
 tinkerId将不起作用，默认会以当前时间加版本号为当前tinkerId。
 4.测试发现，在开发过程中，如果打的基准包安装在手机上，启动后在bugly版本管理列表中看不到基准包版本号，此时
 打的补丁包上传后会提示基准包找不到，此时耐心等待2-3分钟等bugly联网自动上传即可。

 5.官方继承文档中提到需要注册如下：
 <activity android:name="com.tencent.bugly.beta.ui.BetaActivity"
               android:configChanges="keyboardHidden|orientation|screenSize|locale"
               android:theme="@android:style/Theme.Translucent" />
 但在实际开发过程中发现去掉也可以。



//SmartRefreshLayout
  //下面示例中的值等于默认值
        RefreshLayout refreshLayout = (RefreshLayout)findViewById(R.id.refreshLayout);
        refreshLayout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);
        refreshLayout.setDragRate(0.5f);//显示下拉高度/手指真实下拉高度=阻尼效果
        refreshLayout.setReboundDuration(300);//回弹动画时长（毫秒）

        refreshLayout.setHeaderHeight(100);//Header标准高度（显示下拉高度>=标准高度 触发刷新）
        refreshLayout.setHeaderHeightPx(100);//同上-像素为单位 （V1.1.0删除）
        refreshLayout.setFooterHeight(100);//Footer标准高度（显示上拉高度>=标准高度 触发加载）
        refreshLayout.setFooterHeightPx(100);//同上-像素为单位 （V1.1.0删除）

        refreshLayout.setFooterHeaderInsetStart(0);//设置 Header 起始位置偏移量 1.0.5
        refreshLayout.setFooterHeaderInsetStartPx(0);//同上-像素为单位 1.0.5 （V1.1.0删除）
        refreshLayout.setFooterFooterInsetStart(0);//设置 Footer 起始位置偏移量 1.0.5
        refreshLayout.setFooterFooterInsetStartPx(0);//同上-像素为单位 1.0.5 （V1.1.0删除）

        refreshLayout.setHeaderMaxDragRate(2);//最大显示下拉高度/Header标准高度
        refreshLayout.setFooterMaxDragRate(2);//最大显示下拉高度/Footer标准高度
        refreshLayout.setHeaderTriggerRate(1);//触发刷新距离 与 HeaderHeight 的比率1.0.4
        refreshLayout.setFooterTriggerRate(1);//触发加载距离 与 FooterHeight 的比率1.0.4

        refreshLayout.setEnableRefresh(true);//是否启用下拉刷新功能
        refreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
        refreshLayout.setEnableAutoLoadMore(true);//是否启用列表惯性滑动到底部时自动加载更多
        refreshLayout.setEnablePureScrollMode(false);//是否启用纯滚动模式
        refreshLayout.setEnableNestedScroll(false);//是否启用嵌套滚动
        refreshLayout.setEnableOverScrollBounce(true);//是否启用越界回弹
        refreshLayout.setEnableScrollContentWhenLoaded(true);//是否在加载完成时滚动列表显示新的内容
        refreshLayout.setEnableHeaderTranslationContent(true);//是否下拉Header的时候向下平移列表或者内容
        refreshLayout.setEnableFooterTranslationContent(true);//是否上拉Footer的时候向上平移列表或者内容
        refreshLayout.setEnableLoadMoreWhenContentNotFull(true);//是否在列表不满一页时候开启上拉加载功能
        refreshLayout.setEnableFooterFollowWhenLoadFinished(false);//是否在全部加载结束之后Footer跟随内容1.0.4
        refreshLayout.setEnableOverScrollDrag(false);//是否启用越界拖动（仿苹果效果）1.0.4

        refreshLayout.setEnableScrollContentWhenRefreshed(true);//是否在刷新完成时滚动列表显示新的内容 1.0.5
        refreshLayout.srlEnableClipHeaderWhenFixedBehind(true);//是否剪裁Header当时样式为FixedBehind时1.0.5
        refreshLayout.srlEnableClipFooterWhenFixedBehind(true);//是否剪裁Footer当时样式为FixedBehind时1.0.5

        refreshLayout.setDisableContentWhenRefresh(false);//是否在刷新的时候禁止列表的操作
        refreshLayout.setDisableContentWhenLoading(false);//是否在加载的时候禁止列表的操作

        refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener());//设置多功能监听器
        refreshLayout.setScrollBoundaryDecider(new ScrollBoundaryDecider());//设置滚动边界判断
        refreshLayout.setScrollBoundaryDecider(new ScrollBoundaryDeciderAdapter());//自定义滚动边界

        refreshLayout.setRefreshHeader(new ClassicsHeader(context));//设置Header
        refreshLayout.setRefreshFooter(new ClassicsFooter(context));//设置Footer
        refreshLayout.setRefreshContent(new View(context));//设置刷新Content（用于非xml布局代替addView）1.0.4

        refreshLayout.autoRefresh();//自动刷新
        refreshLayout.autoLoadMore();//自动加载
        refreshLayout.autoRefresh(400);//延迟400毫秒后自动刷新
        refreshLayout.autoLoadMore(400);//延迟400毫秒后自动加载
        refreshLayout.finishRefresh();//结束刷新
        refreshLayout.finishLoadMore();//结束加载
        refreshLayout.finishRefresh(3000);//延迟3000毫秒后结束刷新
        refreshLayout.finishLoadMore(3000);//延迟3000毫秒后结束加载
        refreshLayout.finishRefresh(false);//结束刷新（刷新失败）
        refreshLayout.finishLoadMore(false);//结束加载（加载失败）
        refreshLayout.finishLoadMoreWithNoMoreData();//完成加载并标记没有更多数据 1.0.4
        refreshLayout.closeHeaderOrFooter();//关闭正在打开状态的 Header 或者 Footer（1.1.0）
        refreshLayout.resetNoMoreData();//恢复没有更多数据的原始状态 1.0.4（1.1.0删除）
        refreshLayout.setNoMoreData(false);//恢复没有更多数据的原始状态 1.0.5