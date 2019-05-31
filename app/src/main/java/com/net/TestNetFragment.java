package com.net;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.cn.common.ui.AbstractTabFragment;
import com.cn.common.ui.BaseActivity;
import com.cn.common.ui.BaseFragment;
import com.cn.common.view.ABToast;
import com.dale.demo.R;
import com.dale.net.ABNet;
import com.dale.net.download.DownloadCallback;
import com.net.p.TestNetContract;
import com.net.p.TestNetPresenter;
import com.tab.MainActivity;

import java.io.File;

/**
 * create by Dale
 * create on 2019/5/20
 * description:
 */
public class TestNetFragment extends AbstractTabFragment<TestNetPresenter> implements TestNetContract.IBindView {

    String [] arr = new String[]{"https://apitest.phptestit.com/CPTEST/","https://www.soarg999.com/ZZCP/","https://www.soarg999.com/CP57/","https://www.soarg999.com/FCCP/"};

    int i = 0;
    ProgressBar progressBar;
    TextView tv_result;

    public static TestNetFragment newInstance() {

        Bundle args = new Bundle();

        TestNetFragment fragment = new TestNetFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_testnet;
    }

    @Override
    protected void initViewsAndEvents() {

        ((MainActivity)getActivity()).test();

        tv_result = rootView.findViewById(R.id.tv_result);
          progressBar = rootView.findViewById(R.id.progressBar);
        rootView.findViewById(R.id.btn_postUrl).setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  presenter.testPostUrl();
              }
          });
        rootView. findViewById(R.id.btn_Post).setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  presenter.testPost();
              }
          });
        rootView. findViewById(R.id.btn_Get).setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  presenter.testGet();
              }
          });
        rootView.findViewById(R.id.btn_Down).setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  presenter.testDown(new DownloadCallback() {

                      @Override
                      public void onProgress(float var1, long var2) {
                          Log.d("Dream","onProgress var1:"+var1+"  var2:"+var2);
                          progressBar.setProgress((int) (var1 * 100));
                      }

                      @Override
                      public void onSuccess(File var1) {
                          Log.d("Dream","onSuccess:"+var1.getPath());
                      }

                      @Override
                      public void onError(Throwable var1) {
                          Log.d("Dream","onError:"+var1.getMessage());
                      }
                  });
              }
          });
        rootView.findViewById(R.id.btn_changeBaseUrl).setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  i++;
                  ABNet.getConfig().baseUrl(arr[i%4]);
              }
          });
    }

    @Override
    public void resultSucc(String result) {
        LogUtils.d("result:"+result);
        tv_result.setText("->"+result);
    }

    @Override
    public String getUserName() {
        return "div123";
    }

    @Override
    public String getPassWord() {
        return "111111";
    }
}
