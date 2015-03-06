package com.rosshambrick.asyncexamples;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AsyncTaskFragment extends Fragment implements AsyncTaskListener<String> {
    private static final String TAG = "AsyncTaskActivity";

    private TextView resultText;
    private ActivityAsyncTask task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.async_task_runner, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        resultText = (TextView) view.findViewById(R.id.activity_async_task_result_text);

        view.findViewById(R.id.activity_async_task_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resultText.setText("Running...");
                        task = new ActivityAsyncTask();
                        task.setListener(AsyncTaskFragment.this);
                        task.execute();
                    }
                });

        if (savedInstanceState != null) {
            if (task != null) {
                resultText.setText("Running...");
                task.setListener(this); //replay previous results and register for remaining results
            }
        }
    }

    @Override
    public void onDestroy() {
        if (task != null) {
            task.setListener(null); //prevent leaking this Fragment
        }
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
    public void onFinally() {
        task = null; //prevents task from being leaked
    }

    private static class ActivityAsyncTask extends TracingAsyncTask<Void, Void, String> {
        private Exception e;
        private AsyncTaskListener<String> listener;
        private String result;

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
