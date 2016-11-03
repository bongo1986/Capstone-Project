package com.greg.ui;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;

import com.greg.ui.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.greg.qrdb.R;

/**
 * Created by Greg on 24-10-2016.
 */
public class CreateNewQrCodeActivity extends BaseActivity {

    @BindView(R.id.input_layout_qr_code_title)
    TextInputLayout mLayoutQrCodeTitle;
    @BindView(R.id.input_layout_qr_code_description)
    TextInputLayout mLayoutQrCodeDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_qr_code);
        initBaseActivity();
        ButterKnife.bind(this);
        mLayoutQrCodeTitle.setError("Test error");
    }

    @OnClick(R.id.btn_create_qr)
    public void CreateQrCodeBtnClicked() {
        //Perform some action
    }
}