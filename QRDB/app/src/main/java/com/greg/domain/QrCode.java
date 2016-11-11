package com.greg.domain;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Greg on 04-11-2016.
 */
public class QrCode implements Parcelable {
    private String mTitle;
    private String mDescription;
    private boolean mIsScanned;
    private UUID mUuid;
    private byte[] mQrBitmapData;

    public boolean ismIsScanned() {
        return mIsScanned;
    }

    public void setmIsScanned(boolean mIsScanned) {
        this.mIsScanned = mIsScanned;
    }

    public QrCode(String mDescription, String mTitle, UUID mUuid, byte[] mQrBitmapData, boolean mIsScanned) {
        this.mDescription = mDescription;
        this.mTitle = mTitle;
        this.mUuid = mUuid;
        this.mQrBitmapData = mQrBitmapData;
        this.mIsScanned = mIsScanned;

    }

    public byte[] getQrBitmapData() {
        return mQrBitmapData;
    }
    public Bitmap getQrBitmap(){
        Bitmap bitmap = null;
        if(mQrBitmapData != null){
            bitmap = BitmapFactory.decodeByteArray(mQrBitmapData, 0, mQrBitmapData.length);
        }
        return bitmap;
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

    @Override
    public int describeContents() {
        return 0;
    }

    protected QrCode(Parcel in) {
        mTitle = in.readString();
        mDescription = in.readString();
        mIsScanned = in.readInt() == 1;
        mUuid = UUID.fromString(in.readString());
        mQrBitmapData = new byte[in.readInt()];
        in.readByteArray(mQrBitmapData);
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(mTitle);
        dest.writeString(mDescription);
        dest.writeInt(mIsScanned == true ? 1 : 0);
        dest.writeString(mUuid.toString());
        dest.writeInt(mQrBitmapData.length);
        dest.writeByteArray(mQrBitmapData);
    }

    public static final Creator<QrCode> CREATOR = new Creator<QrCode>() {
        @Override
        public QrCode createFromParcel(Parcel in) {
            return new QrCode(in);
        }

        @Override
        public QrCode[] newArray(int size) {
            return new QrCode[size];
        }

    };
}



