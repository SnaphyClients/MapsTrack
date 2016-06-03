package com.snaphy.mapstrack.Model;

/**
 * Created by Ravi-Gupta on 5/31/2016.
 */
public class DateModel {

    public DateModel() {
        super();
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    private int day;
    private int month;
    private int year;
}
