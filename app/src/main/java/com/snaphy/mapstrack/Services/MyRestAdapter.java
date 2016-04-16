package com.snaphy.mapstrack.Services;

import android.content.Context;

import com.strongloop.android.loopback.RestAdapter;

/**
 * Created by Ravi-Gupta on 3/22/2016.
 */
public class MyRestAdapter extends RestAdapter {

    public static final int TIMEOUT = 25000;

    public MyRestAdapter(Context context, String url){
        super(context, url);
        setTimeout(TIMEOUT);

    }


    public void setTimeout(int timeoutMillis){
        getClient().setTimeout(timeoutMillis);
    }
}