package com.rosshambrick.asyncexamples;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class NonCancellingAsyncTaskActivity extends TracingActivity implements AsyncTaskListener<String> {
    private static final String TAG = "AsyncTaskActivity";

    private TextView resultText;
    private ActivityAsyncTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_task);

        resultText = (TextView) findViewById(R.id.activity_async_task_result_text);

        findViewById(R.id.activity_async_task_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        task = new ActivityAsyncTask();
                        task.setListener(NonCancellingAsyncTaskActivity.this);
                        task.execute();
                    }
                });

        if (savedInstanceState != null) {
            //restores instance for caching on rotation
            task = (ActivityAsyncTask) getLastCustomNonConfigurationInstance();
            if (task != null) {
                task.setListener(this); //replay previous results and register for remaining results
            }
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return task; //saves instance for caching on rotation
    }

    @Override
    protected void onDestroy() {
        task.setListener(null); //prevent leaking this Activity
        super.onDestroy();
    }

    @Override
    public void onError(Exception e) {
        resultText.setText(e.getLocalizedMessage());
    }

    @Override
    public void onSuccess(String result) {
        resultText.setText(result);
    }

    @Override
    public void onPreExecute() {
        resultText.setText("Running...");
    }

    @Override
    public void onFinally() {
        task = null; //prevents task from being leaked
    }

    private class ActivityAsyncTask extends TracingAsyncTask<Void, Void, String> {
        private Exception e;
        private AsyncTaskListener<String> listener;
        private String result;

        @Override
        protected void onPreExecute() {
            if (listener != null) {
                listener.onPreExecute();
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                this.e = e;
            }
            return "Done!";
        }

        @Override
        protected void onPostExecute(String result) {
            this.result = result;
            dispatchResult();
        }

        private void dispatchResult() {
            if (listener != null) {
                if (e == null) { //handle success
                    listener.onSuccess(result);
                } else { //handle errors
                    listener.onError(e);
                }
                listener.onFinally();
            }
            task = null; //prevents task from being leaked
        }

        public void setListener(AsyncTaskListener<String> listener) {
            this.listener = listener;
            if (getStatus().equals(Status.FINISHED)) {
                dispatchResult();
            } else if (getStatus().equals(Status.RUNNING)) {
                onPreExecute();
            }
        }
    }
}
