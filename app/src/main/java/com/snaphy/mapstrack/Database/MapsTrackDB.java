package com.snaphy.mapstrack.Database;

import android.app.Application;

import com.activeandroid.ActiveAndroid;

/**
 * Created by Ravi-Gupta on 12/23/2015.
 */
public class MapsTrackDB extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }
}
