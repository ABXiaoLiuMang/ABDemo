package com.dale.demo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import okhttp3.OkHttpClient;

public class Main2Activity extends AppCompatActivity {

    SmartRefreshLayout refreshLayout;
    RecyclerView recyclerview;
    MyAdapter adapter;
    String bugTest ="1234567890123";

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
//        bugTest ="";
           int s =10_000;
        OkHttpClient d;
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });



        findViewById(R.id.btn_bug).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bugTest = bugTest.substring(0,10);
                Log.d("Dream",bugTest);
                Toast.makeText(Main2Activity.this,"hhhhhhhhhhhhhh",Toast.LENGTH_LONG).show();
            }
        });

        recyclerview = findViewById(R.id.recyclerview);
        refreshLayout = findViewById(R.id.refreshLayout);
        //刷新的监听事件
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setNewData(getDatas());
                        //请求数据
                        refreshLayout.finishRefresh();  //刷新完成
                    }
                },2500);

            }
        });
        //加载的监听事件
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addData(getDatas());
                        refreshLayout.finishLoadMore();      //加载完成
                        refreshLayout.finishLoadMoreWithNoMoreData();  //全部加载完成,没有数据了调用此方法
                    }
                },2500);


            }
        });

        adapter = new MyAdapter(R.layout.item_layout, getDatas());
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(adapter);
        loadUpgradeInfo();
    }


    private ArrayList<String>  getDatas(){
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            list.add("");
        }
        return list;
    }

    public class MyAdapter extends BaseQuickAdapter<String, BaseViewHolder>{

        public MyAdapter(int layoutResId, @Nullable List<String> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {

        }
    }

    private void loadUpgradeInfo() {
        /***** 获取升级信息 *****/
        UpgradeInfo upgradeInfo = Beta.getUpgradeInfo();

        if (upgradeInfo == null) {
            Log.d("Dream","无升级信息");
            return;
        }

        StringBuilder info = new StringBuilder();
        info.append("id: ").append(upgradeInfo.id).append("\n");
        info.append("标题: ").append(upgradeInfo.title).append("\n");
        info.append("升级说明: ").append(upgradeInfo.newFeature).append("\n");
        info.append("versionCode: ").append(upgradeInfo.versionCode).append("\n");
        info.append("versionName: ").append(upgradeInfo.versionName).append("\n");
        info.append("发布时间: ").append(upgradeInfo.publishTime).append("\n");
        info.append("安装包Md5: ").append(upgradeInfo.apkMd5).append("\n");
        info.append("安装包下载地址: ").append(upgradeInfo.apkUrl).append("\n");
        info.append("安装包大小: ").append(upgradeInfo.fileSize).append("\n");
        info.append("弹窗间隔（ms）: ").append(upgradeInfo.popInterval).append("\n");
        info.append("弹窗次数: ").append(upgradeInfo.popTimes).append("\n");
        info.append("发布类型（0:测试 1:正式）: ").append(upgradeInfo.publishType).append("\n");
        info.append("弹窗类型（1:建议 2:强制 3:手工）: ").append(upgradeInfo.upgradeType);

        Log.d("Dream",info.toString());
    }
}
