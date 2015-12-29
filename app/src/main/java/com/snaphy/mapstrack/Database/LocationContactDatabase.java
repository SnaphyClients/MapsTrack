package com.snaphy.mapstrack.Database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by Ravi-Gupta on 12/24/2015.
 */
@Table(name = "LocationContact")
public class LocationContactDatabase extends Model {

    // If name is omitted, then the field name is used.
    @Column(name = "Name")
    public String name;

    @Column(name = "Number")
    public String number;

    @Column(name = "LocId")
    public LocationEventDatabase locId;

    public LocationContactDatabase() {
        super();
    }

    public LocationContactDatabase(LocationEventDatabase locId, String name, String number) {
        super();
        this.name = name;
        this.number = number;
        this.locId = locId;
    }

    public static List<LocationContactDatabase> getAll(LocationEventDatabase locationEventDatabase) {
        return new Select()
                .from(LocationContactDatabase.class)
                .where("LocId = ?", locationEventDatabase.getId())
                .orderBy("Name ASC")
                .execute();
    }


}
