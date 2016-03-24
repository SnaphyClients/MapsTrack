package com.snaphy.mapstrack.Services;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.androidsdk.snaphy.snaphyandroidsdk.models.Customer;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.CustomerRepository;
import com.snaphy.mapstrack.Constants;
import com.strongloop.android.loopback.callbacks.ObjectCallback;

/**
 * Created by Ravi-Gupta on 3/23/2016.
 */
public class LocationUpdaterService extends Service implements android.location.LocationListener {


    public static MyRestAdapter getRestAdapter() {
        return restAdapter;
    }

    public static void setRestAdapter(MyRestAdapter restAdapter) {
        LocationUpdaterService.restAdapter = restAdapter;
    }

    static MyRestAdapter restAdapter;

    public static Customer getCustomer() {
        return customer;
    }

    public static void setCustomer(Customer customer) {
        LocationUpdaterService.customer = customer;
    }

    private static Customer customer;

    private enum State {
        IDLE, WORKING;
    }


    private static State state;

    private LocationManager locationManager;
    private PowerManager.WakeLock wakeLock;

    static {
        state = State.IDLE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "LocationUpdaterService");
    }

    public MyRestAdapter getLoopBackAdapter() {
        if (restAdapter == null) {

            restAdapter = new MyRestAdapter(
                    getApplicationContext(),
                    Constants.apiUrl);
        }
        return restAdapter;
    }


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LocationUpdaterService.setRestAdapter(getLoopBackAdapter());
        checkLogin();
        /*if (state == State.IDLE) {*/
            state = State.WORKING;
            this.wakeLock.acquire();
            // register location updates
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            int permissionCheck1 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
            int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            if (permissionCheck1 == PackageManager.PERMISSION_GRANTED || permissionCheck2 == PackageManager.PERMISSION_GRANTED) {
                // you will want to listen for updates only once
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, 4000, 0, this);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        4000, 0, this);

            }

       /* }*/
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        state = State.IDLE;
        if (this.wakeLock.isHeld()) {
            this.wakeLock.release();
        }
    }

    private void sendToServer(Location location) {
        Log.v(Constants.TAG, location.getLatitude() + "");
        Log.v(Constants.TAG, LocationUpdaterService.getCustomer()+"");
        Log.v(Constants.TAG, LocationUpdaterService.getRestAdapter()+"");

        // send to server in background thread. you might want to start AsyncTask here
    }

    private void onSendingFinished() {
        // call this after sending finished to stop the service
        this.stopSelf(); //stopSelf will call onDestroy and the WakeLock releases.
        //Be sure to call this after everything is done (handle exceptions and other stuff) so you release a wakeLock
        //or you will end up draining battery like hell
    }

    @Override
    public void onLocationChanged(Location location) {
        int permissionCheck1 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionCheck1 == PackageManager.PERMISSION_GRANTED || permissionCheck2 == PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(this);
             // you will want to listen for updates only once
            sendToServer(location);
        }

    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public void checkLogin(){
        if(BackgroundService.getCustomerRepository() == null){
            CustomerRepository customerRepository = getLoopBackAdapter().createRepository(CustomerRepository.class);
            BackgroundService.setCustomerRepository(customerRepository);
        }

        // later in one of the Activity classes
        Customer current = BackgroundService.getCustomerRepository().getCachedCurrentUser();

        if (current != null) {

            LocationUpdaterService.setCustomer(current);
            //Move to home fragment
        } else {
            //SHOW PROGRESS DIALOG
            final ProgressDialog progress = new ProgressDialog(this);
            BackgroundService.getCustomerRepository().findCurrentUser(new ObjectCallback<Customer>() {
                @Override
                public void onSuccess(Customer object) {
                    //CLOSE PROGRESS DIALOG
                    progress.dismiss();
                    if(object != null){
                        LocationUpdaterService.setCustomer(object);
                        //Move to home fragment
                    }else{
                        // you have to login first
                        LocationUpdaterService.setCustomer(null);
                        //Register anonymous for push service..
                    }

                }

                @Override
                public void onError(Throwable t) {
                    //CLOSE PROGRESS DIALOG
                    progress.dismiss();
                    // you have to login first
                    LocationUpdaterService.setCustomer(null);
                    //Register anonymous for push service..
                }
            });

        }

    }


}