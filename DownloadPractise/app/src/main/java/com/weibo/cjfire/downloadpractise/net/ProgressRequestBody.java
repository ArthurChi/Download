package com.weibo.cjfire.downloadpractise.net;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Created by cjfire on 16/10/23.
 */

public class ProgressRequestBody extends RequestBody {

    private final RequestBody mRequestBody;
    private final ProgressRequestListener mProgressRequestListener;
    private BufferedSink bufferedSink;

    public ProgressRequestBody(RequestBody mRequestBody, ProgressRequestListener mProgressRequestListener) {
        this.mRequestBody = mRequestBody;
        this.mProgressRequestListener = mProgressRequestListener;
    }

    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (bufferedSink == null) {
            bufferedSink = Okio.buffer(sink(sink));
        }

        mRequestBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {

            long byteWritten = 0L;
            long contentLength = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);

                if (contentLength == 0) {
                    contentLength = contentLength();
                }

                byteWritten += byteCount;

                mProgressRequestListener.onRequestProgress(byteWritten, contentLength, byteWritten == contentLength);
            }
        };
    }
}
