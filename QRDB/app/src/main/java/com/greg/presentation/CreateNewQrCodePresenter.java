package com.greg.presentation;

import com.google.zxing.common.StringUtils;
import com.greg.domain.QrBitmap;
import com.greg.domain.QrCode;
import com.greg.domain.QrCodeService;
import com.greg.qrdb.R;
import com.greg.utils.StringRetreiver;

import javax.inject.Inject;

/**
 * Created by Greg on 03-11-2016.
 */
public class CreateNewQrCodePresenter implements BasePresenter<CreateNewQrCodeView>{

    private QrCodeService mQrCodeService;
    private CreateNewQrCodeView mCreateNewQrCodeView;
    private StringRetreiver mStringRetreiver;
    @Inject
    public CreateNewQrCodePresenter(QrCodeService qrSrv, StringRetreiver stringRetreiver) {
        mQrCodeService = qrSrv;
        mStringRetreiver = stringRetreiver;
    }
    @Override
    public void setView(CreateNewQrCodeView view) {
        mCreateNewQrCodeView = view;
    }
    @Override
    public void resume() {
        QrBitmap qrBitap = mQrCodeService.GenerateNewQrCode();
        QrCode qrCode = new QrCode("", "", qrBitap.getmUUID(), qrBitap.getBitmapBytes());
        mCreateNewQrCodeView.showQrCode(qrCode);
    }
    @Override
    public void pause() {

    }
    @Override
    public void destroy() {
        mCreateNewQrCodeView = null;
    }

    public void CreateNewQrCode(QrCode qr){
        boolean errorFound = false;
        if(qr.getmTitle() == null || qr.getmTitle().equals("")){
            mCreateNewQrCodeView.showTitleError(mStringRetreiver.getString(R.string.empty_field_error_message));
            errorFound = true;
        }
        if(qr.getmTitle().length() > 300 || qr.getmTitle().length() > 300){
            mCreateNewQrCodeView.showTitleError(mStringRetreiver.getString(R.string.text_too_long_message));
            errorFound = true;
        }
        if(qr.getmDescription() == null || qr.getmDescription().equals("")){
            mCreateNewQrCodeView.showDescriptionError(mStringRetreiver.getString(R.string.empty_field_error_message));
            errorFound = true;
        }
        if(qr.getmDescription().length() > 300 || qr.getmDescription().length() > 300){
            mCreateNewQrCodeView.showDescriptionError(mStringRetreiver.getString(R.string.empty_field_error_message));
            errorFound = true;
        }
        if(errorFound == false) {
            mCreateNewQrCodeView.showWaitDialog();
            mQrCodeService.InsertQRcode(qr).subscribe(id -> {
                if (mCreateNewQrCodeView != null) {
                    mCreateNewQrCodeView.hideWaitDialog();
                    if (id != -1) {
                        mCreateNewQrCodeView.redirectToMyQrCodes(true);
                    } else {

                    }
                }
            }, e -> {
                mCreateNewQrCodeView.hideWaitDialog();
            });
        }
    }
}
