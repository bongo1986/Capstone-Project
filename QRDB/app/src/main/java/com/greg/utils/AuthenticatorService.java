package com.greg.utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Greg on 15-11-2016.
 */
public class AuthenticatorService extends Service {
    private Authenticator mAuthenticator;
    public AuthenticatorService() {
    }

    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new Authenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
