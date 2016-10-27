package com.greg.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Greg on 25-10-2016.
 */
public class QrdbDbHelper extends SQLiteOpenHelper {

    private static final  int DATABASE_VERSION = 1;
    public static String DATABASE_NAME = "qrdb_database.db";

    public QrdbDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + QrdbContract.CodeEntry.TABLE_NAME + " (" +
                QrdbContract.CodeEntry._ID + " INTEGER PRIMARY KEY," +
                QrdbContract.CodeEntry.COLUMN_TITLE + " TEXT  NOT NULL, " +
                QrdbContract.CodeEntry.COLUMN_DESCRIPTION + " TEXT  NOT NULL, " +
                QrdbContract.CodeEntry.COLUMN_QR_CODE_IMAGE_DATA + " BLOB " +
                " );";
        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + QrdbContract.CodeEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
