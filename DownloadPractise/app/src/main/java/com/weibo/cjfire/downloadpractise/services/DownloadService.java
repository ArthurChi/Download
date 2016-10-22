package com.weibo.cjfire.downloadpractise.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.weibo.cjfire.downloadpractise.entities.FileInfo;

/**
 * Created by cjfire on 16/10/22.
 */

public class DownloadService extends Service {

    public static final String ACTION_START = "ACTION_START";
    public static final String ACTION_STOP = "ACTION_STOP";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (ACTION_START.equals(intent.getAction())) {
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
            Log.i("test", "start" + fileInfo.toString());
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
}
