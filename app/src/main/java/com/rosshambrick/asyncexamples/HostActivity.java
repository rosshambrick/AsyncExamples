package com.rosshambrick.asyncexamples;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class HostActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new AsyncTaskFragment())
                    .commit();
        }
    }
}
