package com.snaphy.mapstrack.Model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ravi-Gupta on 12/5/2015.
 */
public class LocationHomeModel {

    private String id;
    private String locationName;
    private String locationAddress;
    private String locationId;
    private ArrayList<SelectContactModel> contacts  = new ArrayList<SelectContactModel>();
    private HashMap<String,Double> latLong = new HashMap<String, Double>();

    public static final String onSave = "LocationHomeModel:onSave";
    public static final String onDelete = "LocationHomeModel:onDelete";
    public static final String onResetData = "LocationHomeModel:onReset";
    public static final String onChangeData = "LocationHomeModel:onChange";
    public static final String onRemoveData = "LocationHomeModel:onRemove";

    public LocationHomeModel(String id, String locationName, String locationAddress, String locationId,
                             ArrayList<SelectContactModel> contacts, HashMap<String,Double> latLong){
        this.locationName = locationName;
        this.locationAddress = locationAddress;
        this.locationId = locationId;
        this.contacts = contacts;
        this.id = id;
        this.latLong = latLong;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public HashMap<String, Double> getLatLong() {
        return latLong;
    }

    public void setLatLong(HashMap<String, Double> latLong) {
        this.latLong = latLong;
    }


}
