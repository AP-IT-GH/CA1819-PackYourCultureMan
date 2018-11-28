package com.ap.pacyourcultureman;

import com.google.android.gms.maps.model.LatLng;

public class Assignment {
    String name,website, shortDescr, longDescr, imgUrl;
    Double lat, lon;
    LatLng latLng;
    public Assignment(String name,String website, Double lat, Double lon, String shortDescr, String longDescr, String imageUrl) {
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
