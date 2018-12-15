package com.ap.pacyourcultureman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Random;

import static com.ap.pacyourcultureman.GameActivity.getBitmapFromDrawable;

public class Assignment {
    private String name,website, shortDescr, longDescr, imgUrl;
    private int id;
    private Double lat, lon;
    private LatLng latLng;
    private Circle circle;
    private Marker marker;
    private  int height;

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    private  int width;
    Bitmap house;


    public Assignment(int id, String name, String website, Double lat, Double lon, String shortDescr, String longDescr, String imageUrl) {
        this.name = name;
        this.id = id;
        this.website = website;
        this.lat = lat;
        this.lon = lon;
        this.shortDescr = shortDescr;
        this.longDescr = longDescr;
        this.imgUrl = imageUrl;
        latLng = new LatLng(this.lat, this.lon);
        this.height = 80;
        this.width = 80;
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
                }
        }
        return assignments.get(n);
    }

    public void DrawHouses(GoogleMap mMap, Context context,String name ){
        house  = getBitmapFromDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.house, null));
        Bitmap scaledHouse = Bitmap.createScaledBitmap(house, width, height, false);
        marker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(scaledHouse))
                .title(name));
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
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

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }


}
