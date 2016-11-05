package com.greg.domain;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.UUID;

/**
 * Created by Greg on 04-11-2016.
 */
public class QrCode {
    private String mTitle;
    private String mDescription;
    private UUID mUuid;
    private byte[] mQrBitmapData;

    public QrCode(String mDescription, String mTitle, UUID mUuid, byte[] mQrBitmapData) {
        this.mDescription = mDescription;
        this.mTitle = mTitle;
        this.mUuid = mUuid;
        this.mQrBitmapData = mQrBitmapData;
    }

    public byte[] getQrBitmapData() {
        return mQrBitmapData;
    }
    public Bitmap getQrBitmap(){
        if(mQrBitmapData != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(mQrBitmapData, 0, mQrBitmapData.length);
            return bitmap;
        }
        else{
            return null;
        }
    }
    public void setQrBitmapData(byte[] qrBitmapData) {
        this.mQrBitmapData = qrBitmapData;
    }
    public UUID getmUuid() {
        return mUuid;
    }

    public void setmUuid(UUID mUuid) {
        this.mUuid = mUuid;
    }
    public String getmDescription() {
        return mDescription;
    }
    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }
    public String getmTitle() {
        return mTitle;
    }
    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }
}
