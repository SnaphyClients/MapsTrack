package com.snaphy.mapstrack.Model;

import java.text.DateFormat;
import java.util.ArrayList;

/**
 * Created by Ravi-Gupta on 12/4/2015.
 */
public class EventHomeModel {

    private String id;
    private String eventId;
    private String eventAddress;
    private String description;
    private String type;
    private DateFormat date;
    private ArrayList<SelectContactModel> contacts  = new ArrayList<SelectContactModel>();

    public static final String onSave = "EventHomeModel:onSave";
    public static final String onDelete = "EventHomeModel:onDelete";
    public static final String onResetData = "EventHomeModel:onReset";
    public static final String onChangeData = "EventHomeModel:onChange";
    public static final String onRemoveData = "EventHomeModel:onRemove";


    public EventHomeModel(String eventId, String eventAddress, String description, String type, DateFormat date, ArrayList<SelectContactModel> contacts ){
        this.eventId = eventId;
        this.eventAddress = eventAddress;
        this.description = description;
        this.type = type;
        this.date = date;
        this.contacts = contacts;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public ArrayList<SelectContactModel> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<SelectContactModel> contacts) {
        this.contacts = contacts;
    }

}
