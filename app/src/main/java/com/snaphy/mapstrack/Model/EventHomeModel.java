package com.snaphy.mapstrack.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Ravi-Gupta on 12/4/2015.
 */
public class EventHomeModel {

    private String id;
    private String eventId;
    private String eventAddress;
    private String description;
    private String type;
    private Date date;
    private ArrayList<SelectContactModel> contacts  = new ArrayList<SelectContactModel>();
    private HashMap<String,String> imageURL =  new HashMap<String, String>();
    private boolean isPrivate;
    private HashMap<String,Double> latLong = new HashMap<String, Double>();

    public static final String onSave = "EventHomeModel:onSave";
    public static final String onDelete = "EventHomeModel:onDelete";
    public static final String onResetData = "EventHomeModel:onReset";
    public static final String onChangeData = "EventHomeModel:onChange";
    public static final String onRemoveData = "EventHomeModel:onRemove";

    public EventHomeModel(String id, String eventId, String eventAddress, String description, String type,
                          Date date, ArrayList<SelectContactModel> contacts, HashMap<String,String> imageURL,
                          boolean isPrivate, HashMap<String,Double> latLong ){
        this.eventId = eventId;
        this.eventAddress = eventAddress;
        this.description = description;
        this.type = type;
        this.date = date;
        this.contacts = contacts;
        this.id = id;
        this.imageURL = imageURL;
        this.isPrivate = isPrivate;
        this.latLong = latLong;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<SelectContactModel> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<SelectContactModel> contacts) {
        this.contacts = contacts;
    }

    public HashMap<String, String> getImageURL() {
        return imageURL;
    }

    public void setImageURL(HashMap<String, String> imageURL) {
        this.imageURL = imageURL;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public HashMap<String, Double> getLatLong() {
        return latLong;
    }

    public void setLatLong(HashMap<String, Double> latLong) {
        this.latLong = latLong;
    }

}
