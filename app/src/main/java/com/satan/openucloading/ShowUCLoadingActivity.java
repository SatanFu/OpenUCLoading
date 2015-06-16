package com.satan.openucloading;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


public class ShowUCLoadingActivity extends Activity {

    private static final int HIDE_LOADING = 111;
    private RevealLayout mRevealLayout;
    private ImageView mContent;
    private Context mContext;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == HIDE_LOADING) {
                mRevealLayout.next();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ucloading);
        mContext = this;
        initView();
        initListener();
        initData();
    }


    private void initView() {
        mRevealLayout = (RevealLayout) findViewById(R.id.rl);
        mContent = (ImageView) findViewById(R.id.iv_content);

    }

    private void initListener() {
        mContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "this is toast", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    mHandler.sendEmptyMessage(HIDE_LOADING);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
