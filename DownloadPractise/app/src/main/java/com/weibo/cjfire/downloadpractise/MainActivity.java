package com.weibo.cjfire.downloadpractise;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

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

    }
}
