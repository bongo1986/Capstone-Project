package com.greg.dagger;

import android.content.Context;

import com.greg.QrdbApplication;
import com.greg.presentation.MyQrCodesPresenter;
import com.greg.presentation.MyQrCodesPresenterImpl;

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
    @Singleton
    public IdummyClass provideDummyClass() {
        return new dummyClass();
    }

    @Provides
    @Singleton
    public MyQrCodesPresenter provideMyQrCodesPresenter(IdummyClass dummy) {
        return new MyQrCodesPresenterImpl(dummy);
    }

}
