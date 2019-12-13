package com.alisha.olxapp.models;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class Post {
    private String title;
    private String description;
    private String createdDate;
    private int price;
    private ArrayList<String> images;
    private ArrayList<Integer> imagesDemo;
    private String uid;
    private FirebaseUser user;
    private ArrayList<Comment> comments;
    private Cords location;
    private String address;

    public Post(String title, String description, String createdDate, int price, ArrayList<String> images, String uid, FirebaseUser user, ArrayList<Comment> comments, Cords location, String address) {
        this.title = title;
        this.description = description;
        this.createdDate = createdDate;
        this.price = price;
        this.images = images;
        this.uid = uid;
        this.user = user;
        this.comments = comments;
        this.location = location;
        this.address = address;
    }

    public Post(String title, String description, String createdDate, int price, ArrayList<Integer> imagesDemo) {
        this.title = title;
        this.description = description;
        this.createdDate = createdDate;
        this.price = price;
        this.imagesDemo = imagesDemo;
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

    public ArrayList<String> getImages() {
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

    public FirebaseUser getUser() {
        return user;
    }

    public void setUser(FirebaseUser user) {
        this.user = user;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public ArrayList<Integer> getImagesDemo() {
        return imagesDemo;
    }

    public void setImagesDemo(ArrayList<Integer> imagesDemo) {
        this.imagesDemo = imagesDemo;
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
