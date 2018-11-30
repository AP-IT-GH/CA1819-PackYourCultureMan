package com.ap.pacyourcultureman;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Assignment {
    String name,website, shortDescr, longDescr, imgUrl;
    Double lat, lon;
    LatLng latLng;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getShortDescr() {
        return shortDescr;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public void setShortDescr(String shortDescr) {
        this.shortDescr = shortDescr;
    }

    public String getLongDescr() {
        return longDescr;
    }

    public void setLongDescr(String longDescr) {
        this.longDescr = longDescr;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Assignment(String name, String website, Double lat, Double lon, String shortDescr, String longDescr, String imageUrl) {
        this.name = name;
        this.website = website;
        this.lat = lat;
        this.lon = lon;
        this.shortDescr = shortDescr;
        this.longDescr = longDescr;
        this.imgUrl = imageUrl;

        latLng = new LatLng(this.lat, this.lon);
    }
}
