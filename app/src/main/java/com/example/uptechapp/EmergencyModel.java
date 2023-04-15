package com.example.uptechapp;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class EmergencyModel {
    private String id;
    private String author;
    private String photoUrl;
    private String title;
    private String description;
    private Date time;
    private LatLng location;

    public EmergencyModel(String id, String author, String photoUrl, String title, String description, Date time, LatLng location) {
        this.id = id;
        this.author = author;
        this.photoUrl = photoUrl;
        this.title = title;
        this.description = description;
        this.time = time;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }
}
