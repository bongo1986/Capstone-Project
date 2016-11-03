package com.greg.dagger;

import com.greg.ui.MainActivity;
import com.greg.ui.MyQrCodesActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Greg on 31-10-2016.
 */
@Singleton
@Component(
        modules = {
                AppModule.class
        }
)
public interface AppComponent {
    void inject(MainActivity activity);
    void inject(MyQrCodesActivity activity);
}


