package com.snaphy.mapstrack.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.snaphy.mapstrack.Collection.LocationHomeCollection;
import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.Model.LocationHomeModel;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;


/**
 * Created by Ravi-Gupta on 12/27/2015.
 */
public class BackgroundService extends Service {

    ArrayList<LocationHomeModel> locationHomeModelArrayList = new ArrayList<LocationHomeModel>();
    LocationHomeCollection locationHomeCollection;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        EventBus.getDefault().registerSticky(this);
        // Let it continue running until it is stopped.
        locationHomeCollection  = new LocationHomeCollection();
        Log.v(Constants.TAG, "Service is Started");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        Log.v(Constants.TAG, "Service is Stopped");
    }
}
