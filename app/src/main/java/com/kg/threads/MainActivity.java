package com.kg.threads;

import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button switchBtn;
    private Button startBtn;
    private Button cancelBtn;
    private Switch testSwitch;
    private ProgressBar progressBar;

    private Handler mainHandler;
    private HandlerThread workerThread;
    private Handler workerHandler;

    private volatile boolean cancel = false;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            doWork(10);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Threads");
        setContentView(R.layout.activity_main);

        switchBtn = findViewById(R.id.switchBtn);
        startBtn = findViewById(R.id.startBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        testSwitch = findViewById(R.id.testSwitch);
        progressBar = findViewById(R.id.progressBar);

        mainHandler = new Handler(Looper.getMainLooper());

        workerThread = new HandlerThread("worker");
        workerThread.start();
        workerHandler = new Handler(workerThread.getLooper());

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel = false;
                workerHandler.post(runnable);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel = true;
            }
        });

        switchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AsyncTaskActivity.class));
            }
        });
    }

    private void doWork(final int seconds) {
        for(int i = 1; i <= seconds; i++) {
            if(cancel){
                break;
            }
            try {
                Log.i(TAG, "doWork: " + i);
                progressBar.setProgress(progressBar.getMax() * i / seconds);
                final int finalI = i;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startBtn.setText(progressBar.getMax() * finalI / seconds + "%");
                    }
                });
                startBtn.post(new Runnable() {
                    @Override
                    public void run() {
                        startBtn.setText(progressBar.getMax() * finalI / seconds + "%");

                    }
                });
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        startBtn.setText(progressBar.getMax() * finalI / seconds + "%");
                    }
                });
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
