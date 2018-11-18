package com.ap.pacyourcultureman;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.res.ResourcesCompat;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.ap.pacyourcultureman.GameActivity.getBitmapFromDrawable;

public class Dot {
    private LatLng location;
    private Marker marker;
    private int id;
    private boolean taken;
    private  int height;
    private  int width;

    public Dot(){
        height = 50;
        width = 50;
        taken = false;
        location = new LatLng(51.232356, 4.409553);
    }


    public void setLocation(LatLng location){
        this.location = location;
    }



    public void Draw(GoogleMap mMap, Context context){

        Bitmap dot = getBitmapFromDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.pacman_dot, null));
        Bitmap scaledDot = Bitmap.createScaledBitmap(dot, width, height, false);
        marker = mMap.addMarker(new MarkerOptions()
                .position(location)
                .icon(BitmapDescriptorFactory.fromBitmap(scaledDot)));
    }

    public void setLocation(double v, double v1) {
    }
}