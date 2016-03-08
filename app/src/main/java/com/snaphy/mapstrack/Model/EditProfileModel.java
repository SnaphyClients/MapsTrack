package com.snaphy.mapstrack.Model;

import android.graphics.drawable.Drawable;

/**
 * Created by Ravi-Gupta on 3/8/2016.
 */
public class EditProfileModel {

    private Drawable image;
    private String firstName;
    private String lastName;
    private String mobileNumber;

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
