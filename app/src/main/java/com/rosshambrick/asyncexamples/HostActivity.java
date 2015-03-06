package com.rosshambrick.asyncexamples;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;


public class HostActivity extends ActionBarActivity {

    public static final String EXTRA_FRAGMENT_NAME = "EXTRA_FRAGMENT_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        if (savedInstanceState == null) {
            Fragment fragment = Fragment.instantiate(this, getIntent().getStringExtra(EXTRA_FRAGMENT_NAME));
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }
}
