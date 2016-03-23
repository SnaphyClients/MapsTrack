package com.snaphy.mapstrack.Services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.SystemClock;

import com.snaphy.mapstrack.Constants;

/**
 * Created by Ravi-Gupta on 3/23/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final PendingIntent wakeupIntent = PendingIntent.getService(context, 0,
                new Intent(context, LocationUpdaterService.class), PendingIntent.FLAG_UPDATE_CURRENT);

        final boolean hasNetwork = !intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
        if (hasNetwork) {
            // start service now for doing once
            context.startService(new Intent(context, LocationUpdaterService.class));

            // schedule service for every 15 minutes
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + Constants.SET_LOCATION_UPDATE_TIMEOUT,
                    Constants.SET_LOCATION_UPDATE_TIMEOUT, wakeupIntent);
            /*alarmManager.set(AlarmManager.RTC_WAKEUP, Constants.SET_LOCATION_UPDATE_TIMEOUT, wakeupIntent);*/
        } else {
            alarmManager.cancel(wakeupIntent);
        }
    }
}
//http://javatechig.com/android/repeat-alarm-example-in-android
//http://stackoverflow.com/questions/20211227/i-want-to-post-location-updates-every-15mins-to-server-even-when-the-app-is-not