package com.kg.threads;



import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

public class AsyncTaskActivity extends AppCompatActivity {

    private static final String TAG = "AsyncTaskActivity";

    private Button switchBtn;
    private Button startBtn;
    private Button cancelBtn;
    private Switch testSwitch;
    private ProgressBar progressBar;

    private WorkTask workTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Async Task");
        setContentView(R.layout.activity_main);

        switchBtn = findViewById(R.id.switchBtn);
        startBtn = findViewById(R.id.startBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        testSwitch = findViewById(R.id.testSwitch);
        progressBar = findViewById(R.id.progressBar);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workTask = new WorkTask();
                workTask.execute(10);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(workTask != null) {
                    workTask.cancel(true);
                }
            }
        });

        switchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AsyncTaskActivity.this, MainActivity.class));
            }
        });
    }

    private class WorkTask extends AsyncTask<Integer, Float, Long>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Long result) {
            Toast.makeText(AsyncTaskActivity.this, "Task finished in " + result,
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(Float... progress) {
            progressBar.setProgress((int) (progressBar.getMax() * progress[0]));
            startBtn.setText(100 * progress[0] + "%");
        }

        @Override
        protected Long doInBackground(Integer... params) {
            long time = System.currentTimeMillis();
            int seconds = params[0];
            for(int i = 1; i <= seconds; i++) {
                if(isCancelled()) {
                    break;
                }
                try {
                    Log.i(TAG, "doWork: " + i);
                    Thread.sleep(1000);
                    publishProgress((float) i / seconds);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return (System.currentTimeMillis() - time)/1000;
        }
    }
}
