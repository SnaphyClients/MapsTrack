package com.snaphy.mapstrack.Model;

import android.content.Intent;

/**
 * Created by Ravi-Gupta on 2/17/2016.
 */
public class ImageResponse {

    public ImageResponse() {

    }

    int request;
    int response;
    Intent data;

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }

    public Intent getData() {
        return data;
    }

    public void setData(Intent data) {
        this.data = data;
    }

    public int getRequest() {
        return request;
    }

    public void setRequest(int request) {
        this.request = request;
    }

}
