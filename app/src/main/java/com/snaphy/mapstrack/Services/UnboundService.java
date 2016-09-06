package com.snaphy.mapstrack.Services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.snaphy.mapstrack.Constants;

/**
 * Created by Ravi-Gupta on 3/24/2016.
 */
public class UnboundService extends Service {

    private IBinder myBinder = new MyBinder();
    private PendingIntent pendingIntent;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        start();
        return myBinder;
    }


    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        /* Retrieve a PendingIntent that will perform a broadcast */
        Log.v(Constants.TAG,"Unbound service is running");
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }



    public void start() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), Constants.SET_LOCATION_UPDATE_TIMEOUT, pendingIntent);
    }

    public class MyBinder extends Binder {
        public UnboundService getService() {
            return UnboundService.this;
        }
    }

}
