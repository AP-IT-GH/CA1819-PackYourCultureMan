package com.ap.pacyourcultureman;

public class Assignment {
    String name,website, shortDescr, longDescr, imgUrl;
    Double lat, lon;
    public Assignment(String name,String website, Double lat, Double lon, String shortDescr, String longDescr, String imageUrl) {
        this.name = name;
        this.website = website;
        this.lat = lat;
        this.lon = lon;
        this.shortDescr = shortDescr;
        this.longDescr = longDescr;
        this.imgUrl = imageUrl;
    }
}
