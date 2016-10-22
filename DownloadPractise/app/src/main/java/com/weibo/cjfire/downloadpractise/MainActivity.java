package com.weibo.cjfire.downloadpractise;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.weibo.cjfire.downloadpractise.entities.FileInfo;
import com.weibo.cjfire.downloadpractise.services.DownloadService;

public class MainActivity extends AppCompatActivity {

    private TextView mTvFileName = null;
    private ProgressBar mPbProgress = null;
    private Button mBtStop = null;
    private Button mBtStart = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvFileName = (TextView) findViewById(R.id.fileName);
        mPbProgress = (ProgressBar) findViewById(R.id.progressBar);
        mBtStop = (Button) findViewById(R.id.stop);
        mBtStart = (Button) findViewById(R.id.download);

        final FileInfo fileInfo = new FileInfo(0,, "QQ.apk", "https://qd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk", 0, 0);

        mBtStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DownloadService.class);
                intent.setAction(DownloadService.ACTION_START);
                intent.putExtra("fileInfo", fileInfo);
                startService(intent);
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
}
