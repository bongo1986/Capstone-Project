package com.greg.qrdb;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.test.AndroidTestCase;

import com.greg.data.QrdbContentProvider;
import com.greg.data.QrdbContract;

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

        assertEquals("Error: Records not deleted from Favorite Movies table during delete", 0, cursor.getCount());
        cursor.close();

    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
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

    public void testGetType() {
        // content://com.example.greg.popularmovies/favoritemovie/
        String type = mContext.getContentResolver().getType(QrdbContract.CodeEntry.CONTENT_URI);
        //String type2 = mContext.getContentResolver().getType(PopularMoviesContract.MovieEntry.CONTENT_ITEM);


        assertEquals("Error: the Movie CONTENT_URI should return Movie.CONTENT_TYPE",
                QrdbContract.CodeEntry.CONTENT_TYPE, type);
    }

}
