package com.greg.utils;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Greg on 13-11-2016.
 */
public class ThreadProviderImpl implements ThreadProvider {
    @Override
    public Scheduler mainThread() {
        return AndroidSchedulers.mainThread();
    }

    @Override
    public Scheduler newThread() {
        return Schedulers.newThread();
    }
}
