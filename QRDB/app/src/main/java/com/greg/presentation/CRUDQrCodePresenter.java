package com.greg.presentation;

import com.greg.domain.QrBitmap;
import com.greg.domain.QrCode;
import com.greg.domain.QrCodeService;
import com.greg.qrdb.R;
import com.greg.utils.StringRetreiver;
import com.greg.utils.ThreadProvider;

import javax.inject.Inject;

/**
 * Created by Greg on 03-11-2016.
 */
public class CRUDQrCodePresenter implements BasePresenter<CRUDQrCodeView>{

    private QrCodeService mQrCodeService;
    private CRUDQrCodeView mCRUDQrCodeView;
    private StringRetreiver mStringRetreiver;
    private ThreadProvider mThreadProvider;
    @Inject
    public CRUDQrCodePresenter(QrCodeService qrSrv, StringRetreiver stringRetreiver, ThreadProvider threadProvider) {
        mQrCodeService = qrSrv;
        mStringRetreiver = stringRetreiver;
        mThreadProvider = threadProvider;
    }

    public QrCode init(QrCode qrCode){
        if(qrCode == null){
            QrBitmap qrBitap = mQrCodeService.GenerateNewQrCode();
            qrCode = new QrCode("", "", qrBitap.getmUUID(), qrBitap.getBitmapBytes(), true, 0);
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

        return qrCode;
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
            mQrCodeService.InsertQRcode(qr, false, true)
                    .subscribeOn(mThreadProvider.newThread())
                    .observeOn(mThreadProvider.mainThread()).subscribe(id -> {
                if (mCRUDQrCodeView != null) {
                    mCRUDQrCodeView.hideWaitDialog();
                    if (id != -1) {
                        mCRUDQrCodeView.redirectToMyQrCodesAfterCreate();
                    } else {
                        mCRUDQrCodeView.showMessageSnackBar(mStringRetreiver.getString(R.string.text_qr_code_not_saved));
                    }
                }
            }, e -> {
                mCRUDQrCodeView.hideWaitDialog();
                mCRUDQrCodeView.showMessageSnackBar(mStringRetreiver.getString(R.string.text_qr_code_not_saved));
            });
        }
    }
    public void UpdateQrCode(QrCode qr){
        boolean errorFound = validateUserInput(qr);
        if(errorFound == false) {
            mCRUDQrCodeView.showWaitDialog();
            mQrCodeService.UpdateQrCode(qr, true)
                    .subscribeOn(mThreadProvider.newThread())
                    .observeOn(mThreadProvider.mainThread()).subscribe(count -> {
                if (mCRUDQrCodeView != null) {
                    mCRUDQrCodeView.hideWaitDialog();
                    if (count == 1) {
                        mCRUDQrCodeView.redirectToMyQrCodesAfterUpdate();
                    } else {
                        mCRUDQrCodeView.showMessageSnackBar(mStringRetreiver.getString(R.string.text_qr_code_not_saved));
                    }
                }
            }, e -> {
                mCRUDQrCodeView.hideWaitDialog();
                mCRUDQrCodeView.showMessageSnackBar(mStringRetreiver.getString(R.string.text_qr_code_not_saved));
            });
        }
    }
    public void DeleteQrCode(QrCode qr){
        mCRUDQrCodeView.showConfirmDeleteDialog(mStringRetreiver.getString(R.string.text_qr_code_delete_dialog_title),String.format(mStringRetreiver.getString(R.string.text_confirm_delete_qr_code), qr.getmTitle()));
    }
    public void DeleteConfirmed(QrCode qr){
        mCRUDQrCodeView.showWaitDialog();
        mQrCodeService.DeleteQrCode(qr, qr.ismIsScanned() == false)
                .subscribeOn(mThreadProvider.newThread())
                .observeOn(mThreadProvider.mainThread())
                .subscribe(count -> {
            if (mCRUDQrCodeView != null) {
                mCRUDQrCodeView.hideWaitDialog();
                if (count == 1) {
                    mCRUDQrCodeView.redirectToMyQrCodesAfterDelete();
                } else {
                    mCRUDQrCodeView.showMessageSnackBar(mStringRetreiver.getString(R.string.text_qr_code_not_deleted));
                }
            }
        }, e -> {
            mCRUDQrCodeView.hideWaitDialog();
            mCRUDQrCodeView.showMessageSnackBar(mStringRetreiver.getString(R.string.text_qr_code_not_deleted));
        });
    }


    private boolean validateUserInput(QrCode qr) {
        boolean errorFound = false;

        if(qr.getmTitle() == null || qr.getmTitle().trim().equals("")){
            mCRUDQrCodeView.showTitleError(mStringRetreiver.getString(R.string.empty_field_error_message));
            errorFound = true;
        }
        if(qr.getmTitle().length() > 30 || qr.getmTitle().length() > 30){
            mCRUDQrCodeView.showTitleError(String.format(mStringRetreiver.getString(R.string.text_too_long_message), 30));
            errorFound = true;
        }
        if(qr.getmDescription() == null || qr.getmDescription().trim().equals("")){
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
