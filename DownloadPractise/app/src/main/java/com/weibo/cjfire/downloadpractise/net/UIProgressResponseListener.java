package com.weibo.cjfire.downloadpractise.net;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.weibo.cjfire.downloadpractise.entities.ProgressModel;

import java.lang.ref.WeakReference;

/**
 * Created by cjfire on 16/10/23.
 */

public abstract class UIProgressResponseListener implements ProgressResponseListener {

    private static final int RESPONSE_UPDATE = 0X02;

    private static class UIHandler extends Handler {

        private WeakReference<UIProgressResponseListener> mUIProgressResponseListenerWeakReference;

        public UIHandler(Looper looper, UIProgressResponseListener mUIProgressResponseListenerWeakReference) {
            super(looper);
            this.mUIProgressResponseListenerWeakReference = new WeakReference<UIProgressResponseListener>(mUIProgressResponseListenerWeakReference);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RESPONSE_UPDATE:
                    UIProgressResponseListener uiProgressResponseListener = mUIProgressResponseListenerWeakReference.get();
                    if (uiProgressResponseListener != null) {
                        ProgressModel progressModel = (ProgressModel) msg.obj;
                        uiProgressResponseListener.onUIResponseProgress(progressModel.getCurrentBytes(), progressModel.getContentLength(), progressModel.isDone());
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
    public void onResponseProgress(long bytesRead, long contentLength, boolean done) {

        Message msg = Message.obtain();
        msg.obj = new ProgressModel(bytesRead, contentLength, done);
        msg.what = RESPONSE_UPDATE;
        mHandler.sendMessage(msg);
    }

    public abstract void onUIResponseProgress(long currentBytes, long contentLength, boolean done);
}
