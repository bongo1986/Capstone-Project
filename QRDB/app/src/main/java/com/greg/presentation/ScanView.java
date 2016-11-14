package com.greg.presentation;

import com.greg.domain.QrCode;

/**
 * Created by Greg on 12-11-2016.
 */
public interface ScanView {
    void ShowErrorMessage(String message);
    void RedirectToQrCodeListAfterScanning(QrCode qr, String message);
}
