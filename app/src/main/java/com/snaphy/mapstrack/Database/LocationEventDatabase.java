/*
package com.snaphy.mapstrack.Database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

*/
/**
 * Created by Ravi-Gupta on 12/24/2015.
 *//*

@Table(name = "LocationEvent")
public class LocationEventDatabase extends Model {

    // If name is omitted, then the field name is used.
    @Column(name = "Name")
    public String name;

    @Column(name = "LocationId")
    public String locationId;

    @Column(name = "Address")
    public String address;


    public LocationEventDatabase() {
        super();
    }

    public LocationEventDatabase(String name, String locationId, String address) {
        super();
        this.name = name;
        this.locationId = locationId;
        this.address = address;
    }

    public List<LocationContactDatabase> contactDatabases() {
        return getMany(LocationContactDatabase.class, "LocId");
    }
}

*/
