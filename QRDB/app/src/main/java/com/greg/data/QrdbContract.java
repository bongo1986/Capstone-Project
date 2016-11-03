package com.greg.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Greg on 25-10-2016.
 */
public class QrdbContract {
    public static final String CONTENT_AUTHORITY = "com.greg.qrdb";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_CODES = "code";


    public static final class CodeEntry implements BaseColumns {

        public static int getIdFromUri(Uri uri) {
            return Integer.parseInt(uri.getPathSegments().get(1));
        }

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CODES ).build();

        public static Uri buildCodeUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }




        public static final String TABLE_NAME = "code";

        //public static final String COLUMN_CODE_ID = "code_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_QR_GUID = "qr_guid";
        public static final String COLUMN_SCAN_COUNT = "scan_count";
        public static final String COLUMN_QR_CODE_IMAGE_DATA = "qr_code_image_data";
    }


}
