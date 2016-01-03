package com.snaphy.mapstrack.Model;

/**
 * Created by Ravi-Gupta on 12/24/2015.
 */
public class SelectContactModel {

    private String id;
    private String contactName;
    private String contactNumber;

    public SelectContactModel(String id, String contactName, String contactNumber) {
        this.id = id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
