package com.example.katesudal.spikefirebase.Model;

/**
 * Created by katesuda.l on 16/12/2559.
 */

public class Room {
    private String name;
    private int maxCapacity;
    private int floor;

    @Override
    public String toString() {
        return "Room{" +
                "name='" + name + '\'' +
                ", maxCapacity=" + maxCapacity +
                ", floor=" + floor +
                '}';
    }

    public Room() {
    }

    public Room(int floor, int maxCapacity, String name) {
        this.floor = floor;
        this.maxCapacity = maxCapacity;
        this.name = name;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
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
