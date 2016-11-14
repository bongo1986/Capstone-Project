package com.greg.presentation;

import android.net.Uri;

import com.greg.domain.QrCode;
import com.greg.domain.QrCodeService;
import com.greg.qrdb.R;
import com.greg.utils.StringRetreiver;

import javax.inject.Inject;

/**
 * Created by Greg on 08-11-2016.
 */
public class QrCodeListPresenter implements BasePresenter<QrCodeListView> {

    private QrCodeListView mQrCodeListView;
    private StringRetreiver mStringRetreiver;
    private QrCodeService mQrCodeService;
    private boolean mIsTablet;
    private int mQrCodesCount;

    @Inject
    public QrCodeListPresenter(StringRetreiver sr, QrCodeService qrCodeService){
        mStringRetreiver = sr;
        mQrCodeService = qrCodeService;
    }

    @Override
    public void setView(QrCodeListView view) {
        mQrCodeListView = view;
    }

    public void init(String msg, boolean isTablet){
        mIsTablet = isTablet;
        if(msg != null) {
            switch (msg) {
                case "CREATE":
                    mQrCodeListView.SnackBar(mStringRetreiver.getString(R.string.text_qr_code_saved));
                    break;
                case "UPDATE":
                    mQrCodeListView.SnackBar(mStringRetreiver.getString(R.string.text_qr_code_updated));
                    break;
                case "DELETE":
                    mQrCodeListView.SnackBar(mStringRetreiver.getString(R.string.text_qr_code_deleted));
                    break;
                case "MENU_SCANNED_CODES":
                    mQrCodeListView.toggleAddButton(false);
                    mQrCodeListView.setTitle(mStringRetreiver.getString(R.string.scanned_qr_codes_menu_item));
                    break;
                default:
                    break;
            }
        }
        if(mIsTablet){
            mQrCodeListView.toggleNoElementSelectedMessage(true);
            mQrCodeListView.toggleQrCodesContainer(false);

        }
    }
    public void setQrCodesCount(int count){
        mQrCodesCount = count;
        if(count == 0){
            mQrCodeListView.showNoQrCodesMessage();
            mQrCodeListView.toggleNoElementSelectedMessage(false);
        }
    }
    public void qrCodeSelected(QrCode qr){
        if(mIsTablet) {
            mQrCodeListView.toggleNoElementSelectedMessage(false);
            mQrCodeListView.toggleQrCodesContainer(true);

        }
    }
    public void shareQrCode(QrCode qr){
        Uri uri = mQrCodeService.getUriToSharedFile(qr);
        if(uri != null){
            mQrCodeListView.sendShareIntent(uri);
        }
    }

    public void addButtonClicked(){
        mQrCodeListView.redirectToAddQrActivity();
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        mQrCodeListView = null;
    }
}
