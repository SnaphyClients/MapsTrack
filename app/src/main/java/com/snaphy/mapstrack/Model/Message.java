package com.snaphy.mapstrack.Model;

/**
 * Created by Ravi-Gupta on 3/9/2016.
 */
public class Message {
    public String message;
    public String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String orderId;
}
