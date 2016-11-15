package com.greg.di;

import com.greg.di.AppModule;
import com.greg.ui.CRUDQrCodeActivity;
import com.greg.ui.CRUDQrCodeFragment;
import com.greg.ui.QrCodeListActivity;
import com.greg.ui.QrGridFragment;
import com.greg.ui.ScanActivity;
import com.greg.utils.QrCodesSyncAdapter;

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
    void inject(CRUDQrCodeFragment fragment);
    void inject(QrGridFragment fragment);
    void inject(CRUDQrCodeActivity activity);
    void inject(QrCodeListActivity activity);
    void inject(ScanActivity activity);
    void inject(QrCodesSyncAdapter activity);

}


