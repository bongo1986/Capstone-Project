package com.greg.ui;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.greg.QrdbApplication;
import com.greg.domain.QrCode;
import com.greg.presentation.CRUDQrCodePresenter;
import com.greg.presentation.CRUDQrCodeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import com.greg.qrdb.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import javax.inject.Inject;

/**
 * Created by Greg on 24-10-2016.
 */
public class CRUDQrCodeActivity extends BaseActivity implements CRUDQrCodeView {

    @BindView(R.id.input_layout_qr_code_title)
    TextInputLayout mLayoutQrCodeTitle;
    @BindView(R.id.input_layout_qr_code_description)
    TextInputLayout mLayoutQrCodeDescription;
    @BindView(R.id.qr_code_img)
    ImageView mQrCodeImage;
    @BindView(R.id.qr_code_title)
    EditText mEditTextTitle;
    @BindView(R.id.qr_code_description)
    EditText mEditTextDescription;
    @BindView(R.id.linear_layout_container)
    LinearLayout mLinearLayoutContainer;
    @BindView(R.id.btn_create_qr)
    Button mBtnCreate;
    @BindView(R.id.btn_update_qr)
    Button mBtnUpdate;
    @BindView(R.id.btn_delete_qr)
    Button mBtnDelete;
    @Inject
    CRUDQrCodePresenter mCRUDQrCodePresenter;

    private QrCode mQr;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_qr_code);
        initBaseActivity();
        QrdbApplication.getInstance().getAppComponent().inject(this);
        ButterKnife.bind(this);
        mCRUDQrCodePresenter.setView(this);

        Bundle b = getIntent().getExtras();
        if(b != null) {
            mQr = b.getParcelable("com.greg.domain.QrCode");
        }
        mCRUDQrCodePresenter.init(mQr);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(mQr.ismIsScanned() == false) {
            getMenuInflater().inflate(R.menu.menu_crud, menu);
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {

            Bitmap icon = mQr.getQrBitmap();
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/jpeg");

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "title");
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values);


            OutputStream outstream;
            try {
                outstream = getContentResolver().openOutputStream(uri);
                icon.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                outstream.close();
            } catch (Exception e) {
                System.err.println(e.toString());
            }

            share.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(share, "Share Qr code"));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onResume() {
        super.onResume();
        mCRUDQrCodePresenter.resume();
    }
    @Override
    public void onPause(){
        super.onPause();
        mCRUDQrCodePresenter.pause();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        mCRUDQrCodePresenter.setView(null);
        mCRUDQrCodePresenter.destroy();
    }

    @OnClick(R.id.btn_create_qr)
    public void CreateQrCodeBtnClicked() {
        mQr.setmDescription(mEditTextDescription.getText().toString());
        mQr.setmTitle(mEditTextTitle.getText().toString());
        mCRUDQrCodePresenter.CreateNewQrCode(mQr);
    }
    @OnClick(R.id.btn_update_qr)
    public void UpdateQrCodeBtnClicked() {
        mQr.setmDescription(mEditTextDescription.getText().toString());
        mQr.setmTitle(mEditTextTitle.getText().toString());
        mCRUDQrCodePresenter.UpdateQrCode(mQr);
    }
    @OnClick(R.id.btn_delete_qr)
    public void DeleteQrCodeBtnClicked() {
        mCRUDQrCodePresenter.DeleteQrCode(mQr);
    }
    @OnTextChanged(R.id.qr_code_title)
    public void QrTitleChanged() {
        mLayoutQrCodeTitle.setError(null);
    }
    @OnTextChanged(R.id.qr_code_description)
    public void QrDescriptionChanged() {
        mLayoutQrCodeDescription.setError(null);
    }
    @Override
    public void showTitleError(String errorText) {
        mLayoutQrCodeTitle.setError(errorText);
    }
    @Override
    public void showDescriptionError(String errorText) {
        mLayoutQrCodeDescription.setError(errorText);
    }

    @Override
    public void redirectToMyQrCodesAfterCreate() {
        finish();
        Intent myQrCodes = new Intent(this, QrCodeListActivity.class);
        myQrCodes.putExtra("AFTER ACTION", "CREATE" );
        startActivity(myQrCodes);
    }
    @Override
    public void redirectToMyQrCodesAfterUpdate(){
        finish();
        Intent myQrCodes = new Intent(this, QrCodeListActivity.class);
        myQrCodes.putExtra("AFTER ACTION", "UPDATE" );
        startActivity(myQrCodes);
    }
    @Override
    public  void redirectToMyQrCodesAfterDelete(){
        finish();
        Intent myQrCodes = new Intent(this, QrCodeListActivity.class);
        myQrCodes.putExtra("AFTER ACTION", "DELETE" );
        startActivity(myQrCodes);
    }
    @Override
    public void hideUpdateButton() {
        mBtnUpdate.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideCreateButton() {
        mBtnCreate.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideDeleteButton() {
        mBtnDelete.setVisibility(View.INVISIBLE);
    }

    @Override
    public void disableInputFields() {
        disableEditText(mEditTextDescription);
        disableEditText(mEditTextTitle);
    }

    @Override
    public void setActivityTitle(String title) {
        setTitle(title);
    }

    @Override
    public void loadModel(QrCode qrCode) {
        mEditTextTitle.setText(qrCode.getmTitle());
        mEditTextDescription.setText(qrCode.getmDescription());
        mQrCodeImage.setImageBitmap(qrCode.getQrBitmap());
        mQr = qrCode;
    }

    @Override
    public void showConfirmDeleteDialog(String title, String msg) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                       mCRUDQrCodePresenter.DeleteConfirmed(mQr);
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    @Override
    public void showErrorMessageSnackBar(String errorText) {
        super.showSnackBar(mLinearLayoutContainer,errorText);
    }

    @Override
    public void showWaitDialog() {
        progress = new ProgressDialog(this);
        progress.setMessage(getString(R.string.wait_dialog_message));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    @Override
    public void hideWaitDialog() {
        progress.hide();
    }

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }
}