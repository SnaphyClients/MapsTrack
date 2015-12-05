package com.snaphy.mapstrack.Model;

/**
 * Created by Ravi-Gupta on 11/28/2015.
 */
public class ShareLocationModel {


    private String shared;
    private String contactName;

    public ShareLocationModel(String shared, String contactName) {
        this.shared  = shared;
        this.contactName = contactName;

    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getShared() {
        return shared;
    }

    public void setShared(String shared) {
        this.shared = shared;
    }

}
