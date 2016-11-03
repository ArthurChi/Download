package com.weibo.cjfire.downloadpractise.services;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.weibo.cjfire.downloadpractise.db.ThreadDAO;
import com.weibo.cjfire.downloadpractise.db.ThreadDAOImp;
import com.weibo.cjfire.downloadpractise.entities.FileInfo;
import com.weibo.cjfire.downloadpractise.entities.ThreadInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

/**
 * Created by cjfire on 16/10/22.
 */

public class DownloadTask {

    private Context mContext = null;
    private FileInfo mFileInfo = null;
    private ThreadDAO mDao = null;
    private int mFinished = 0;
    boolean isPause = false;
    private FileChannel channelOut;

    public DownloadTask(Context mContext, FileInfo mFileInfo) {
        this.mContext = mContext;
        this.mFileInfo = mFileInfo;

        mDao = new ThreadDAOImp(mContext);
    }

    public void download() {
        List<ThreadInfo> threadInfos = mDao.getThread(mFileInfo.getUrl());
        ThreadInfo threadInfo = null;

        if (threadInfos.size() == 0) {
            threadInfo = new ThreadInfo(0, mFileInfo.getUrl(), 0, mFileInfo.getLength(), 0);
        } else {
            threadInfo = threadInfos.get(0);
        }

        new DownloadThread(threadInfo).start();
    }

    class DownloadThread extends Thread {
        private ThreadInfo mThreadInfo = null;

        public DownloadThread(ThreadInfo mThreadInfo) {
            this.mThreadInfo = mThreadInfo;
        }

        @Override
        public void run() {

            HttpURLConnection conn = null;
            RandomAccessFile raf = null;
            InputStream input = null;

            if (!mDao.isExists(mThreadInfo.getId(), mThreadInfo.getUrl())) {
                mDao.insertThread(mThreadInfo);
            }

            try {
                URL url = new URL(mThreadInfo.getUrl());
                conn = (HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(30000);
                conn.setRequestMethod("GET");

                int start = mThreadInfo.getStart() + mThreadInfo.getFinished();
                conn.setRequestProperty("Range", "bytes="+start+"-"+mThreadInfo.getEnd());

                File file = new File(DownloadService.DOWNLOAD_PATH, mFileInfo.getFileName());
                raf = new RandomAccessFile(file, "rwd");
//                raf.seek(start);
                channelOut = raf.getChannel();
                MappedByteBuffer mappedByteBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE, start, mFileInfo.getLength());

                Intent intent = new Intent(DownloadService.ACTION_UPDATE);
                mFinished += mThreadInfo.getFinished();
                // start to download
                Log.i("test", String.valueOf(conn.getResponseCode()));

                if (conn.getResponseCode() < 300) {

                    input = conn.getInputStream();
                    byte[] buffer = new byte[1024];
                    int len = -1;

                    long time = System.currentTimeMillis();
                    while ((len = input.read(buffer)) != -1) {
                            mappedByteBuffer.put(buffer, 0 ,len);
                            mFinished += len;
                            Log.i("test", String.valueOf(mFinished) + "~~~~~" + String.valueOf(mFileInfo.getLength()));
                        if (System.currentTimeMillis() - time > 500 || mFinished == mFileInfo.getLength()) {
                            time = System.currentTimeMillis();
                            intent.putExtra("finished", (int)((float)mFinished / mFileInfo.getLength() * 100));
                            mContext.sendBroadcast(intent);
                        }

                        if (isPause) {
                            mThreadInfo.setFinished(mFinished);
                            mDao.updateThreat(mThreadInfo);
                            return;
                        }
                    }

                    mDao.deleteThread(mThreadInfo.getId(), mThreadInfo.getUrl());
                } else {
                    Log.i("test", String.valueOf(conn.getResponseCode()));
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conn.disconnect();
                try {

                    if (channelOut != null) {
                        channelOut.close();
                    }

                    if (raf != null) {
                        raf.close();
                    }

                    if (input != null) {
                        input.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
