package com.greg.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.widget.ImageView;

import com.greg.QrdbApplication;
import com.greg.domain.QrCode;
import com.greg.presentation.QrCodeListPresenter;
import com.greg.presentation.QrCodeListView;
import com.greg.qrdb.R;

import java.util.UUID;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Greg on 06-11-2016.
 */
public class QrCodeListActivity extends BaseActivity implements QrCodeSelectedListener, QrCodeListView {

    QrGridFragment mQrGridFragment;

    @Inject
    QrCodeListPresenter mQrCodeListPresenter;

    @BindView(R.id.qr_coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_list);
        ButterKnife.bind(this);
        initBaseActivity();
        QrdbApplication.getInstance().getAppComponent().inject(this);
        mQrCodeListPresenter.setView(this);
        mQrGridFragment = (QrGridFragment)getSupportFragmentManager().findFragmentById(R.id.qr_grid_fragment);
        mQrGridFragment.setQrCodeSelectedListener(this);
        Intent intent = getIntent();
        String afterAction = intent.getStringExtra("AFTER ACTION");
        mQrCodeListPresenter.afterAction(afterAction);
    }

    @Override
    public void QrCodeSelected(QrCode qr, ImageView imageView) {
        //finish();
        Intent i = new Intent(this, CRUDQrCodeActivity.class);
        i.putExtra("com.greg.domain.QrCode", qr);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, imageView, getString(R.string.image_trans));
        this.startActivity(i, options.toBundle());
    }



    @Override
    public void SnackBar(String text) {
        showSnackBar(mCoordinatorLayout,text);
    }

    @OnClick(R.id.add_new_qr_code_fab)
    public void CreateQrCodeBtnClicked() {
        mQrCodeListPresenter.addButtonClicked();
    }

    @Override
    public void redirectToAddQrActivity() {
        finish();
        Intent create = new Intent(this, CRUDQrCodeActivity.class);
        startActivity(create);
    }

}
