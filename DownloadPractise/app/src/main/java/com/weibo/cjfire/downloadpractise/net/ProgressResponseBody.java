package com.weibo.cjfire.downloadpractise.net;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by cjfire on 16/10/23.
 */

public class ProgressResponseBody extends ResponseBody {

    private final ResponseBody mResponseBody;
    private final ProgressResponseListener mProgressListener;
    private BufferedSource mBufferedSource;

    public ProgressResponseBody(ResponseBody mResponseBody, ProgressResponseListener mProgressListener) {
        this.mResponseBody = mResponseBody;
        this.mProgressListener = mProgressListener;
    }

    @Override
    public MediaType contentType() {
        return mResponseBody.contentType();
    }

    @Override
    public long contentLength() {
        return mResponseBody.contentLength();
    }

    @Override
    public BufferedSource source() {

        if (mBufferedSource == null) {
            mBufferedSource = Okio.buffer(source(mResponseBody.source()));
        }
        return mBufferedSource;
    }

    private Source source(Source source) {

        return new ForwardingSource(source) {

            long totalBytesRead = 0L;
            @Override
            public long read(Buffer sink, long byteCount) throws IOException {

                long bytesRead = super.read(sink, byteCount);

                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                mProgressListener.onResponseProgress(totalBytesRead, mResponseBody.contentLength(), bytesRead == -1);

                return super.read(sink, byteCount);
            }
        };
    }
}
