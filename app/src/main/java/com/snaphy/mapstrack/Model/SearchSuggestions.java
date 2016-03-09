package com.snaphy.mapstrack.Model;

/**
 * Created by Ravi-Gupta on 3/9/2016.
 */
public class SearchSuggestions {

    private boolean isEvent;
    private String suggestion;

    public SearchSuggestions(boolean isEvent, String suggestion) {
        this.isEvent = isEvent;
        this.suggestion = suggestion;
    }

    public boolean isEvent() {
        return isEvent;
    }

    public void setIsEvent(boolean isEvent) {
        this.isEvent = isEvent;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }
}
