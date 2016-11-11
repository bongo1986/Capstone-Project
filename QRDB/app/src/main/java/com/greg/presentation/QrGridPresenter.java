package com.greg.presentation;

import com.greg.ui.QrGridFragment;

import java.util.UUID;

import javax.inject.Inject;

/**
 * Created by Greg on 08-11-2016.
 */
public class QrGridPresenter implements BasePresenter<QrGridView> {

    private QrGridView mQrGridView;

    @Inject
    public QrGridPresenter(){

    }

    @Override
    public void setView(QrGridView view) {
        mQrGridView = view;
    }


    @Override
    public void resume() {

    }
    @Override
    public void pause() {

    }
    @Override
    public void destroy() {
        mQrGridView = null;
    }
}
