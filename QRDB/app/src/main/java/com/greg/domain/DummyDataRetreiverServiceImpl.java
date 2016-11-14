package com.greg.domain;

import java.util.UUID;

/**
 * Created by Greg on 12-11-2016.
 */
public class DummyDataRetreiverServiceImpl implements DataRetreiverService {
    @Override
    public QrCode findScannedCode(String str) {
        return new QrCode("aaa","testDesc", UUID.randomUUID(),new byte[]{0,1},true);
    }
}
