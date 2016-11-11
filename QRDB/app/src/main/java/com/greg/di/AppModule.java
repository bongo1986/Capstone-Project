package com.greg.di;

import android.content.Context;

import com.greg.QrdbApplication;
import com.greg.domain.QrCodeService;
import com.greg.domain.QrCodeServiceImpl;
import com.greg.utils.StringRetreiver;
import com.greg.utils.StringRetreiverImpl;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

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
    public StringRetreiver provideStringRetreiver(Context c) {
        return new StringRetreiverImpl(c);
    }

    @Provides
    public QrCodeService provideQrcodeService(Context c) {
        return new QrCodeServiceImpl(c);
    }



}
