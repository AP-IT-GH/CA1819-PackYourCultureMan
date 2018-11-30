package com.ap.pacyourcultureman;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Assignment {
    String name,website, shortDescr, longDescr, imgUrl;
    Double lat, lon;
    LatLng latLng;
    Circle circle;
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
    public Assignment getRandomAssignment(Context context, GoogleMap mMap, Assignment currentAssigment, List<Assignment> assignments, List<Circle> circles){
        Random rand = new Random();
        int n = rand.nextInt(assignments.size());
        if(assignments.get(n) == currentAssigment && currentAssigment != null) {
            n = rand.nextInt(assignments.size());
        }
        Log.d("Random", Integer.toString(n));
            for (int i = 0; i < circles.size(); i++) {
                circles.get(i).remove();
                Log.d("REMOVE", Integer.toString(i));
        }
        circles.clear();
        if(this.circle != null) {
                this.circle.remove();
                Log.d("RemoveMain", "fff");
        }
        for(int i = 0; i < assignments.size(); i++) {
                if(n == i) {
                    CircleOptions circleOptionsCurrentAssignment = new CircleOptions();
                    circleOptionsCurrentAssignment.center(new LatLng(assignments.get(n).lat, assignments.get(n).lon));
                    circleOptionsCurrentAssignment.radius(10);
                    circleOptionsCurrentAssignment.strokeColor(Color.YELLOW);
                    circleOptionsCurrentAssignment.fillColor(0x30ff0000);
                    circleOptionsCurrentAssignment.strokeWidth(2);
                    this.circle = mMap.addCircle(circleOptionsCurrentAssignment);
                    Log.d("CurrentAssignmentWriter", "ff");
                }
                else {
                    CircleOptions circleOptionsSafeZone = new CircleOptions();
                    circleOptionsSafeZone.center(new LatLng(assignments.get(i).lat, assignments.get(i).lon));
                    circleOptionsSafeZone.radius(10);
                    circleOptionsSafeZone.strokeColor(Color.GREEN);
                    circleOptionsSafeZone.fillColor(0x30ff0000);
                    circleOptionsSafeZone.strokeWidth(2);
                    Circle circleSafezone = mMap.addCircle(circleOptionsSafeZone);
                    circles.add(circleSafezone);
                    Log.d("SafeZonerWriter", Integer.toString(i));
                }
        }
        return assignments.get(n);
    }
}
