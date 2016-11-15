package com.greg.utils;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import com.greg.QrdbApplication;
import com.greg.domain.QrCodeService;

import javax.inject.Inject;

/**
 * Created by Greg on 15-11-2016.
 */
public class QrCodesSyncAdapter  extends AbstractThreadedSyncAdapter {

    @Inject
    QrCodeService mQrCodeService;

    public QrCodesSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        QrdbApplication.getInstance().getAppComponent().inject(this);
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        try{
            mQrCodeService.SyncScanCounts();
        }
        catch (Exception e) {
            syncResult.hasHardError();
        }

    }
}
