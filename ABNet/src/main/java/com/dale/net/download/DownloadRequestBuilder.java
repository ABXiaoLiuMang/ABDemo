package com.dale.net.download;


import com.dale.net.callback.OnFileCallback;


public class DownloadRequestBuilder {
     String url;
     String savePath;
     String fileName;
    private DownloadRequest mRequest;
    public DownloadRequestBuilder() {
    }

    public DownloadRequestBuilder url(String url) {
        this.url = url;
        return this;
    }

    public DownloadRequestBuilder savePath(String savePath) {
        this.savePath = savePath;
        return this;
    }

    public DownloadRequestBuilder fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public void cancelDownload(){
       if (mRequest != null){
           mRequest.cancel();
       }
    }

    public DownloadRequest send(OnFileCallback callback){
        mRequest = new DownloadRequest(this);
        mRequest.send(callback);
        return mRequest;
    }
}
