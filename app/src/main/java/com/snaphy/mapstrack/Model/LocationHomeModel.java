package com.snaphy.mapstrack.Model;

import java.util.ArrayList;

/**
 * Created by Ravi-Gupta on 12/5/2015.
 */
public class LocationHomeModel {


    private String locationName;
    private String locationAddress;
    private String locationId;
    private ArrayList<String> contacts  = new ArrayList<String>();

    public LocationHomeModel(String locationName, String locationAddress, String locationId, ArrayList<String> contacts){
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

    public ArrayList<String> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<String> contacts) {
        this.contacts = contacts;
    }
}
