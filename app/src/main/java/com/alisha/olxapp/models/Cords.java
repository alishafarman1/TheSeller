package com.alisha.olxapp.models;

public class Cords {

    private double lat;
    private double lng;

    public Cords(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public Cords() {

    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
