package com.example.katesudal.spikefirebase.Model;


public class SendNotificationRequest {

    private Notification notification;
    private String to;

    public SendNotificationRequest() {
    }

    public SendNotificationRequest(Notification notification, String to) {
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