package com.example.katesudal.spikefirebase;


public class SendNotiRequest {

    private Notification notification;
    private String to;

    public SendNotiRequest() {
    }

    public SendNotiRequest(Notification notification, String to) {
        super();
        this.notification = notification;
        this.to = to;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}