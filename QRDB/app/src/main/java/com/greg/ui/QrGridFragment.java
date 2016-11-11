package com.greg.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.greg.QrdbApplication;
import com.greg.data.QrdbContract;
import com.greg.domain.QrCode;
import com.greg.presentation.QrGridPresenter;
import com.greg.presentation.QrGridView;
import com.greg.qrdb.R;

import java.util.ArrayList;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Greg on 06-11-2016.
 */
public class QrGridFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> , QrGridView {


    private QrCodesAdapter mAdapter;
    private QrCodeSelectedListener mQrCodeSelectedListener;
    @Inject
    QrGridPresenter mQrGridPresenter;
    @BindView(R.id.qr_codes_grid)
    RecyclerView mQrCodesContainer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        QrdbApplication.getInstance().getAppComponent().inject(this);
        mQrGridPresenter.setView(this);
        //Toolbar toolbar = (Toolbar)  getView().findViewById(R.id.toolbar);
        View v = inflater.inflate(R.layout.content_qr_codes_grid, container, false);
        ButterKnife.bind(this, v);
        getLoaderManager().initLoader(1, null, this);
        return v;
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = new CursorLoader(getActivity() , QrdbContract.CodeEntry.CONTENT_URI, null,
                null, null, null);
        return cursorLoader;
    }
    public void setQrCodeSelectedListener(QrCodeSelectedListener listener){
        mQrCodeSelectedListener = listener;
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter = new QrCodesAdapter(getActivity() , data, mQrCodeSelectedListener);
        mQrCodesContainer.setAdapter(mAdapter);
        StaggeredGridLayoutManager sglm =
                new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mQrCodesContainer.setLayoutManager(sglm);
    }
    @Override
    public void onLoaderReset(Loader loader) {
        mQrCodesContainer.setAdapter(null);
    }




}
