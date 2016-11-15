package com.greg.utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Greg on 15-11-2016.
 */
public class QrCodesSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static QrCodesSyncAdapter sSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null)
                sSyncAdapter = new QrCodesSyncAdapter(getApplicationContext(), true);
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
