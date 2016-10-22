package com.weibo.cjfire.downloadpractise.services;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.weibo.cjfire.downloadpractise.entities.FileInfo;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by cjfire on 16/10/22.
 */

public class DownloadService extends Service {

    public static final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/";
    public static final String ACTION_START = "ACTION_START";
    public static final String ACTION_STOP = "ACTION_STOP";
    public static final int MSG_INIT = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (ACTION_START.equals(intent.getAction())) {
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
            new InitThread(fileInfo).start();
        } else if (ACTION_STOP.equals(intent.getAction())) {
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
            Log.i("test", "stop" + fileInfo.toString());
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_INIT:
                    FileInfo fileInfo = (FileInfo) msg.obj;
                    Log.i("test", fileInfo.toString());
                    break;
            }
        }
    };

    class InitThread extends Thread {
        private FileInfo mFileinfo;
        HttpURLConnection conn;

        public InitThread(FileInfo mFileinfo) {
            this.mFileinfo = mFileinfo;
        }

        @Override
        public void run() {

            RandomAccessFile raf = null;

            try {
                URL url = new URL(mFileinfo.getUrl());
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000);
                conn.setRequestMethod("GET");
                int length = -1;

                if (conn.getResponseCode() == 200) {

                    length = conn.getContentLength();
                } else {
                    return;
                }

                File dir = new File(DOWNLOAD_PATH);

                if (!dir.exists()) {
                    dir.mkdir();
                }

                File file = new File(dir, mFileinfo.getFileName());
                raf = new RandomAccessFile(file, "rwd");

                raf.setLength(length);
                mFileinfo.setLength(length);

                mHandler.obtainMessage(MSG_INIT, mFileinfo).sendToTarget();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conn.disconnect();
                try {
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
