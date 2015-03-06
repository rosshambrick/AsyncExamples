package com.rosshambrick.asyncexamples;

import android.os.AsyncTask;
import android.util.Log;

public abstract class TracingAsyncTask<TParams, TProgress, TResult> extends AsyncTask<TParams, TProgress, TResult>{
    private static final String TAG = "TRACE";

    public TracingAsyncTask() {
        Log.d(TAG, getClass().getSimpleName() + "::ctor");
    }

    @Override
    protected void finalize() throws Throwable {
        Log.d(TAG, getClass().getSimpleName() + "::finalize");
        super.finalize();
    }
}
