package com.greg.ui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.greg.QrdbApplication;
import com.greg.domain.QrCodeService;
import com.greg.qrdb.R;

import java.util.Random;

import javax.inject.Inject;

/**
 * Created by Greg on 15-11-2016.
 */
public class QrWidget extends AppWidgetProvider {

    @Inject
    QrCodeService mQrCodeService;

    public QrWidget(){
        super();
        QrdbApplication.getInstance().getAppComponent().inject(this);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget);
            remoteViews.setTextViewText(R.id.tv_widget, String.format(context.getString(R.string.text_widget_info_text),mQrCodeService.GetScanCount() ));
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

}
