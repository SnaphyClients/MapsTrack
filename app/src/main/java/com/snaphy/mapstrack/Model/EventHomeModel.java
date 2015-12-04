package com.snaphy.mapstrack.Model;

/**
 * Created by Ravi-Gupta on 12/4/2015.
 */
public class EventHomeModel {

    private String eventId;
    private String eventAddress;

    public EventHomeModel(String eventId, String eventAddress){
        this.eventId = eventId;
        this.eventAddress = eventAddress;
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

}
