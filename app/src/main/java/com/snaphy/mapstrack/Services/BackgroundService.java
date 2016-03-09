package com.snaphy.mapstrack.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.androidsdk.snaphy.snaphyandroidsdk.models.Customer;
import com.androidsdk.snaphy.snaphyandroidsdk.repository.CustomerRepository;
import com.snaphy.mapstrack.Collection.EventHomeCollection;
import com.snaphy.mapstrack.Collection.LocationHomeCollection;
import com.snaphy.mapstrack.Collection.ShareLocationCollection;
import com.strongloop.android.loopback.RestAdapter;

import org.simple.eventbus.EventBus;


/**
 * Created by Ravi-Gupta on 12/27/2015.
 */
public class BackgroundService extends Service {

    LocationHomeCollection locationHomeCollection;
    EventHomeCollection eventHomeCollection;
    ShareLocationCollection shareLocationCollection;
    static RestAdapter restAdapter;

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
        shareLocationCollection = new ShareLocationCollection();
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



    public static RestAdapter getLoopBackAdapter() {
        return restAdapter;
    }


    public static void setLoopBackAdapter(RestAdapter adapter){
        restAdapter = adapter;
    }
}
