package com.snaphy.mapstrack.Model;

/**
 * Created by Ravi-Gupta on 12/24/2015.
 */
public class SelectContactModel {

    private String contactName;
    private String contactNumber;

    public SelectContactModel(String contactName, String contactNumber) {
        this.contactName = contactName;
        this.contactNumber = contactNumber;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

}
