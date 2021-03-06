package com.rosshambrick.asyncexamples;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;


public class MenuActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        findViewById(R.id.activity_async_task)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MenuActivity.this, AsyncTaskActivity.class));
                    }
                });

        findViewById(R.id.fragment_async_task)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MenuActivity.this, HostActivity.class);
                        intent.putExtra(HostActivity.EXTRA_FRAGMENT_NAME, AsyncTaskFragment.class.getCanonicalName());
                        startActivity(intent);
                    }
                });

        findViewById(R.id.fragment_rxandroid)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MenuActivity.this, HostActivity.class);
                        intent.putExtra(HostActivity.EXTRA_FRAGMENT_NAME, RxAndroidFragment.class.getCanonicalName());
                        startActivity(intent);
                    }
                });
    }
}
