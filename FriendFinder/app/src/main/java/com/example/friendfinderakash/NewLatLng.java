package com.example.friendfinderakash;

public class NewLatLng {

    private Double latitude;
    private Double longitude;

    public NewLatLng()
    {

    }

    public NewLatLng(double lat_, double lng_ ){

        latitude = lat_;
        longitude = lng_;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
