package com.greg.domain;

import java.util.UUID;

import rx.Observable;

/**
 * Created by Greg on 03-11-2016.
 */
public interface QrCodeService {
    Observable<Long> InsertQRcode(QrCode code, boolean isScanned);
    Observable<Integer> DeleteQrCode(UUID uuid);
    Observable<Integer> UpdateQrCode(QrCode code);
    QrBitmap GetQrBitmapForUuid(UUID uuid);
    QrBitmap GenerateNewQrCode();
}
