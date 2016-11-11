package com.greg.presentation;

import com.greg.qrdb.R;
import com.greg.utils.StringRetreiver;

import javax.inject.Inject;

/**
 * Created by Greg on 08-11-2016.
 */
public class QrCodeListPresenter implements BasePresenter<QrCodeListView> {

    private QrCodeListView mQrCodeListView;
    private StringRetreiver mStringRetreiver;

    @Inject
    public QrCodeListPresenter(StringRetreiver sr){
        mStringRetreiver = sr;
    }

    @Override
    public void setView(QrCodeListView view) {
        mQrCodeListView = view;
    }

    public void afterAction(String msg){
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
                default:
                    break;
            }
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
