package com.alisha.olxapp.models;

import com.google.firebase.auth.FirebaseUser;

public class Comment {
    private FirebaseUser user;
    private String comment;
    private String date;

    public Comment(FirebaseUser user, String comment, String date) {
        this.user = user;
        this.comment = comment;
        this.date = date;
    }

    public Comment(){

    }

    public FirebaseUser getUser() {
        return user;
    }

    public void setUser(FirebaseUser user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
