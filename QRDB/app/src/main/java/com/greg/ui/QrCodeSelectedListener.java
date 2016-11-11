package com.greg.ui;


import android.widget.ImageView;

import com.greg.domain.QrCode;

/**
 * Created by Greg on 06-11-2016.
 */
public interface QrCodeSelectedListener {
    void QrCodeSelected(QrCode m, ImageView imageView);
}
