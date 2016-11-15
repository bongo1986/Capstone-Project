package com.greg.presentation;


import com.greg.domain.FirebaseService;
import com.greg.domain.QrBitmap;
import com.greg.domain.QrCode;
import com.greg.domain.QrCodeService;
import com.greg.qrdb.R;
import com.greg.utils.StringRetreiver;
import com.greg.utils.ThreadProvider;

import java.util.UUID;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Greg on 12-11-2016.
 */
public class ScanPresenter implements BasePresenter<ScanView>  {

    ScanView mScanView;
    FirebaseService mFirebaseService;
    QrCodeService mQrCodeService;
    StringRetreiver mStringRetreiver;
    ThreadProvider mThreadProvider;

    private class ScanningResult{
        public QrCode QR;
        public boolean isExisting;
    }
    @Inject
    public ScanPresenter(FirebaseService mFirebaseService, QrCodeService mQrCodeService, StringRetreiver mStringRetreiver, ThreadProvider mThreadProvider){
        this.mFirebaseService = mFirebaseService;
        this.mQrCodeService = mQrCodeService;
        this.mStringRetreiver = mStringRetreiver;
        this.mThreadProvider = mThreadProvider;
    }

    @Override
    public void setView(ScanView view) {
        mScanView = view;
    }

    public void qrCodeScanned(String scanningString){

        Observable<ScanningResult> myObservable = Observable.create(
                new Observable.OnSubscribe<ScanningResult>() {
                    @Override
                    public void call(Subscriber<? super ScanningResult> sub) {
                        boolean isUuidValid = true;
                        try{
                            UUID uuid = UUID.fromString(scanningString);
                        } catch (IllegalArgumentException exception){
                            isUuidValid = false;
                            sub.onNext(null);
                            sub.onCompleted();
                        }
                        if(isUuidValid) {
                            QrCode qr = mFirebaseService.findQrCodeByUuid(scanningString);
                            if (qr == null) {
                                sub.onNext(null);
                                sub.onCompleted();
                            } else {
                                int scan_count = qr.getmScanCount();
                                scan_count++;
                                qr.setmScanCount(scan_count);
                                mFirebaseService.updateQrCodeScanCount(qr);

                                QrBitmap qrBitap = mQrCodeService.GetQrBitmapForUuid(qr.getmUuid());
                                qr.setQrBitmapData(qrBitap.getBitmapBytes());

                                QrCode existing = mQrCodeService.GetQrCodeForUuid(qr.getmUuid());
                                if(existing != null && existing.getmUuid().equals(qr.getmUuid()) && existing.ismIsScanned() == false){
                                    sub.onNext(null);
                                    sub.onCompleted();
                                }
                                else if (existing != null && existing.ismIsScanned() == true) {
                                    ScanningResult r = new ScanningResult();
                                    r.isExisting = true;
                                    mQrCodeService.UpdateQrCodeSync(qr, false);
                                    r.QR = qr;
                                    sub.onNext(r);
                                    sub.onCompleted();
                                } else {
                                    ScanningResult r = new ScanningResult();
                                    r.isExisting = false;
                                    mQrCodeService.InsertQrCodeSync(qr,true, false);
                                    r.QR = qr;
                                    sub.onNext(r);
                                    sub.onCompleted();
                                }
                            }
                        }
                    }
                }
        );
        myObservable
                .subscribeOn(mThreadProvider.newThread())
                .observeOn(mThreadProvider.mainThread())
                .subscribe(result -> {
            if(result == null){
                mScanView.ShowErrorMessage(mStringRetreiver.getString(R.string.text_code_not_found));
            }
            else if(result.isExisting){
                mScanView.RedirectToQrCodeListAfterScanning(result.QR, mStringRetreiver.getString(R.string.text_existing_qr));
            }
            else
            {
                mScanView.RedirectToQrCodeListAfterScanning(result.QR, mStringRetreiver.getString(R.string.text_new_qr_code_saved));
            }
        }, e -> {
            mScanView.ShowErrorMessage(mStringRetreiver.getString(R.string.text_code_not_found));
        });


    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }
}
