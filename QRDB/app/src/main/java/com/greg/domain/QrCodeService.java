package com.greg.domain;

import rx.Observable;

/**
 * Created by Greg on 03-11-2016.
 */
public interface QrCodeService {
    Observable<Long> InsertQRcode(QrCode code);
    QrBitmap GenerateNewQrCode();
}
