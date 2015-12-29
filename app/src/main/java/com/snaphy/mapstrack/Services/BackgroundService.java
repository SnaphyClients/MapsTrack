package com.snaphy.mapstrack.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.snaphy.mapstrack.Collection.EventHomeCollection;
import com.snaphy.mapstrack.Collection.LocationHomeCollection;

import org.simple.eventbus.EventBus;


/**
 * Created by Ravi-Gupta on 12/27/2015.
 */
public class BackgroundService extends Service {

    LocationHomeCollection locationHomeCollection;
    EventHomeCollection eventHomeCollection;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     *  This method is called to start the service from main Activity
     * @param intent {Intent}
     * @param flags {int}
     * @param startId {int}
     * @return int
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        EventBus.getDefault().registerSticky(this);
        eventHomeCollection = new EventHomeCollection();
        locationHomeCollection  = new LocationHomeCollection();
        return START_STICKY;
    }

    /**
     * This method is used to kill service or destroy service
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}