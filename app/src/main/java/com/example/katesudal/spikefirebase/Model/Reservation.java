package com.example.katesudal.spikefirebase.Model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by katesuda.l on 19/12/2559.
 */

public class Reservation {
    private String userId;
    private String roomId;
    private String timeStamp;
    private String reservedDate;
    private String usedType;

    public Reservation(String userId, String roomId, String timeStamp, String reservedDate, String usedType) {
        this.userId = userId;
        this.roomId = roomId;
        this.timeStamp = timeStamp;
        this.reservedDate = reservedDate;
        this.usedType = usedType;
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

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getReservedDate() {
        return reservedDate;
    }

    public void setReservedDate(String reservedDate) {
        this.reservedDate = reservedDate;
    }

    public String getUsedType() {
        return usedType;
    }

    public void setUsedType(String usedType) {
        this.usedType = usedType;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("roomId", roomId);
        result.put("timeStamp", timeStamp);
        result.put("reservedDate", reservedDate);
        result.put("usedType", usedType);

        return result;
    }
}
