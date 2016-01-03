package com.snaphy.mapstrack.Model;

import java.util.HashMap;

/**
 * Created by Ravi-Gupta on 11/28/2015.
 */
public class ShareLocationModel {


    private String id;
    private String contactName;
    private String contactNumber;
    private HashMap<String,Double> latLong = new HashMap<String, Double>();


    public ShareLocationModel(String id, String contactName, String contactNumber, HashMap<String,Double> latLong) {
        this.id  = id;
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this.latLong = latLong;

    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public HashMap<String, Double> getLatLong() {
        return latLong;
    }

    public void setLatLong(HashMap<String, Double> latLong) {
        this.latLong = latLong;
    }



}
