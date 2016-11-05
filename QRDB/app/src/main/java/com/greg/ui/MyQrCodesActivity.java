package com.greg.ui;

import android.os.Bundle;

import com.greg.QrdbApplication;
import com.greg.presentation.MyQrCodesPresenter;
import com.greg.qrdb.R;
import com.greg.ui.BaseActivity;

import javax.inject.Inject;

/**
 * Created by Greg on 22-10-2016.
 */
public class MyQrCodesActivity extends BaseActivity {

    @Inject
    MyQrCodesPresenter myQrCodesPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QrdbApplication.getInstance().getAppComponent().inject(this);
        setContentView(R.layout.activity_my_qr_codes);
        initBaseActivity();
    }
}