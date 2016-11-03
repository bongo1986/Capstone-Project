package com.greg.qrdb;

import android.content.ContentValues;
import android.database.Cursor;

import com.greg.data.QrdbContract;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Greg on 28-10-2016.
 */
public class TestUtilities {
    static ContentValues createCodeValues(int index) {

        UUID uuid = UUID.randomUUID();

        ContentValues movieValues = new ContentValues();
        movieValues.put(QrdbContract.CodeEntry.COLUMN_DESCRIPTION, "Test description" + Integer.toString(index));
        movieValues.put(QrdbContract.CodeEntry.COLUMN_TITLE, "Test title" + Integer.toString(index));
        movieValues.put(QrdbContract.CodeEntry.COLUMN_QR_GUID, uuid.toString());
        movieValues.put(QrdbContract.CodeEntry.COLUMN_SCAN_COUNT, 0);
       // movieValues.put(QrdbContract.CodeEntry.COLUMN_QR_CODE_IMAGE_DATA, (byte[])null);

        return movieValues;
    }

}
