package com.greg.presentation;

import com.greg.domain.QrCode;

/**
 * Created by Greg on 05-11-2016.
 */
public interface CRUDQrCodeView extends  WaitDialogView  {

    void showTitleError(String errorText);
    void showDescriptionError(String errorText);
    void redirectToMyQrCodesAfterCreate();
    void redirectToMyQrCodesAfterUpdate();
    void redirectToMyQrCodesAfterDelete();
    void hideUpdateButton();
    void hideCreateButton();
    void hideDeleteButton();
    void disableInputFields();
    void setActivityTitle(String title);
    void loadModel(QrCode qrCode);
    void showConfirmDeleteDialog(String title, String msg);
    void showErrorMessageSnackBar(String errorText);
}
