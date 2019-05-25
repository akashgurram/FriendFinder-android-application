package com.example.friendfinderakash;

import java.sql.Timestamp;

public class Users {

    private String name_;
    private NewLatLng location_;
    private String curTimeStamp_;

    public Users()
    {

    }


    public Users(String name_, NewLatLng location_, String curTimeStamp_){

        this.name_ =name_;
        this.location_ = location_;
        this.curTimeStamp_ = curTimeStamp_;
    }



    public String getName_() {
        return name_;
    }

    public void setName_(String name_) {
        this.name_ = name_;
    }

    public NewLatLng getLocation_() {
        return location_;
    }

    public void setLocation_(NewLatLng location_) {
        this.location_ = location_;
    }

    public String getCurTimeStamp() {
        return curTimeStamp_;
    }

    public void setCurTimeStamp(String curTimeStamp_) {
        this.curTimeStamp_ = curTimeStamp_;
    }
}
