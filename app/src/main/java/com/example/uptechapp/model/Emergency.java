package com.example.uptechapp.model;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.SimpleTimeZone;

public class Emergency {
    private String id;
    private String photoUrl;
    private String title;
    private String description;
    private String time;
    private String locationSTR;
    private LatLng location;


    public Emergency(String id, String title, String description, String time, String photoUrl, String locationSTR) {
        this.id = id;
        this.photoUrl = photoUrl;
        this.title = title;
        this.description = description;
        this.time = time;
        this.locationSTR = locationSTR;
        String[] latlong =  locationSTR.split(",");
        double latitude = Double.parseDouble(latlong[0]);
        double longitude = Double.parseDouble(latlong[1]);
        Log.d("TENSHI", "Emergency1: " + locationSTR);
        location = new LatLng(latitude, longitude);
        Log.d("TENSHI", "Emergency2: " + location);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Emergency{" +
                "id='" + id + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", time='" + time + '\'' +
                ", locationSTR='" + locationSTR + '\'' +
                ", location=" + location +
                '}';
    }
}
