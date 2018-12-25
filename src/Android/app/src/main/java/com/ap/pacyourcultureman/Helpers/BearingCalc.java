package com.ap.pacyourcultureman.Helpers;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;

public class BearingCalc {
    public BearingCalc() {}

    public String getBearingInString(double lat1, double lon1, double lat2, double lon2){
        double longitude1 = lon1;
        double longitude2 = lon2;
        double latitude1 = Math.toRadians(lat1);
        double latitude2 = Math.toRadians(lat2);
        double longDiff= Math.toRadians(longitude2-longitude1);
        double y= Math.sin(longDiff)*Math.cos(latitude2);
        double x=Math.cos(latitude1)*Math.sin(latitude2)-Math.sin(latitude1)*Math.cos(latitude2)*Math.cos(longDiff);
        double resultDegree= (Math.toDegrees(Math.atan2(y, x))+360)%360;
        String coordNames[] = {"N","NNE", "NE","ENE","E", "ESE","SE","SSE", "S","SSW", "SW","WSW", "W","WNW", "NW","NNW", "N"};
        double directionid = Math.round(resultDegree / 22.5);
        if (directionid < 0) {
            directionid = directionid + 16;
        }
        String compasLoc=coordNames[(int) directionid];

        return compasLoc;
    }
    public String getDistance(LatLng latLng1, LatLng latLng2) {
        String distanceInM;
        Location loc1 = new Location("");
        loc1.setLatitude(latLng1.latitude);
        loc1.setLongitude(latLng1.longitude);

        Location loc2 = new Location("");
        loc2.setLatitude(latLng2.latitude);
        loc2.setLongitude(latLng2.longitude);
        distanceInM = new DecimalFormat("##.##").format(loc1.distanceTo(loc2));
        return distanceInM + "m";
    }

    public static double getBearingBetweenTwoPoints1(LatLng latLng1, LatLng latLng2) {

        double lat1 = degreesToRadians(latLng1.latitude);
        double long1 = degreesToRadians(latLng1.longitude);
        double lat2 = degreesToRadians(latLng2.latitude);
        double long2 = degreesToRadians(latLng2.longitude);


        double dLon = (long2 - long1);


        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double radiansBearing = Math.atan2(y, x);


        return radiansToDegrees(radiansBearing);
    }
    private static double degreesToRadians(double degrees) {
        return degrees * Math.PI / 180.0;
    }

    private static double radiansToDegrees(double radians) {
        return radians * 180.0 / Math.PI;
    }
}
