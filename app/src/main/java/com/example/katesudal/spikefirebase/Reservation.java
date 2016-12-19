package com.example.katesudal.spikefirebase;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by katesuda.l on 19/12/2559.
 */

public class Reservation {
    String userId;
    String roomId;

    public Reservation(String userId, String roomId) {
        this.userId = userId;
        this.roomId = roomId;
    }

    public Reservation(){}

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("roomId", roomId);

        return result;
    }
}
