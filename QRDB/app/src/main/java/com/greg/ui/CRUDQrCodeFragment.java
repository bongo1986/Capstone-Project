package com.greg.ui;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.greg.QrdbApplication;
import com.greg.domain.QrCode;
import com.greg.presentation.CRUDQrCodePresenter;
import com.greg.presentation.CRUDQrCodeView;
import com.greg.qrdb.R;

import java.io.OutputStream;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by Greg on 11-11-2016.
 */
public class CRUDQrCodeFragment extends Fragment implements CRUDQrCodeView {


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        QrdbApplication.getInstance().getAppComponent().inject(this);
        mCRUDQrCodePresenter.setView(this);
        //Toolbar toolbar = (Toolbar)  getView().findViewById(R.id.toolbar);
        View v = inflater.inflate(R.layout.content_crud_qr_code, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    public void LoadQrCode(QrCode qr){
        mQr = mCRUDQrCodePresenter.init(qr);
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
            Uri uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values);


            OutputStream outstream;
            try {
                outstream = getActivity().getContentResolver().openOutputStream(uri);
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
        getActivity().finish();
        Intent myQrCodes = new Intent(getActivity(), QrCodeListActivity.class);
        myQrCodes.putExtra("AFTER ACTION", "CREATE" );
        startActivity(myQrCodes);
    }
    @Override
    public void redirectToMyQrCodesAfterUpdate(){
        getActivity().finish();
        Intent myQrCodes = new Intent(getActivity(), QrCodeListActivity.class);
        myQrCodes.putExtra("AFTER ACTION", "UPDATE" );
        startActivity(myQrCodes);
    }
    @Override
    public  void redirectToMyQrCodesAfterDelete(){
        getActivity().finish();
        Intent myQrCodes = new Intent(getActivity(), QrCodeListActivity.class);
        myQrCodes.putExtra("AFTER ACTION", "DELETE" );
        startActivity(myQrCodes);
    }
    @Override
    public void hideUpdateButton() {
        mBtnUpdate.setVisibility(View.GONE);
    }
    @Override
    public void hideCreateButton() {
        mBtnCreate.setVisibility(View.GONE);
    }
    @Override
    public void hideDeleteButton() {
        mBtnDelete.setVisibility(View.GONE);
    }
    @Override
    public void disableInputFields() {
        disableEditText(mEditTextDescription);
        disableEditText(mEditTextTitle);
    }

    @Override
    public void setActivityTitle(String title) {
       getActivity().setTitle(title);
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
        new AlertDialog.Builder(getActivity())
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
    public void showMessageSnackBar(String errorText) {
        showSnackBar(mLinearLayoutContainer,errorText);
    }

    @Override
    public void showWaitDialog() {
        progress = new ProgressDialog(getActivity());
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

    private void showSnackBar(View c, String errorText) {
        Snackbar snackbar = Snackbar.make(c, errorText, Snackbar.LENGTH_LONG);
        snackbar.show();
        View view = snackbar.getView();
        TextView txtv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
    }
}
