package com.greg.domain;

import android.net.Uri;

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
    Long InsertQrCodeSync(QrCode code, boolean isScanned);
    Integer UpdateQrCodeSync(QrCode code);
    QrCode GetQrCodeForUuid(UUID uuid);
    //void Sc
    QrBitmap GenerateNewQrCode();
    Uri getUriToSharedFile(QrCode qr);
}
