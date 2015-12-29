package com.snaphy.mapstrack.Collection;

import android.util.Log;

import com.snaphy.mapstrack.Constants;
import com.snaphy.mapstrack.Model.LocationHomeModel;
import com.snaphy.mapstrack.Model.SelectContactModel;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;

/**
 * Created by Ravi-Gupta on 12/27/2015.
 */
public class LocationHomeCollection {

    ArrayList<LocationHomeModel> locationHomeModelArrayList = new ArrayList<LocationHomeModel>();

    public LocationHomeCollection() {
        EventBus.getDefault().registerSticky(this);
        EventBus.getDefault().register(this);
        initialize();
        EventBus.getDefault().post(new LocationHomeModel("Rob", "sdf", "sasa", new ArrayList<SelectContactModel>()), "test");
        EventBus.getDefault().post(new LocationHomeModel("Rob", "sdkkf", "sasa", new ArrayList<SelectContactModel>()), "");
    }

    public void initialize() {
        locationHomeModelArrayList.clear();
        /**
         *  TODO Fetch Data From Server
         */
        ArrayList<SelectContactModel> selectContactModels = new ArrayList<SelectContactModel>();
        selectContactModels.add(new SelectContactModel("Ravi Gupta","9873046993"));
        selectContactModels.add(new SelectContactModel("Robins Gupta","987389993"));
        selectContactModels.add(new SelectContactModel("Anurag Gupta","9873045632"));
        selectContactModels.add(new SelectContactModel("Siddarth Jain","8745646993"));
        //Remove this static code
        locationHomeModelArrayList.add(new LocationHomeModel("Ravi Home","DLF Phase 3","raviHome",selectContactModels));
        locationHomeModelArrayList.add(new LocationHomeModel("Sid House","Palam Vihar","sidHouse",selectContactModels));
        locationHomeModelArrayList.add(new LocationHomeModel("Anurag Office", "Sikandarpur Station", "officeOfficeDLF Phase 3", selectContactModels));
        locationHomeModelArrayList.add(new LocationHomeModel("Robin Home", "V Block, Gurgaon", "myHome", selectContactModels));

        EventBus.getDefault().postSticky(locationHomeModelArrayList, LocationHomeModel.onResetData);

    }


    @Subscriber(tag ="test")
    private void onSave_(LocationHomeModel locationHomeModel) {
        Log.i(Constants.TAG, "hey i am getting default.");
    }


    @Subscriber(tag = LocationHomeModel.onSave)
    private void onSave(LocationHomeModel locationHomeModel) {
        Log.i(Constants.TAG, "hey i am here.");
        if(locationHomeModel.getId() == null) { // New Data has been added in create location fragment
            locationHomeModelArrayList.add(new LocationHomeModel(locationHomeModel.getLocationName(),
                    locationHomeModel.getLocationAddress(), locationHomeModel.getLocationId(), locationHomeModel.getContacts()));
            /**
             *  TODO Send POST request to server
             */
            EventBus.getDefault().post(locationHomeModel, LocationHomeModel.onChangeData);
        }  else { // Data has been updated in create location fragment

            for(LocationHomeModel element : locationHomeModelArrayList) {
                if(element.getId() == locationHomeModel.getId()) {
                    int position = locationHomeModelArrayList.indexOf(element);
                    locationHomeModelArrayList.set(position, locationHomeModel);
                }
            }
            /**
             * TODO Send UPDATE request to server
             */
            EventBus.getDefault().postSticky(locationHomeModel, LocationHomeModel.onChangeData);
        }
    }



    @Subscriber(tag = LocationHomeModel.onDelete)
    private void onDelete(LocationHomeModel locationHomeModel) {
        for(LocationHomeModel element : locationHomeModelArrayList) {
            if(element.getId() == locationHomeModel.getId()) {
                int position = locationHomeModelArrayList.indexOf(element);
                locationHomeModelArrayList.remove(position);
            }
        }
        /**
         * TODO Send DELETE request to server
         */
        EventBus.getDefault().postSticky(locationHomeModel, LocationHomeModel.onRemoveData);
    }


}
