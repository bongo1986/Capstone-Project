package com.greg.presentation;


import com.greg.domain.QrCode;
import com.greg.domain.QrCodeService;

import javax.inject.Inject;

/**
 * Created by Greg on 02-11-2016.
 */
public class MyQrCodesPresenter  {

    private QrCodeService mQrCodeService;

    @Inject
    public MyQrCodesPresenter(QrCodeService qrSrv) {
        mQrCodeService = qrSrv;
    }

    public void CreateQrCode(QrCode qr){

    }


}
