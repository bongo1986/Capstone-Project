package com.greg.qrdb;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.greg.data.QrdbContentProvider;
import com.greg.data.QrdbContract;
import com.greg.data.QrdbDbHelper;

import java.util.Map;
import java.util.Set;

/**
 * Created by Greg on 27-10-2016.
 */
public class QrdbContentProviderTests extends AndroidTestCase {

    public void deleteAllRecords() {
        mContext.getContentResolver().delete(
                QrdbContract.CodeEntry.CONTENT_URI,
                null,
                null
        );
        Cursor cursor = mContext.getContentResolver().query(
                QrdbContract.CodeEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        assertEquals("Error: Records not deleted from Code table during delete", 0, cursor.getCount());
        cursor.close();

    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        deleteAllRecords();

    }


    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();
        // We define the component name based on the package name from the context and the
        // WeatherProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                QrdbContentProvider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            assertEquals("Error: provider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + QrdbContract.CONTENT_AUTHORITY,
                    providerInfo.authority, QrdbContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {

            assertTrue("Error: provider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    public void testBasicCodeQuery() {


        QrdbDbHelper dbHelper = new QrdbDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createCodeValues(0);

        long rowId = db.insert(QrdbContract.CodeEntry.TABLE_NAME, null, testValues);

        assertTrue("Unable to Insert code into the Database", rowId  != -1);

        db.close();

        // Test the basic content provider query
        Cursor cursor = mContext.getContentResolver().query(
                QrdbContract.CodeEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        // Make sure we get the correct cursor out of the database
        validateCursor("testBasicCodeQuery", cursor, testValues);
    }
    public void testCodeByIdQuery() {
        // insert our test records into the database
        QrdbDbHelper dbHelper = new QrdbDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createCodeValues(1);

        long rowId = db.insert(QrdbContract.CodeEntry.TABLE_NAME, null, testValues);

        assertTrue("Unable to Insert PopularMovie into the Database", rowId  != -1);

        db.close();

        // Test the basic content provider query
        Cursor cursor = mContext.getContentResolver().query(
                QrdbContract.CodeEntry.CONTENT_URI,
                null,
                QrdbContract.CodeEntry._ID + " = ?" ,
                new String[]{ "3"},
                null
        );
        // Test the basic content provider query
        Cursor cursor2 = mContext.getContentResolver().query(
                QrdbContract.CodeEntry.CONTENT_URI,
                null,
                QrdbContract.CodeEntry._ID + " = ?" ,
                new String[]{ "1"},
                null
        );
        int count = cursor.getCount();

        assertTrue("Unable to Insert Code into the Database", count  == 0);
        validateCursor("testCodeByIdQuery", cursor2, testValues);
    }
    public void testScanCount() {
        // insert our test records into the database
        QrdbDbHelper dbHelper = new QrdbDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createCodeValues(1, 2);
        ContentValues testValues2 = TestUtilities.createCodeValues(1, 3);

        long rowId = db.insert(QrdbContract.CodeEntry.TABLE_NAME, null, testValues);
        long rowId2 = db.insert(QrdbContract.CodeEntry.TABLE_NAME, null, testValues2);

        assertTrue("Unable to Insert PopularMovie into the Database", rowId  != -1);

        db.close();

        // Test the basic content provider query
        Cursor cursor = mContext.getContentResolver().query(
                QrdbContract.CodeEntry.SCAN_COUNT_URI,
                null,
                null,
                null,
                null
        );
        // Test the basic content provider query

        int rowsCount = cursor.getCount();

        cursor.moveToNext();

        int scanCount = cursor.getInt(0);
        assertTrue("Unable to Insert Code into the Database", scanCount  == 5);
        //validateCursor("testCodeByIdQuery", cursor2, testValues);
    }

    public void testInsert() {

        // insert our test records into the database
        ContentValues testValues = TestUtilities.createCodeValues(1);
        Uri insertedUri = mContext.getContentResolver().insert( QrdbContract.CodeEntry.CONTENT_URI,testValues);
        long rowId = ContentUris.parseId(insertedUri);
        assertTrue("Unable to Insert PopularMovie into the Database", rowId  != -1);

    }

    public void testUpdate() {

        // insert our test records into the database
        ContentValues testValues = TestUtilities.createCodeValues(1);
        Uri insertedUri = mContext.getContentResolver().insert( QrdbContract.CodeEntry.CONTENT_URI,testValues);


        long rowId = ContentUris.parseId(insertedUri);

        ContentValues values = new ContentValues();

        values.put(QrdbContract.CodeEntry.COLUMN_DESCRIPTION, "Test description updated");
        values.put(QrdbContract.CodeEntry.COLUMN_TITLE, "Test title updated");


        int updated_count  = mContext.getContentResolver().update(QrdbContract.CodeEntry.CONTENT_URI,values, QrdbContract.CodeEntry._ID + " = ?" ,new String[] { String.valueOf(rowId)});


        assertTrue("Unable to Insert PopularMovie into the Database", rowId  != -1);

        assertEquals("Error: Records not updated", 1, updated_count);

    }

    public void testDelete() {

        // insert our test records into the database
        ContentValues testValues = TestUtilities.createCodeValues(1);
        Uri insertedUri = mContext.getContentResolver().insert( QrdbContract.CodeEntry.CONTENT_URI,testValues);
        long rowId = ContentUris.parseId(insertedUri);
        assertTrue("Unable to Insert PopularMovie into the Database", rowId  != -1);

        mContext.getContentResolver().delete(
                QrdbContract.CodeEntry.CONTENT_URI,
                QrdbContract.CodeEntry._ID + " = ?" ,
                new String[] { Long.toString(rowId)});

        Cursor cursor = mContext.getContentResolver().query(
                QrdbContract.CodeEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        assertEquals("Error: Records not deleted from Code table during delete", 0, cursor.getCount());
        cursor.close();

    }


    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }
    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue.toString(), valueCursor.getString(idx));
        }
    }
}
