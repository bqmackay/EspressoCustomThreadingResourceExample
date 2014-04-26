package com.example.myapplication2.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends Activity implements RepsCallbackInterface {

    @InjectView(R.id.button)    Button repsThreadButton;
    @InjectView(R.id.button2)   Button repsAsyncTaskButton;
    @InjectView(R.id.textView2) TextView threadText;
    @InjectView(R.id.textView3) TextView asyncText;
    private DownloadHelper downloadHelper;
    private boolean didThreadReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        downloadHelper = new DownloadHelper(this);

        repsThreadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadHelper.getReps();
            }
        });

        repsAsyncTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadHelper.getRepsWithAsyncTask();
            }
        });
    }

    @Override
    public void onRepsThreadReceived(final boolean didReceive) {
        setDidThreadReturn(true);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                threadText.setText(didReceive ? "Thread Retrieved" : "Thread Failed");
            }
        });
    }

    @Override
    public void onRepsAsyncReceived(boolean didReceive) {
        asyncText.setText(didReceive ? "Async Retrieved" : "Async Failed");
    }

    public DownloadHelper getDownloadHelper() {
        return downloadHelper;
    }

    public void setDownloadHelper(DownloadHelper downloadHelper) {
        this.downloadHelper = downloadHelper;
    }

    public boolean isDidThreadReturn() {
        return didThreadReturn;
    }

    public void setDidThreadReturn(boolean didThreadReturn) {
        this.didThreadReturn = didThreadReturn;
    }
}
