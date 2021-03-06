package com.greg.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.greg.QrdbApplication;
import com.greg.data.QrdbContract;
import com.greg.domain.QrCode;
import com.greg.presentation.QrCodeListPresenter;
import com.greg.presentation.QrCodeListView;
import com.greg.qrdb.R;

import java.io.OutputStream;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Greg on 06-11-2016.
 */
public class QrCodeListActivity extends BaseActivity implements QrCodeGridListener, QrCodeListView {

    QrGridFragment mQrGridFragment;
    CRUDQrCodeFragment mCrudFragment;
    QrCode mQr;

    MenuItem shareMenuItem;

    @Inject
    QrCodeListPresenter mQrCodeListPresenter;

    @Inject
    FirebaseAnalytics mFirebaseAnalytics;

    @BindView(R.id.qr_coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.tv_no_codes)
    TextView mNoQrCodesMsg;

    @BindView(R.id.tv_no_qr_code_selected)
    TextView mNoQrCodeSelectedMsg;

    @BindView(R.id.qr_details_container)
    FrameLayout mQrDetailsContainer;

    @BindView(R.id.add_new_qr_code_fab)
    FloatingActionButton mAddButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_list);
        ButterKnife.bind(this);
        initBaseActivity();
        QrdbApplication.getInstance().getAppComponent().inject(this);
        mQrCodeListPresenter.setView(this);
        Intent intent = getIntent();
        String afterAction = intent.getStringExtra("AFTER ACTION");
        boolean onlyScanned = intent.getBooleanExtra("SCANNED_ONLY", false);
        mCrudFragment = (CRUDQrCodeFragment)getSupportFragmentManager().findFragmentById(R.id.qr_crud_fragment);
        mQrGridFragment = (QrGridFragment)getSupportFragmentManager().findFragmentById(R.id.qr_grid_fragment);
        mQrGridFragment.setScannedOnly(onlyScanned);
        if(mCrudFragment != null){
            mQrGridFragment.setNumberOfColumns(1);
        }
        mQrGridFragment.setQrCodeSelectedListener(this);

        Bundle bundle = new Bundle();
        bundle.putString("SCREEN", "QR CODES LIST");
        mFirebaseAnalytics.logEvent("SCREEN",bundle );

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        UUID guid = UUID.randomUUID();

       /* mDatabase.child(guid.toString()).child("title").setValue("title AAAAA_BBBB");
        mDatabase.child(guid.toString()).child("description").setValue("description AAAAA_BBBB");


        Query query = mDatabase.child(guid.toString());

        ValueEventListener valueEventListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                String t = dataSnapshot.child("title").getValue().toString();
                String d = dataSnapshot.child("description").getValue().toString();
                mDatabase.child(guid.toString()).child("description").setValue(d + "_updated");
                mDatabase.child(guid.toString()).child("title").setValue(t + "_updated");
                query.removeEventListener(this);
                dataSnapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        };

        query.addValueEventListener(valueEventListener);
        //query.removeEventListener();
        query.keepSynced(true);*/


        mQrCodeListPresenter.init(afterAction, mCrudFragment != null);
    }

    private Account createDummyAccount(Context context) {
        Account dummyAccount = new Account("dummyaccount", "com.greg");
        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);
        accountManager.addAccountExplicitly(dummyAccount, null, null);
        ContentResolver.setSyncAutomatically(dummyAccount, QrdbContract.CONTENT_AUTHORITY, true);
        return dummyAccount;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ContentResolver.requestSync(createDummyAccount(this),  QrdbContract.CONTENT_AUTHORITY, Bundle.EMPTY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(mCrudFragment != null) {
            getMenuInflater().inflate(R.menu.menu_crud, menu);
            shareMenuItem = menu.findItem(R.id.action_share);
            shareMenuItem.setVisible(false);
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            mQrCodeListPresenter.shareQrCode(mQr);
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onStop() {
        super.onStop();
        finish();
    }
    @Override
    public void QrCodeSelected(QrCode qr, ImageView imageView) {
        //finish();

        if(mCrudFragment == null) {
            Intent i = new Intent(this, CRUDQrCodeActivity.class);
            i.putExtra("com.greg.domain.QrCode", qr);
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(this, imageView, getString(R.string.image_trans));
            this.startActivity(i, options.toBundle());

        }
        else{
            shareMenuItem.setVisible(true);
            mCrudFragment.LoadQrCode(qr);
            mQrCodeListPresenter.qrCodeSelected(qr);
            mQr = qr;
        }
    }
    @Override
    public void LoaderReady(int elementsCount) {
        mQrCodeListPresenter.setQrCodesCount(elementsCount);
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

    @Override
    public void showNoQrCodesMessage() {
        mNoQrCodesMsg.setVisibility(View.VISIBLE);
    }
    @Override
    public void toggleNoElementSelectedMessage(boolean show) {
        if(show){
            mNoQrCodeSelectedMsg.setVisibility(View.VISIBLE);
        }
        else{
            mNoQrCodeSelectedMsg.setVisibility(View.GONE);
        }
    }
    @Override
    public void toggleQrCodesContainer(boolean show) {
        if(show){
            mQrDetailsContainer.setVisibility(View.VISIBLE);
        }
        else{
            mQrDetailsContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void toggleAddButton(boolean show) {
        mAddButton.setVisibility(View.GONE);
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title);
    }

    @Override
    public void sendShareIntent(Uri uri) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, "Share Qr code"));
    }
}
