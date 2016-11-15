package com.greg.domain;

import android.net.Uri;

import java.util.UUID;

import rx.Observable;

/**
 * Created by Greg on 03-11-2016.
 */
public interface QrCodeService {
    Observable<Long> InsertQRcode(QrCode code, boolean isScanned, boolean updateBackend);

    Observable<Integer> DeleteQrCode(QrCode code, boolean updateBacked);
    Observable<Integer> UpdateQrCode(QrCode code, boolean updateBacked);

    QrBitmap GetQrBitmapForUuid(UUID uuid);
    Long InsertQrCodeSync(QrCode code, boolean isScanned, boolean updateBacked);
    Integer UpdateQrCodeSync(QrCode code, boolean updateBacked);
    Integer UpdateScanCount(String uuid, int count);
    void SyncScanCounts();
    QrCode GetQrCodeForUuid(UUID uuid);
    //void Sc
    QrBitmap GenerateNewQrCode();
    Uri getUriToSharedFile(QrCode qr);
}
