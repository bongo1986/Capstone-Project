package com.greg.ui;

import android.os.Bundle;

import com.greg.qrdb.R;

/**
 * Created by Greg on 24-10-2016.
 */
public class ScannedQrCodesActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_qr_codes);
        initBaseActivity();
    }
}
