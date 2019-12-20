package com.alisha.olxapp.models;

public class AppUser {
    private String name;
    private String email;
    private String phone;
    private String id;
    private String image;

    public AppUser(String name, String email, String phone, String id, String image) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.id = id;
        this.image = image;
    }

    public AppUser() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
