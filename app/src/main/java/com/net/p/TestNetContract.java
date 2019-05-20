package com.net.p;

import com.dale.net.download.DownloadCallback;

/**
 * create by Dale
 * create on 2019/5/20
 * description:
 */
public class TestNetContract {
    interface IBindPresenter {
         void testPostUrl();
         void testPost();
         void testGet();
         void testDown(DownloadCallback downloadCallback);
    }

    public interface IBindView  {
        void resultSucc(String result);
        String getUserName();
        String getPassWord();
    }
}
