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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.greg.data.QrdbContract;
import com.greg.qrdb.R;

import java.io.OutputStream;
import java.util.UUID;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Greg on 03-11-2016.
 */
public class QrCodeServiceImpl implements QrCodeService {

    private Context mContext;
    private FirebaseService mFirebaseService;

    public QrCodeServiceImpl(Context c, FirebaseService fs){
        mFirebaseService = fs;
        mContext = c;
    }
    public Observable<Long> InsertQRcode(QrCode code, boolean isScanned, boolean updateBacked){
        Observable<Long> myObservable = Observable.create(
                new Observable.OnSubscribe<Long>() {
                    @Override
                    public void call(Subscriber<? super Long> sub) {
                        long rowId = InsertQrCodeSync(code, isScanned, updateBacked);
                        sub.onNext(rowId);
                        sub.onCompleted();

                    }
                }
        );
        return myObservable;
    }
    @Override
    public Observable<Integer> DeleteQrCode(QrCode code, boolean updateBacked) {
        Observable<Integer> myObservable = Observable.create(
                new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> sub) {
                        if(isOnline() == false){
                            sub.onNext(0);
                            sub.onCompleted();
                        }
                        else {
                            if (updateBacked) {
                                mFirebaseService.deleteQrCode(code.getmUuid().toString());
                            }

                            int rowsDeleted = mContext.getContentResolver().delete(
                                    QrdbContract.CodeEntry.CONTENT_URI,
                                    QrdbContract.CodeEntry.COLUMN_QR_GUID + " = ?",
                                    new String[]{code.getmUuid().toString()});

                            sub.onNext(rowsDeleted);
                            sub.onCompleted();
                        }
                    }
                }
        );
        return myObservable;
    }
    @Override
    public Observable<Integer> UpdateQrCode(QrCode code, boolean updateBacked) {
        Observable<Integer> myObservable = Observable.create(
                new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> sub) {

                        int rowsUpdated = UpdateQrCodeSync(code, updateBacked);

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
    public Long InsertQrCodeSync(QrCode code, boolean isScanned, boolean updateBacked) {
        if(isOnline() == false){
            return new Long(-1);
        }
        if(updateBacked){
            mFirebaseService.insertQrCode(code);
        }
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
    public Integer UpdateQrCodeSync(QrCode code, boolean updateBacked) {
        if(isOnline() == false){
            return 0;
        }
        if(updateBacked){
            mFirebaseService.updateQrCodeInfo(code);
        }
        ContentValues values = new ContentValues();
        values.put(QrdbContract.CodeEntry.COLUMN_DESCRIPTION, code.getmDescription());
        values.put(QrdbContract.CodeEntry.COLUMN_TITLE, code.getmTitle());
        values.put(QrdbContract.CodeEntry.COLUMN_SCAN_COUNT, code.getmScanCount());

        return mContext.getContentResolver().update(
                QrdbContract.CodeEntry.CONTENT_URI,
                values,
                QrdbContract.CodeEntry.COLUMN_QR_GUID + " = ?" ,
                new String[] { code.getmUuid().toString()});
    }



    @Override
    public Integer UpdateScanCount(String uuid, int count) {
        ContentValues values = new ContentValues();
        values.put(QrdbContract.CodeEntry.COLUMN_SCAN_COUNT, count);

        return mContext.getContentResolver().update(
                QrdbContract.CodeEntry.CONTENT_URI,
                values,
                QrdbContract.CodeEntry.COLUMN_QR_GUID + " = ? and " + QrdbContract.CodeEntry.COLUMN_IS_SCANNED +" = 0" ,
                new String[] { uuid});
    }

    @Override
    public int GetScanCount() {
        int scanCount = 0;
        Cursor cursor = mContext.getContentResolver().query(
                QrdbContract.CodeEntry.SCAN_COUNT_URI,
                null,
                null,
                null,
                null
        );
        // Test the basic content provider query
        int rowsCount = cursor.getCount();
        if(rowsCount > 0){
            cursor.moveToNext();
            scanCount = cursor.getInt(0);
        }
        return scanCount;
    }

    @Override
    public void SyncScanCounts() {

            Cursor cursor = mContext.getContentResolver().query(
                                QrdbContract.CodeEntry.CONTENT_URI,
                                null,
                                QrdbContract.CodeEntry.COLUMN_IS_SCANNED + " = ?",
                                new String[] { "0" },
                                null
                        );

                        if (cursor.moveToFirst()){
                            do{
                                String uuid = cursor.getString(cursor.getColumnIndex(QrdbContract.CodeEntry.COLUMN_QR_GUID));
                                if(isOnline() == true){
                                    QrCode backend_code = mFirebaseService.findQrCodeByUuid(uuid);
                                    if(backend_code != null){
                                        UpdateScanCount(uuid, backend_code.getmScanCount());
                                    }
                                }
                                // do what ever you want here
                            }while(cursor.moveToNext());
                        }
                        cursor.close();



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
            int scan_count = mCursor.getInt(mCursor.getColumnIndex(QrdbContract.CodeEntry.COLUMN_SCAN_COUNT));
            int isScanned = mCursor.getInt(mCursor.getColumnIndex(QrdbContract.CodeEntry.COLUMN_IS_SCANNED));
            byte[] imageData = mCursor.getBlob(mCursor.getColumnIndex(QrdbContract.CodeEntry.COLUMN_QR_CODE_IMAGE_DATA));
            qr = new QrCode(desc,title, UUID.fromString(uuidStr), imageData, isScanned == 1, scan_count);
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

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
