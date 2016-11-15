package com.greg.domain;

import java.util.UUID;

/**
 * Created by Greg on 12-11-2016.
 */
public class DummyFirebaseServiceImpl implements FirebaseService {
    @Override
    public QrCode findQrCodeByUuid(String str) {
        return new QrCode("aaa","testDesc", UUID.randomUUID(),new byte[]{0,1},true, 0);
    }

    @Override
    public void insertQrCode(QrCode code) {

    }

    @Override
    public void updateQrCodeInfo(QrCode code) {

    }

    @Override
    public void updateQrCodeScanCount(QrCode code) {

    }

    @Override
    public void deleteQrCode(String uuid) {

    }
}
