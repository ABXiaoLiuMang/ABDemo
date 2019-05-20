package com.dale.net.download;

import java.io.File;

/**
 * create by Dale
 * create on 2019/5/20
 * description:
 */
public interface DownloadCallback {

    void onProgress(float var1, long var2);

    void onSuccess(File var1);

    void onError(Throwable var1);
}
