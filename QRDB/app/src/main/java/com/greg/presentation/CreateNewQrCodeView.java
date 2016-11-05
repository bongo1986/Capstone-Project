package com.greg.presentation;

import com.greg.domain.QrCode;

/**
 * Created by Greg on 05-11-2016.
 */
public interface CreateNewQrCodeView extends  WaitDialogView  {
    void showQrCode(QrCode qr);
    void showTitleError(String errorText);
    void showDescriptionError(String errorText);
    void redirectToMyQrCodes(boolean afterSuccessfullCreate);
}
