package com.snaphy.mapstrack.Event;

import com.snaphy.mapstrack.Model.SelectContactModel;

import java.util.ArrayList;

/**
 * Created by Ravi-Gupta on 12/24/2015.
 */
public class LocationContactEvent {

    private String locationName;
    private String locationAddress;
    private String locationId;
    private ArrayList<SelectContactModel> contacts  = new ArrayList<SelectContactModel>();

    public LocationContactEvent(String locationName, String locationAddress, String locationId, ArrayList<SelectContactModel> contacts){
        this.locationName = locationName;
        this.locationAddress = locationAddress;
        this.locationId = locationId;
        this.contacts = contacts;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }
    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public ArrayList<SelectContactModel> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<SelectContactModel> contacts) {
        this.contacts = contacts;
    }


}
