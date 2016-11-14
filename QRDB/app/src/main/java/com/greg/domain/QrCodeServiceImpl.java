package com.greg.domain;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.greg.data.QrdbContract;
import com.greg.qrdb.R;

import java.io.OutputStream;
import java.util.UUID;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Greg on 03-11-2016.
 */
public class QrCodeServiceImpl implements QrCodeService {

    private Context mContext;

    public QrCodeServiceImpl(Context c){
        mContext = c;
    }
    public Observable<Long> InsertQRcode(QrCode code, boolean isScanned){
        Observable<Long> myObservable = Observable.create(
                new Observable.OnSubscribe<Long>() {
                    @Override
                    public void call(Subscriber<? super Long> sub) {

                        long rowId = InsertQrCodeSync(code, isScanned);
                        sub.onNext(rowId);
                        sub.onCompleted();

                    }
                }
        );
        return myObservable;
    }
    @Override
    public Observable<Integer> DeleteQrCode(UUID uuid) {
        Observable<Integer> myObservable = Observable.create(
                new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> sub) {
                        int rowsDeleted = mContext.getContentResolver().delete(
                                QrdbContract.CodeEntry.CONTENT_URI,
                                QrdbContract.CodeEntry.COLUMN_QR_GUID + " = ?" ,
                                new String[] { uuid.toString()});

                        sub.onNext(rowsDeleted);
                        sub.onCompleted();
                    }
                }
        );
        return myObservable;
    }
    @Override
    public Observable<Integer> UpdateQrCode(QrCode code) {
        Observable<Integer> myObservable = Observable.create(
                new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> sub) {

                        int rowsUpdated = UpdateQrCodeSync(code);

                        sub.onNext(rowsUpdated);
                        sub.onCompleted();
                    }
                }
        );
        return myObservable;
    }
    @Override
    public QrBitmap GetQrBitmapForUuid(UUID uuid) {
        return getQrBitmapForUuid(uuid);
    }
    @Override
    public Long InsertQrCodeSync(QrCode code, boolean isScanned) {
        ContentResolver cr = mContext.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(QrdbContract.CodeEntry.COLUMN_DESCRIPTION, code.getmDescription());
        values.put(QrdbContract.CodeEntry.COLUMN_TITLE, code.getmTitle());
        values.put(QrdbContract.CodeEntry.COLUMN_QR_GUID, code.getmUuid().toString());
        values.put(QrdbContract.CodeEntry.COLUMN_SCAN_COUNT, 0);
        values.put(QrdbContract.CodeEntry.COLUMN_IS_SCANNED, isScanned == true ? 1 : 0);
        values.put(QrdbContract.CodeEntry.COLUMN_QR_CODE_IMAGE_DATA, code.getQrBitmapData());
        Uri insertedUri = mContext.getContentResolver().insert( QrdbContract.CodeEntry.CONTENT_URI,values);
        return ContentUris.parseId(insertedUri);
    }
    @Override
    public Integer UpdateQrCodeSync(QrCode code) {
        ContentValues values = new ContentValues();
        values.put(QrdbContract.CodeEntry.COLUMN_DESCRIPTION, code.getmDescription());
        values.put(QrdbContract.CodeEntry.COLUMN_TITLE, code.getmTitle());


        return mContext.getContentResolver().update(
                QrdbContract.CodeEntry.CONTENT_URI,
                values,
                QrdbContract.CodeEntry.COLUMN_QR_GUID + " = ?" ,
                new String[] { code.getmUuid().toString()});
    }
    @Override
    public QrCode GetQrCodeForUuid(UUID uuid) {
        QrCode qr = null;
        Cursor mCursor = mContext.getContentResolver().query(
                QrdbContract.CodeEntry.CONTENT_URI,
                null,
                QrdbContract.CodeEntry.COLUMN_QR_GUID + " = ?" ,
                new String[] { uuid.toString()},
                null,
                null);

        if(mCursor != null && mCursor.getCount() == 1) {
            mCursor.moveToFirst();
            String title = mCursor.getString(mCursor.getColumnIndex(QrdbContract.CodeEntry.COLUMN_TITLE));
            String desc = mCursor.getString(mCursor.getColumnIndex(QrdbContract.CodeEntry.COLUMN_DESCRIPTION));
            String uuidStr = mCursor.getString(mCursor.getColumnIndex(QrdbContract.CodeEntry.COLUMN_QR_GUID));
            int isScanned = mCursor.getInt(mCursor.getColumnIndex(QrdbContract.CodeEntry.COLUMN_IS_SCANNED));
            byte[] imageData = mCursor.getBlob(mCursor.getColumnIndex(QrdbContract.CodeEntry.COLUMN_QR_CODE_IMAGE_DATA));
            qr = new QrCode(desc,title, UUID.fromString(uuidStr), imageData, isScanned == 1);
        }

        return  qr;
    }
    @Override
    public QrBitmap GenerateNewQrCode() {
        UUID uuid = UUID.randomUUID();
        return getQrBitmapForUuid(uuid);
    }
    @Override
    public Uri getUriToSharedFile(QrCode qr) {
        String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
        int res = mContext.checkCallingOrSelfPermission(permission);
        if (res == PackageManager.PERMISSION_GRANTED) {
            Bitmap icon = qr.getQrBitmap();
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/jpeg");

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "title");
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            Uri uri = mContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values);


            OutputStream outstream;
            try {
                outstream = mContext.getContentResolver().openOutputStream(uri);
                icon.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                outstream.close();
            } catch (Exception e) {
                System.err.println(e.toString());
            }


            return uri;
        }
        return null;
    }

    private QrBitmap getQrBitmapForUuid(UUID uuid) {
        QRCodeWriter writer = new QRCodeWriter();
        QrBitmap result = null;
        try {
            BitMatrix bitMatrix = writer.encode(uuid.toString(), BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            result = new QrBitmap(bmp, uuid);

        } catch (WriterException e) {
            e.printStackTrace();
        }

        return result;
    }
}
