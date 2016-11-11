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
public class CRUDQrCodePresenter implements BasePresenter<CRUDQrCodeView>{

    private QrCodeService mQrCodeService;
    private CRUDQrCodeView mCRUDQrCodeView;
    private StringRetreiver mStringRetreiver;
    @Inject
    public CRUDQrCodePresenter(QrCodeService qrSrv, StringRetreiver stringRetreiver) {
        mQrCodeService = qrSrv;
        mStringRetreiver = stringRetreiver;
    }

    public void init(QrCode qrCode){
        if(qrCode == null){
            QrBitmap qrBitap = mQrCodeService.GenerateNewQrCode();
            qrCode = new QrCode("", "", qrBitap.getmUUID(), qrBitap.getBitmapBytes(), true);
            mCRUDQrCodeView.loadModel(qrCode);
            mCRUDQrCodeView.hideDeleteButton();
            mCRUDQrCodeView.hideUpdateButton();
            mCRUDQrCodeView.setActivityTitle(mStringRetreiver.getString(R.string.btn_create_qr_code));
        }
        else{
            if(qrCode.ismIsScanned()){
                mCRUDQrCodeView.hideCreateButton();
                mCRUDQrCodeView.hideUpdateButton();
                mCRUDQrCodeView.disableInputFields();
                mCRUDQrCodeView.setActivityTitle(mStringRetreiver.getString(R.string.text_scanned_code));
            }
            else{
                mCRUDQrCodeView.hideCreateButton();
                mCRUDQrCodeView.setActivityTitle(mStringRetreiver.getString(R.string.btn_update_qr_code));
            }
            mCRUDQrCodeView.loadModel(qrCode);
        }
    }

    @Override
    public void setView(CRUDQrCodeView view) {
        mCRUDQrCodeView = view;
    }
    @Override
    public void resume() {

    }
    @Override
    public void pause() {

    }
    @Override
    public void destroy() {
        mCRUDQrCodeView = null;
    }

    public void CreateNewQrCode(QrCode qr){
        boolean errorFound = validateUserInput(qr);
        if(errorFound == false) {
            mCRUDQrCodeView.showWaitDialog();
            mQrCodeService.InsertQRcode(qr, false).subscribe(id -> {
                if (mCRUDQrCodeView != null) {
                    mCRUDQrCodeView.hideWaitDialog();
                    if (id != -1) {
                        mCRUDQrCodeView.redirectToMyQrCodesAfterCreate();
                    } else {
                        mCRUDQrCodeView.showErrorMessageSnackBar(mStringRetreiver.getString(R.string.text_qr_code_not_saved));
                    }
                }
            }, e -> {
                mCRUDQrCodeView.hideWaitDialog();
                mCRUDQrCodeView.showErrorMessageSnackBar(mStringRetreiver.getString(R.string.text_qr_code_not_saved));
            });
        }
    }
    public void UpdateQrCode(QrCode qr){
        boolean errorFound = validateUserInput(qr);
        if(errorFound == false) {
            mCRUDQrCodeView.showWaitDialog();
            mQrCodeService.UpdateQrCode(qr).subscribe(count -> {
                if (mCRUDQrCodeView != null) {
                    mCRUDQrCodeView.hideWaitDialog();
                    if (count == 1) {
                        mCRUDQrCodeView.redirectToMyQrCodesAfterUpdate();
                    } else {
                        mCRUDQrCodeView.showErrorMessageSnackBar(mStringRetreiver.getString(R.string.text_qr_code_not_saved));
                    }
                }
            }, e -> {
                mCRUDQrCodeView.hideWaitDialog();
                mCRUDQrCodeView.showErrorMessageSnackBar(mStringRetreiver.getString(R.string.text_qr_code_not_saved));
            });
        }
    }
    public void DeleteQrCode(QrCode qr){
        mCRUDQrCodeView.showConfirmDeleteDialog(mStringRetreiver.getString(R.string.text_qr_code_delete_dialog_title),String.format(mStringRetreiver.getString(R.string.text_confirm_delete_qr_code), qr.getmTitle()));
    }
    public void DeleteConfirmed(QrCode qr){
        mCRUDQrCodeView.showWaitDialog();
        mQrCodeService.DeleteQrCode(qr.getmUuid()).subscribe(count -> {
            if (mCRUDQrCodeView != null) {
                mCRUDQrCodeView.hideWaitDialog();
                if (count == 1) {
                    mCRUDQrCodeView.redirectToMyQrCodesAfterDelete();
                } else {
                    mCRUDQrCodeView.showErrorMessageSnackBar(mStringRetreiver.getString(R.string.text_qr_code_not_deleted));
                }
            }
        }, e -> {
            mCRUDQrCodeView.hideWaitDialog();
            mCRUDQrCodeView.showErrorMessageSnackBar(mStringRetreiver.getString(R.string.text_qr_code_not_deleted));
        });
    }


    private boolean validateUserInput(QrCode qr) {
        boolean errorFound = false;

        if(qr.getmTitle() == null || qr.getmTitle().equals("")){
            mCRUDQrCodeView.showTitleError(mStringRetreiver.getString(R.string.empty_field_error_message));
            errorFound = true;
        }
        if(qr.getmTitle().length() > 30 || qr.getmTitle().length() > 30){
            mCRUDQrCodeView.showTitleError(String.format(mStringRetreiver.getString(R.string.text_too_long_message), 30));
            errorFound = true;
        }
        if(qr.getmDescription() == null || qr.getmDescription().equals("")){
            mCRUDQrCodeView.showDescriptionError(mStringRetreiver.getString(R.string.empty_field_error_message));
            errorFound = true;
        }
        if(qr.getmDescription().length() > 300 || qr.getmDescription().length() > 300){
            mCRUDQrCodeView.showDescriptionError(String.format(mStringRetreiver.getString(R.string.text_too_long_message), 300));
            errorFound = true;
        }
        return errorFound;
    }
}
