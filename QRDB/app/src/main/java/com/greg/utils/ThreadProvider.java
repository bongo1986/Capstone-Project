package com.greg.utils;

import rx.Scheduler;

/**
 * Created by Greg on 13-11-2016.
 */
public interface ThreadProvider {
    Scheduler mainThread();
    Scheduler newThread();
}
