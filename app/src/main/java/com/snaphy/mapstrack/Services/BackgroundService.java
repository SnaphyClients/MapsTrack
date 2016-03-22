package com.snaphy.mapstrack.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.androidsdk.snaphy.snaphyandroidsdk.models.Customer;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.CustomerRepository;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;
import com.snaphy.mapstrack.Collection.EventHomeCollection;
import com.snaphy.mapstrack.Collection.EventTypeCollection;
import com.snaphy.mapstrack.Collection.LocationHomeCollection;
import com.snaphy.mapstrack.Collection.ShareLocationCollection;
import com.snaphy.mapstrack.Collection.TrackCollection;
import com.strongloop.android.loopback.RestAdapter;

import org.simple.eventbus.EventBus;


/**
 * Created by Ravi-Gupta on 12/27/2015.
 */
public class BackgroundService extends Service {

    LocationHomeCollection locationHomeCollection;
    EventHomeCollection eventHomeCollection;
    ShareLocationCollection shareLocationCollection;
    EventTypeCollection eventTypeCollection;
    static RestAdapter restAdapter;


    public static LatLng getCurrentLocation() {
        return currentLocation;
    }

    public static void setCurrentLocation(LatLng currentLocation) {
        BackgroundService.currentLocation = currentLocation;
    }

    private static LatLng currentLocation;

    public static String getAddress() {
        return address;
    }

    public static void setAddress(String address) {
        BackgroundService.address = address;
    }

    private static String address;

    static public TrackCollection getTrackCollection() {
        return trackCollection;
    }

    static TrackCollection trackCollection;

    public static String getAccessToken() {
        return accessToken;
    }

    public static void setAccessToken(String accessToken) {
        BackgroundService.accessToken = accessToken;
    }

    static String accessToken;


    private static CustomerRepository customerRepository;

    public static CustomerRepository getCustomerRepository() {
        return customerRepository;
    }

    public static void setCustomerRepository(CustomerRepository customerRepository) {
        BackgroundService.customerRepository = customerRepository;
    }

    private static Customer customer;

    public static Customer getCustomer() {
        return customer;
    }

    public static void setCustomer(Customer customer) {
        BackgroundService.customer = customer;
    }

    public static GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    public static void setGoogleApiClient(GoogleApiClient googleApiClient) {
        BackgroundService.googleApiClient = googleApiClient;
    }

    private static GoogleApiClient googleApiClient;

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
        //eventHomeCollection = new EventHomeCollection();
        //locationHomeCollection  = new LocationHomeCollection();
        //shareLocationCollection = new ShareLocationCollection();

        proceedNextStep();
        return START_NOT_STICKY;
    }


    private void proceedNextStep(){
        trackCollection = new TrackCollection(getLoopBackAdapter(), getApplicationContext());
    }

    /**
     * This method is used to kill service or destroy service
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }



    public static RestAdapter getLoopBackAdapter() {
        return restAdapter;
    }


    public static void setLoopBackAdapter(RestAdapter adapter){
        restAdapter = adapter;
    }
}
