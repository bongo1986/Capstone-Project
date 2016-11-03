package com.greg;

import android.app.Application;
import android.content.Context;

import com.greg.dagger.AppComponent;
import com.greg.dagger.AppModule;
import com.greg.dagger.DaggerAppComponent;

/**
 * Created by Greg on 31-10-2016.
 */
public class QrdbApplication extends Application {


    private static QrdbApplication instance = new QrdbApplication();
    private static AppComponent appComponent;

    public static QrdbApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getAppComponent();
    }

    public AppComponent getAppComponent() {
        if (appComponent == null){
            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(this))
                    .build();
        }
        return appComponent;
    }
}
