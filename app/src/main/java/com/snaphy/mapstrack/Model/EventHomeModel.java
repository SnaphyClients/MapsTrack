package com.snaphy.mapstrack.Model;

import java.text.DateFormat;
import java.util.ArrayList;

/**
 * Created by Ravi-Gupta on 12/4/2015.
 */
public class EventHomeModel {

    private String eventId;
    private String eventAddress;
    private String description;
    private String type;
    private DateFormat date;
    private ArrayList<String> contacts  = new ArrayList<String>();


    public EventHomeModel(String eventId, String eventAddress, String description, String type, DateFormat date, ArrayList<String> contacts ){
        this.eventId = eventId;
        this.eventAddress = eventAddress;
        this.description = description;
        this.type = type;
        this.date = date;
        this.contacts = contacts;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventAddress() {
        return eventAddress;
    }

    public void setEventAddress(String eventAddress) {
        this.eventAddress = eventAddress;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DateFormat getDate() {
        return date;
    }

    public void setDate(DateFormat date) {
        this.date = date;
    }

    public ArrayList<String> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<String> contacts) {
        this.contacts = contacts;
    }

}
