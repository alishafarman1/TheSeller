package com.alisha.olxapp.models;

public class PostImage {
    private String downloadUrl;

    public PostImage() {
    }

    public PostImage(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}

