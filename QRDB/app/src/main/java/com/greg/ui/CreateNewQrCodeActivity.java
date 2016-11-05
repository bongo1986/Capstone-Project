package com.greg.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;
import android.widget.ImageView;

import com.greg.QrdbApplication;
import com.greg.domain.QrCode;
import com.greg.presentation.CreateNewQrCodePresenter;
import com.greg.presentation.CreateNewQrCodeView;
import com.greg.ui.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.greg.qrdb.R;

import javax.inject.Inject;

/**
 * Created by Greg on 24-10-2016.
 */
public class CreateNewQrCodeActivity extends BaseActivity implements CreateNewQrCodeView {

    @BindView(R.id.input_layout_qr_code_title)
    TextInputLayout mLayoutQrCodeTitle;
    @BindView(R.id.input_layout_qr_code_description)
    TextInputLayout mLayoutQrCodeDescription;
    @BindView(R.id.qr_code_img)
    ImageView mQrCodeImage;
    @BindView(R.id.qr_code_title)
    EditText mEditTextTitle;
    @BindView(R.id.qr_code_description)
    EditText mEditTextDescription;

    @Inject
    CreateNewQrCodePresenter mCreateNewQrCodePresenter;

    private QrCode mQr;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_qr_code);
        initBaseActivity();
        QrdbApplication.getInstance().getAppComponent().inject(this);
        ButterKnife.bind(this);
        mCreateNewQrCodePresenter.setView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mCreateNewQrCodePresenter.resume();
    }
    @Override
    public void onPause(){
        super.onPause();
        mCreateNewQrCodePresenter.pause();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        mCreateNewQrCodePresenter.setView(null);
        mCreateNewQrCodePresenter.destroy();
    }

    @OnClick(R.id.btn_create_qr)
    public void CreateQrCodeBtnClicked() {
        mQr.setmDescription(mEditTextDescription.getText().toString());
        mQr.setmTitle(mEditTextTitle.getText().toString());
        mCreateNewQrCodePresenter.CreateNewQrCode(mQr);
    }

    @Override
    public void showQrCode(QrCode qr) {
        mQr = qr;
        mQrCodeImage.setImageBitmap(qr.getQrBitmap());
    }

    @Override
    public void showTitleError(String errorText) {
        mLayoutQrCodeTitle.setError(errorText);
    }

    @Override
    public void showDescriptionError(String errorText) {
        mLayoutQrCodeDescription.setError(errorText);
    }

    @Override
    public void redirectToMyQrCodes(boolean afterSuccessfullCreate) {
        finish();
        Intent myQrCodes = new Intent(this, MyQrCodesActivity.class);
        if(afterSuccessfullCreate){
            myQrCodes.putExtra("QR_CODE_CREATED", true );
        }
        startActivity(myQrCodes);
    }

    @Override
    public void showWaitDialog() {
        progress = new ProgressDialog(this);
        progress.setMessage(getString(R.string.wait_dialog_message));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    @Override
    public void hideWaitDialog() {
        progress.hide();
    }
}