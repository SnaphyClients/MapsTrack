package com.snaphy.mapstrack.Model;

import com.androidsdk.snaphy.snaphyandroidsdk.models.LastUpdatedLocation;

/**
 * Created by Ravi-Gupta on 3/3/2016.
 */
public class ContactModel {

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public LastUpdatedLocation getLastUpdatedLocation() {
        return lastUpdatedLocation;
    }

    public void setLastUpdatedLocation(LastUpdatedLocation lastUpdatedLocation) {
        this.lastUpdatedLocation = lastUpdatedLocation;
    }




    private String contactNumber;
    private String contactName;
    private boolean isSelected;
    LastUpdatedLocation lastUpdatedLocation;
}
