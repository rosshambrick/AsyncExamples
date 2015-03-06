package com.rosshambrick.asyncexamples;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

public class TracingActivity extends ActionBarActivity {
    private static final String TAG = "TRACE";

    public TracingActivity() {
        Log.d(TAG, getClass().getSimpleName() + "::ctor");
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        Log.d(TAG, getClass().getSimpleName() + "::onCreate");
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, getClass().getSimpleName() + "::onDestroy");
        super.onDestroy();
    }

    @Override
    protected void finalize() throws Throwable {
        Log.d(TAG, getClass().getSimpleName() + "::finalize");
        super.finalize();
    }
}
