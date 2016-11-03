package com.weibo.cjfire.downloadpractise;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.weibo.cjfire.downloadpractise.entities.FileInfo;
import com.weibo.cjfire.downloadpractise.net.ProgressHelper;
import com.weibo.cjfire.downloadpractise.net.ProgressResponseListener;
import com.weibo.cjfire.downloadpractise.net.UIProgressResponseListener;
import com.weibo.cjfire.downloadpractise.services.DownloadService;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView mTvFileName;
    private ProgressBar mPbProgress;
    private Button mBtStop;
    private Button mBtStart;
    private OkHttpClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvFileName = (TextView) findViewById(R.id.fileName);
        mPbProgress = (ProgressBar) findViewById(R.id.progressBar);
        mBtStop = (Button) findViewById(R.id.stop);
        mBtStart = (Button) findViewById(R.id.download);
        mPbProgress.setMax(100);

        mClient = new OkHttpClient.Builder().build();

        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadService.ACTION_UPDATE);
        registerReceiver(mReceiver, filter);

        final FileInfo fileInfo = new FileInfo(0, "http://192.168.8.112/DownloadFile/index.php", "moke.rar", 0, 0);

        mBtStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                download();
//                Intent intent = new Intent(MainActivity.this, DownloadService.class);
//                intent.setAction(DownloadService.ACTION_START);
//                intent.putExtra("fileInfo", fileInfo);
//                startService(intent);
            }
        });

        mBtStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DownloadService.class);
                intent.setAction(DownloadService.ACTION_STOP);
                intent.putExtra("fileInfo", fileInfo);
                startService(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (DownloadService.ACTION_UPDATE.equals(intent.getAction())) {
                int finished = intent.getIntExtra("finished", 0);
                Log.i("test", String.valueOf(finished));
                mPbProgress.setProgress(finished);
            }
        }
    };

    private void download() {
        //这个是非ui线程回调，不可直接操作UI
        final ProgressResponseListener progressResponseListener = new ProgressResponseListener() {
            @Override
            public void onResponseProgress(long bytesRead, long contentLength, boolean done) {
                Log.e("TAG", "bytesRead:" + bytesRead);
                Log.e("TAG", "contentLength:" + contentLength);
                Log.e("TAG", "done:" + done);
                if (contentLength != -1) {
                    //长度未知的情况下回返回-1
                    Log.e("TAG", (100 * bytesRead) / contentLength + "% done");
                }
                Log.e("TAG", "================================");
            }
        };


        //这个是ui线程回调，可直接操作UI
        final UIProgressResponseListener uiProgressResponseListener = new UIProgressResponseListener() {
            @Override
            public void onUIResponseProgress(long bytesRead, long contentLength, boolean done) {
                Log.e("TAG", "bytesRead:" + bytesRead);
                Log.e("TAG", "contentLength:" + contentLength);
                Log.e("TAG", "done:" + done);
                if (contentLength != -1) {
                    //长度未知的情况下回返回-1
                    Log.e("TAG", (100 * bytesRead) / contentLength + "% done");
                }
                Log.e("TAG", "================================");
                //ui层回调
                mPbProgress.setProgress((int) ((100 * bytesRead) / contentLength));
                //Toast.makeText(getApplicationContext(), bytesRead + " " + contentLength + " " + done, Toast.LENGTH_LONG).show();
            }
        };

        //构造请求
        final Request request1 = new Request.Builder()
                .url("http://192.168.8.112/DownloadFile/index.php").addHeader("Range", "0-")
                .build();

        //包装Response使其支持进度回调
        ProgressHelper.addProgressResponseListener(mClient, uiProgressResponseListener).newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "error ", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("TAG", response.body().string());
            }
        });
    }
}
