package com.greg.presentation;

import android.net.Uri;

/**
 * Created by Greg on 08-11-2016.
 */
public interface QrCodeListView {
    void SnackBar(String text);
    void redirectToAddQrActivity();
    void showNoQrCodesMessage();
    void toggleNoElementSelectedMessage(boolean show);
    void toggleQrCodesContainer(boolean show);
    void toggleAddButton(boolean show);
    void setTitle(String title);
    void sendShareIntent(Uri uri);
}
