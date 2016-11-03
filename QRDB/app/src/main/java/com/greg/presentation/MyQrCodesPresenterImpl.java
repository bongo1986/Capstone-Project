package com.greg.presentation;

import com.greg.dagger.IdummyClass;

import javax.inject.Inject;

/**
 * Created by Greg on 02-11-2016.
 */
public class MyQrCodesPresenterImpl implements MyQrCodesPresenter {

    private IdummyClass mDummy;

    @Inject
    public MyQrCodesPresenterImpl(IdummyClass dummy) {
        mDummy = dummy;
    }

    private int test;
}
