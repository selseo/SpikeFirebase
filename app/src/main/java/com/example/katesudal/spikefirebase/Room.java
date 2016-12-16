package com.example.katesudal.spikefirebase;

/**
 * Created by katesuda.l on 16/12/2559.
 */

public class Room {
    private int id;
    private String name;
    private int maxCapacity;
    private int floor;

    public Room(int floor, int id, int maxCapacity, String name) {
        this.floor = floor;
        this.id = id;
        this.maxCapacity = maxCapacity;
        this.name = name;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
