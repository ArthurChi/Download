package com.weibo.cjfire.downloadpractise.net;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.weibo.cjfire.downloadpractise.entities.ProgressModel;

import java.lang.ref.WeakReference;

/**
 * Created by cjfire on 16/10/23.
 */

public abstract class UIProgressRequestListener implements ProgressRequestListener {
    private static final int REQUEST_UPDATE = 0x01;

    private static class UIHandler extends Handler {

        private final WeakReference<UIProgressRequestListener> mUIProgressRequestListenerWeakReference;

        public UIHandler(Looper looper, UIProgressRequestListener uiProgressRequestListener) {
            super(looper);

            mUIProgressRequestListenerWeakReference = new WeakReference<UIProgressRequestListener>(uiProgressRequestListener);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REQUEST_UPDATE:
                    UIProgressRequestListener uiProgressRequestListener = mUIProgressRequestListenerWeakReference.get();

                    if (uiProgressRequestListener != null) {
                        ProgressModel progressModel = (ProgressModel) msg.obj;
                        uiProgressRequestListener.onUIRequestProgress(progressModel.getCurrentBytes(), progressModel.getContentLength(), progressModel.isDone());
                    }

                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }

    private final Handler mHandler = new UIHandler(Looper.getMainLooper(), this);

    @Override
    public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {

        Message msg = Message.obtain();
        msg.obj = new ProgressModel(bytesWritten, contentLength, done);
        msg.what = REQUEST_UPDATE;
        mHandler.sendMessage(msg);
    }

    public abstract void onUIRequestProgress(long bytesWrite, long contentLength, boolean done);
}
