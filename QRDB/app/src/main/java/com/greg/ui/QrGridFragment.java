package com.greg.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.greg.QrdbApplication;
import com.greg.data.QrdbContract;
import com.greg.presentation.QrGridPresenter;
import com.greg.presentation.QrGridView;
import com.greg.qrdb.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Greg on 06-11-2016.
 */
public class QrGridFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> , QrGridView {


    private QrCodesAdapter mAdapter;
    private QrCodeGridListener mQrCodeGridListener;
    private boolean mScannedOnly;
    private StaggeredGridLayoutManager mSglm =
            new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
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
        return v;
    }
    public void setScannedOnly(boolean val){

        mScannedOnly = val;
        getLoaderManager().initLoader(1, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        CursorLoader cursorLoader = new CursorLoader(getActivity() , QrdbContract.CodeEntry.CONTENT_URI, null,
                QrdbContract.CodeEntry.COLUMN_IS_SCANNED + " = ?" ,
                new String[] { mScannedOnly == true ? "1" : "0" }, null);
        return cursorLoader;
    }
    public void setQrCodeSelectedListener(QrCodeGridListener listener){
        mQrCodeGridListener = listener;
    }
    public void setNumberOfColumns(int numOfCols){
        mSglm = new StaggeredGridLayoutManager(numOfCols, StaggeredGridLayoutManager.VERTICAL);
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter = new QrCodesAdapter(getActivity() , data, mQrCodeGridListener);
        mQrCodesContainer.setLayoutManager(mSglm);
        mQrCodesContainer.setAdapter(mAdapter);
        if(data == null || data.getCount() == 0){
            mQrCodeGridListener.LoaderReady(0);
        }
        else{
            mQrCodeGridListener.LoaderReady(data.getCount());
        }
    }
    @Override
    public void onLoaderReset(Loader loader) {
        mQrCodesContainer.setAdapter(null);
    }




}
