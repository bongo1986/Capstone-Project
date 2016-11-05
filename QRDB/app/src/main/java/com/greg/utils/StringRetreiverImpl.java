package com.greg.utils;

import android.content.Context;

/**
 * Created by Greg on 05-11-2016.
 */
public class StringRetreiverImpl implements StringRetreiver {

    private Context mContext;

    public StringRetreiverImpl(Context c) {
        mContext = c;
    }

    public String getString(int id){
        return mContext.getString(id);
    }
}
