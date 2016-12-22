package com.example.katesudal.spikefirebase.Model;

/**
 * Created by katesuda.l on 22/12/2559.
 */

public class User {
    private String email;


    public User(String email) {
        this.email = email;
    }
    public User(){

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
