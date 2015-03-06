package com.rosshambrick.asyncexamples;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.app.AppObservable;
import rx.android.app.support.RxFragment;
import rx.android.lifecycle.LifecycleEvent;
import rx.android.lifecycle.LifecycleObservable;
import rx.schedulers.Schedulers;

import static rx.android.lifecycle.LifecycleEvent.*;

public class RxAndroidFragment extends RxFragment implements Observer<String> {
    private static final String TAG = "AsyncTaskActivity";

    private TextView resultText;
    private Observable<String> cache;

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
                        cache = bindLifecycle(getStringObservable(), DESTROY).cache();
                        cache.subscribe(RxAndroidFragment.this);
                    }
                });

        if (savedInstanceState != null) {
            if (cache != null) {
                resultText.setText("Running...");
                cache.subscribe(this);
            }
        }
    }

    @Override
    public void onNext(String result) {
        resultText.setText(result);
    }

    @Override
    public void onError(Throwable e) {
        resultText.setText(e.getLocalizedMessage());
    }

    @Override
    public void onCompleted() {
        //nothing
    }

    private Observable<String> getStringObservable() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    Thread.sleep(5000);
                    subscriber.onNext("Done!");
                    subscriber.onCompleted();
                } catch (InterruptedException e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    //TODO: PR this to RxAndroid Framework
    private <T> Observable<T> bindLifecycle(Observable<T> observable, LifecycleEvent lifecycleEvent) {
        Observable<T> boundObservable = AppObservable.bindFragment(this, observable);
        return LifecycleObservable.bindUntilLifecycleEvent(lifecycle(), boundObservable, lifecycleEvent);
    }

    //TODO: PR this to RxAndroid Framework
    private <T> Observable<T> bindLifecycle(Observable<T> observable) {
        Observable<T> boundObservable = AppObservable.bindFragment(this, observable);
        return LifecycleObservable.bindFragmentLifecycle(lifecycle(), boundObservable);
    }

}
