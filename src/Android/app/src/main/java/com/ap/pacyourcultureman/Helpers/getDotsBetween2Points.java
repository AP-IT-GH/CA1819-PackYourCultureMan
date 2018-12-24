package com.ap.pacyourcultureman.Helpers;

import com.ap.pacyourcultureman.Dot;

import java.util.ArrayList;
import java.util.List;


public class GetDotsBetween2Points {

    private static final long RADIUS_OF_EARTH = 6371000; // radius of earth in m


    public static void GetDotsBetweenAanB(Double latA, Double lngA, Double latB, Double lngB, List<Dot> list){
        ArrayList<Dot> tempList = new ArrayList<>();
        // distance between points
        int meters = 30;
        // start and end position
        GetDotsBetween2Points.MockLocation start = new GetDotsBetween2Points.MockLocation(latA, lngA);
        GetDotsBetween2Points.MockLocation end = new GetDotsBetween2Points.MockLocation(latB, lngB);
        //azimuth
        double azimuth = calculateBearing(start, end);
        ArrayList<GetDotsBetween2Points.MockLocation> coords = getLocations(azimuth, start, end);
        for (GetDotsBetween2Points.MockLocation mockLocation : coords) {
            tempList.add(new Dot(mockLocation.lat , mockLocation.lng));
        }
        for(int i = 0; i < tempList.size()  ; i+=meters) {
            list.add(new Dot(tempList.get(i).getLat() , tempList.get(i).getLon()));
        }
    }


    private static class MockLocation {
        public double lat;
        public double lng;

        private MockLocation(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }

        @Override
        public String toString() {
            return "(" + lat + "," + lng + ")";
        }
    }



      /*returns every coordinate pair in between two coordinate pairs given the desired interval*/

    private static ArrayList<MockLocation> getLocations( double azimuth, MockLocation start, MockLocation end) {

        double d = getPathLength(start, end);
        int dist = (int) d;
        int coveredDist = 1;
        ArrayList<MockLocation> coords = new ArrayList<>();
        coords.add(new MockLocation(start.lat, start.lng));
        for(int distance = 0; distance < dist; distance += 1) {
            MockLocation coord = getDestinationLatLng(start.lat, start.lng, azimuth, coveredDist);
            coveredDist += 1;
            coords.add(coord);
        }
        coords.add(new MockLocation(end.lat, end.lng));

        return coords;

    }


   /*  calculates the distance between two lat, long coordinate pairs*/

    private static double getPathLength(MockLocation start, MockLocation end) {
        double lat1Rads = Math.toRadians(start.lat);
        double lat2Rads = Math.toRadians(end.lat);
        double deltaLat = Math.toRadians(end.lat - start.lat);

        double deltaLng = Math.toRadians(end.lng - start.lng);
        double a = Math.sin(deltaLat/2) * Math.sin(deltaLat/2) + Math.cos(lat1Rads) * Math.cos(lat2Rads) * Math.sin(deltaLng/2) * Math.sin(deltaLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = RADIUS_OF_EARTH * c;
        return d;
    }


   /*   returns the lat and long of destination point given the start lat, long, azimuth, and distance*/

    private static MockLocation getDestinationLatLng(double lat, double lng, double azimuth, double distance) {
        double radiusKm = RADIUS_OF_EARTH / 1000; //Radius of the Earth in km
        double brng = Math.toRadians(azimuth); //Bearing is degrees converted to radians.
        double d = distance / 1000; //Distance m converted to km
        double lat1 = Math.toRadians(lat); //Current dd lat point converted to radians
        double lon1 = Math.toRadians(lng); //Current dd long point converted to radians
        double lat2 = Math.asin(Math.sin(lat1) * Math.cos(d / radiusKm) + Math.cos(lat1) * Math.sin(d / radiusKm) * Math.cos(brng));
        double lon2 = lon1 + Math.atan2(Math.sin(brng) * Math.sin(d / radiusKm) * Math.cos(lat1), Math.cos(d / radiusKm) - Math.sin(lat1) * Math.sin(lat2));
        //convert back to degrees
        lat2 = Math.toDegrees(lat2);
        lon2 = Math.toDegrees(lon2);
        return new MockLocation(lat2, lon2);
    }


/*     calculates the azimuth in degrees from start point to end point */

    private static double calculateBearing(MockLocation start, MockLocation end) {
        double startLat = Math.toRadians(start.lat);
        double startLong = Math.toRadians(start.lng);
        double endLat = Math.toRadians(end.lat);
        double endLong = Math.toRadians(end.lng);
        double dLong = endLong - startLong;
        double dPhi = Math.log(Math.tan((endLat / 2.0) + (Math.PI / 4.0)) / Math.tan((startLat / 2.0) + (Math.PI / 4.0)));
        if (Math.abs(dLong) > Math.PI) {
            if (dLong > 0.0) {
                dLong = -(2.0 * Math.PI - dLong);
            } else {
                dLong = (2.0 * Math.PI + dLong);
            }
        }
        double bearing = (Math.toDegrees(Math.atan2(dLong, dPhi)) + 360.0) % 360.0;
        return bearing;
    }

}