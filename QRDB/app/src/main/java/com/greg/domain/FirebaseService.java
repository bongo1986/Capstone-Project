package com.greg.domain;

import rx.Observable;

/**
 * Created by Greg on 12-11-2016.
 */
public interface FirebaseService {
    QrCode findQrCodeByUuid(String uuid);
    void insertQrCode(QrCode code) ;
    void updateQrCodeInfo(QrCode code);
    void updateQrCodeScanCount(QrCode code);
    void deleteQrCode(String uuid);
}
