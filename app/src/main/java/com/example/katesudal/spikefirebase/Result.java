package com.example.katesudal.spikefirebase;

/**
 * Created by katesuda.l on 15/12/2559.
 */

public class Result {
    private String messageId;

    public Result(){}

    public Result(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
