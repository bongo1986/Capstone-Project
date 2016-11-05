package com.greg.domain;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

/**
 * Created by Greg on 04-11-2016.
 */
public class QrBitmap {
    private UUID mUUID;
    private Bitmap mBitmap;

    public QrBitmap(Bitmap mBitmap, UUID mUUID) {
        this.mBitmap = mBitmap;
        this.mUUID = mUUID;
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public byte[] getBitmapBytes(){
        if(mBitmap != null){
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            this.mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        }
        else{
            return null;
        }
    }

    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public UUID getmUUID() {
        return mUUID;
    }

    public void setmUUID(UUID mUUID) {
        this.mUUID = mUUID;
    }
}
