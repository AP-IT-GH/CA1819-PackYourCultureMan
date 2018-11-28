package com.ap.pacyourcultureman.Helpers;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class CollisionDetection {
    public CollisionDetection() {}
    public Boolean collisionDetect(LatLng latLng1, LatLng latLng2, int distance) {
        Location loc1 = new Location("");
        loc1.setLatitude(latLng1.latitude);
        loc1.setLongitude(latLng1.longitude);

        Location loc2 = new Location("");
        loc2.setLatitude(latLng2.latitude);
        loc2.setLongitude(latLng2.longitude);

        float distanceInMeters = loc1.distanceTo(loc2);
        if(distanceInMeters < distance) {
            return true;
        }
        else {
            return false;
        }
    }
}
