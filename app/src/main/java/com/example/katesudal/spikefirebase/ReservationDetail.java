package com.example.katesudal.spikefirebase;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by katesuda.l on 19/12/2559.
 */

public class ReservationDetail {
    private int id;
    private String timeStamp;
    private String reservedDate;
    private String usedType;

    public ReservationDetail(int id, String reservedDate, String timeStamp, String usedType) {
        this.id = id;
        this.reservedDate = reservedDate;
        this.timeStamp = timeStamp;
        this.usedType = usedType;
    }

    public ReservationDetail(String reservedDate, String timeStamp, String usedType) {
        this.reservedDate = reservedDate;
        this.timeStamp = timeStamp;
        this.usedType = usedType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReservedDate() {
        return reservedDate;
    }

    public void setReservedDate(String reservedDate) {
        this.reservedDate = reservedDate;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
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
        result.put("timeStamp", timeStamp);
        result.put("reservedDate", reservedDate);
        result.put("usedType", usedType);

        return result;
    }
}
