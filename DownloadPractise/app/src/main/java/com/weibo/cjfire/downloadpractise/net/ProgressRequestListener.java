package com.weibo.cjfire.downloadpractise.net;

/**
 * Created by cjfire on 16/10/23.
 */

public interface ProgressRequestListener {
    void onRequestProgress(long bytesWritten, long contentLength, boolean done);
}
