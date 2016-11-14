package com.greg.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.greg.QrdbApplication;
import com.greg.domain.QrCode;
import com.greg.presentation.ScanPresenter;
import com.greg.presentation.ScanView;
import com.greg.qrdb.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Greg on 24-10-2016.
 */
public class ScanActivity extends BaseActivity implements ZXingScannerView.ResultHandler, ScanView {


    private boolean savingQrCode;
    private boolean showingSnackBar;
    @BindView(R.id.scanContainer)
    CoordinatorLayout mScanContainer;
    @Inject
    ScanPresenter mScanPresenter;
    private ZXingScannerView mZXingScanner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr_codes);
        initBaseActivity();

        QrdbApplication.getInstance().getAppComponent().inject(this);
        mScanPresenter.setView(this);
        ButterKnife.bind(this);

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mZXingScanner = new ZXingScannerView(this);
        contentFrame.addView(mZXingScanner);


    }




    @Override
    public void onResume() {
        super.onResume();
        mZXingScanner.setResultHandler(this); // Register ourselves as a handler for scan results.
        mZXingScanner.startCamera();

        // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mZXingScanner.stopCamera();
    }


    @Override
    public void handleResult(Result rawResult) {

        /*
        Context context = getApplicationContext();
        CharSequence text = "Hello toast!";
        int duration = Toast.LENGTH_SHORT;

        /*Toast toast = Toast.makeText(context, "Scanned successfully: " + rawResult.getText(), duration);
        toast.show();

        Log.v("QR", rawResult.getText()); // Prints scan results
        Log.v("QR", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
        */
        // If you would like to resume scanning, call this method below:
        if(savingQrCode == false) {
            savingQrCode = true;
            mScanPresenter.qrCodeScanned(rawResult.getText());
        }
        mZXingScanner.resumeCameraPreview(this);
    }

    @Override
    public void ShowErrorMessage(String message) {
        savingQrCode = false;
        if(showingSnackBar == false) {
            mScanContainer.postDelayed(new Runnable() {
                public void run() {
                    showingSnackBar = false;
                }
            }, 3000);
            showingSnackBar = true;
            showSnackBar(mScanContainer, message);
        }
    }

    @Override
    public void RedirectToQrCodeListAfterScanning(QrCode qr, String message) {
        savingQrCode = false;
        finish();
        Intent i = new Intent(this, CRUDQrCodeActivity.class);
        i.putExtra("MSG", message);
        i.putExtra("com.greg.domain.QrCode", qr);
        this.startActivity(i);
    }
}
