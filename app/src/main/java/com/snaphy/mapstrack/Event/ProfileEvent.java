package com.snaphy.mapstrack.Event;

/**
 * Created by Ravi-Gupta on 12/23/2015.
 */
public class ProfileEvent {

    private final String name;
    private final String emailId;
    private final String pictureUrl;

    public ProfileEvent(String name, String emailId, String pictureUrl) {
        this.name = name;
        this.emailId = emailId;
        this.pictureUrl = pictureUrl;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getName() {
        return name;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

}
