package com.snaphy.mapstrack.Model;

/**
 * Created by Ravi-Gupta on 12/5/2015.
 */
public class LocationHomeModel {


    private String locationName;
    private String locationAddress;

    public LocationHomeModel(String locationName, String locationAddress){
        this.locationName = locationName;
        this.locationAddress = locationAddress;
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
}
