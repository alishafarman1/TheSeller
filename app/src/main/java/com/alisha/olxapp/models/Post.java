package com.alisha.olxapp.models;

import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Post implements Serializable {
    private String title;
    private String description;
    private String createdDate;
    private int price;
    private List<String> images;
    private String uid;
    private AppUser user;
    private Cords location;
    private String address;

    public Post(String title, String description, String createdDate, int price, ArrayList<String> images, String uid, AppUser user, Cords location, String address) {
        this.title = title;
        this.description = description;
        this.createdDate = createdDate;
        this.price = price;
        this.images = images;
        this.uid = uid;
        this.user = user;
        this.location = location;
        this.address = address;
    }



    public Post() {
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

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Cords getLocation() {
        return location;
    }

    public void setLocation(Cords location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
