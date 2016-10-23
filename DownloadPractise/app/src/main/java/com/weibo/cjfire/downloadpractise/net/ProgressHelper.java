package com.weibo.cjfire.downloadpractise.net;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by cjfire on 16/10/23.
 */

public class ProgressHelper {

    public static OkHttpClient addProgressResponseListener(OkHttpClient client, final ProgressResponseListener progressResponseListener) {

        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder().body(new ProgressResponseBody(originalResponse.body(), progressResponseListener)).build();
            }
        };

        return client.newBuilder().addInterceptor(interceptor).build();
    }

    public static ProgressRequestBody addProgressRequestListener(RequestBody requestBody, ProgressRequestListener progressRequestListener) {
        return new ProgressRequestBody(requestBody, progressRequestListener);
    }
}
