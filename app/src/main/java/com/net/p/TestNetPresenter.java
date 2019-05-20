package com.net.p;

import android.app.Activity;
import android.os.Environment;

import com.cn.common.ui.BasePresenter;
import com.cn.common.view.ABToast;
import com.dale.net.ABNet;
import com.dale.net.callback.ABError;
import com.dale.net.callback.OnFileCallback;
import com.dale.net.download.DownloadCallback;
import com.dale.net.download.DownloadRequest;
import com.dale.net.exception.ErrorMessage;
import com.net.ApiSource;
import com.net.LoginBean;
import com.net.OnCommonCallback;
import com.net.TestBean;
import com.net.TestNetActivity;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * create by Dale
 * create on 2019/5/20
 * description:
 */
public class TestNetPresenter extends BasePresenter<TestNetActivity> implements TestNetContract.IBindPresenter{

    @Override
    public void testPostUrl() {
        String name = getView().getUserName();
        String pass = getView().getPassWord();
        ApiSource.testPostUrl(name,pass)
                .getRxResult()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new OnCommonCallback<LoginBean>(){

                    @Override
                    public void fails(ABError error) {
                        getView().resultSucc("err testPostUrl:"+ error.errorMessage);
                    }

                    @Override
                    public void onSuccess(LoginBean s){
                         getView().resultSucc("succ testPostUrl:"+ s.toString());
                    }
                });
    }

    @Override
    public void testPost() {
        String name = getView().getUserName();
        String pass = getView().getPassWord();
        ApiSource.testPost(name,pass).subscribe(new OnCommonCallback<LoginBean>() {
            @Override
            public void fails(ABError error) {
                getView().resultSucc("err testPost:"+ error.errorMessage);
            }

            @Override
            public void onSuccess(LoginBean s) throws Exception {
                getView().resultSucc("succ testPost:"+ s.toString());
            }
        });
    }

    @Override
    public void testGet() {
        ApiSource.testGet().getRxResult().subscribe(new OnCommonCallback<TestBean>() {
            @Override
            public void fails(ABError error) {
                getView().resultSucc("err testGet:"+ error.errorMessage);
            }

            @Override
            public void onSuccess(TestBean s) throws Exception {
                getView().resultSucc("succ testGet:"+ s.toString());
            }
        });
    }

    @Override
    public void testDown(final DownloadCallback downloadCallback) {
        ABNet.download()
                .url("http://wap.apk.anzhi.com/data5/apk/201905/10/f91de8b8d21087810ab57609cb347157_33995600.apk")
                .savePath(downloadDir(getActivity()))
                .fileName("daleFile.apk")
                .send(new OnFileCallback() {
                    @Override
                    public void onProgress(float progress, long total) {
                        downloadCallback.onProgress(progress,total);
                    }

                    @Override
                    public void success(DownloadRequest request, File file) throws Exception {
                        downloadCallback.onSuccess(file);
                    }

                    @Override
                    public void error(DownloadRequest request, ErrorMessage error) {
                        downloadCallback.onError(error.getError());
                        ABToast.show("下载失败");
                    }
                });
    }

    public static String downloadDir(Activity activity) {
        return Environment.getExternalStorageDirectory() + "/Android/data/" + activity.getPackageName();
    }
}
