package com.dale.net.callback;


import com.dale.net.download.DownloadRequest;
import com.dale.net.exception.ErrorMessage;

import java.io.File;


public interface OnFileCallback {
    /**
     * 进度
     *
     * @param progress 进度0.00 - 0.50  - 1.00
     * @param total    文件总大小 单位字节
     */
    void onProgress(float progress, long total);

    void success(DownloadRequest request, File file) throws Exception;

    void error(DownloadRequest request, ErrorMessage error);
}
