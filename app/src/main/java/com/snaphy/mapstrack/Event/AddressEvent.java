package com.snaphy.mapstrack.Event;

/**
 * Created by Ravi-Gupta on 12/23/2015.
 */
public class AddressEvent  {
    private final String address;

    public AddressEvent(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
