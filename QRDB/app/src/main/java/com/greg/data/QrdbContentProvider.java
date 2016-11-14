package com.greg.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Greg on 26-10-2016.
 */
public class QrdbContentProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final int CODES = 100;
    private static final int CODES_BY_ID = 200;

    private QrdbDbHelper mQrdbDbHelper;


    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = QrdbContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, QrdbContract.PATH_CODES, CODES);
        matcher.addURI(authority, QrdbContract.PATH_CODES + "/*", CODES_BY_ID);

        return matcher;
    }

    private static final String sCodeSelection =
            QrdbContract.CodeEntry.TABLE_NAME+
                    "." + QrdbContract.CodeEntry._ID + " = ? ";

    @Override
    public boolean onCreate() {
        mQrdbDbHelper = new QrdbDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "weather/*/*"
            case CODES: {
                retCursor = mQrdbDbHelper.getReadableDatabase().query(
                        QrdbContract.CodeEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case CODES_BY_ID:
            {
                retCursor = getFavoriteMovieById(uri, projection, sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    private Cursor getFavoriteMovieById(Uri uri, String[] projection, String sortOrder) {
        int id = QrdbContract.CodeEntry.getIdFromUri(uri);

        String[] selectionArgs;
        String selection;

        selectionArgs = new String[]{Integer.toString(id)};
        selection = sCodeSelection;


        return mQrdbDbHelper.getReadableDatabase().query(
                QrdbContract.CodeEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mQrdbDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case CODES: {
                long _id = db.insert(QrdbContract.CodeEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = QrdbContract.CodeEntry.buildCodeUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mQrdbDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        switch (match) {
            case CODES:
                rowsDeleted = db.delete(QrdbContract.CodeEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mQrdbDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated = 0;

        switch (match) {
            case CODES: {
                rowsUpdated = db.update(QrdbContract.CodeEntry.TABLE_NAME, contentValues,selection, selectionArgs);

                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if(rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
