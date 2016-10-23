package com.weibo.cjfire.downloadpractise.net;

/**
 * Created by cjfire on 16/10/23.
 */

public interface ProgressResponseListener {

    void onResponseProgress(long bytesRead, long contentLength, boolean done);
}
