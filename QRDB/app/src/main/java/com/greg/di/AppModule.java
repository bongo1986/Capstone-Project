package com.greg.di;

import android.content.Context;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.greg.QrdbApplication;
import com.greg.domain.DataRetreiverService;
import com.greg.domain.DummyDataRetreiverServiceImpl;
import com.greg.domain.QrCodeService;
import com.greg.domain.QrCodeServiceImpl;
import com.greg.qrdb.R;
import com.greg.utils.StringRetreiver;
import com.greg.utils.StringRetreiverImpl;
import com.greg.utils.ThreadProvider;
import com.greg.utils.ThreadProviderImpl;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;

/**
 * Created by Greg on 31-10-2016.
 */
@Module
public class AppModule {
    private final QrdbApplication app;

    public AppModule(QrdbApplication app) {
        this.app = app;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return app;
    }
    @Provides
    @Singleton
    public ThreadProvider provideThreadProvider() {
        return new ThreadProviderImpl();
    }
    @Provides
    @Singleton
    public FirebaseAnalytics provideTracker(Context c) {
        return FirebaseAnalytics.getInstance(c);
    }


    @Provides
    public StringRetreiver provideStringRetreiver(Context c) {
        return new StringRetreiverImpl(c);
    }
    @Provides
    public QrCodeService provideQrcodeService(Context c) {
        return new QrCodeServiceImpl(c);
    }
    @Provides
    public DataRetreiverService provideDataRetreiverService(Context c) {
        return new DummyDataRetreiverServiceImpl();
    }





}
