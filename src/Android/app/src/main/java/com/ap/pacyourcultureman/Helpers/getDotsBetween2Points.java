package com.ap.pacyourcultureman.Helpers;

import com.ap.pacyourcultureman.Dot;
import com.ap.pacyourcultureman.Street;

import java.util.ArrayList;
import java.util.List;


public class getDotsBetween2Points {

    private static final long RADIUS_OF_EARTH = 6371000; // radius of earth in m


    public static void GetDotsBetweenAanB(Double latA, Double lngA, Double latB, Double lngB, List<Dot> list){
        // point interval in meters
        ArrayList<Dot> temp = new ArrayList<>();
        int interval = 30;
        getDotsBetween2Points.MockLocation start = new getDotsBetween2Points.MockLocation(latA, lngA);
        getDotsBetween2Points.MockLocation end = new getDotsBetween2Points.MockLocation(latB, lngB);
        double azimuth = calculateBearing(start, end);
        ArrayList<getDotsBetween2Points.MockLocation> coords = getLocations(azimuth, start, end);
        for (getDotsBetween2Points.MockLocation mockLocation : coords) {
            temp.add(new Dot(mockLocation.lat , mockLocation.lng));
        }
        for(int i = 0; i < temp.size()  ; i+=interval) {
            list.add(new Dot(temp.get(i).getLat() , temp.get(i).getLon()));
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


    /**
     * returns every coordinate pair in between two coordinate pairs given the desired interval
     * @param azimuth
     * @param start
     * @param end
     * @return
     */
    private static ArrayList<MockLocation> getLocations( double azimuth, MockLocation start, MockLocation end) {

        //niet wijzigen
        final int interval = 1;
        double d = getPathLength(start, end);
        int dist = (int) d / interval;
        int coveredDist = interval;
        ArrayList<MockLocation> coords = new ArrayList<>();
        coords.add(new MockLocation(start.lat, start.lng));
        for(int distance = 0; distance < dist; distance += interval) {
            MockLocation coord = getDestinationLatLng(start.lat, start.lng, azimuth, coveredDist);
            coveredDist += interval;
            coords.add(coord);
        }
        coords.add(new MockLocation(end.lat, end.lng));

        return coords;

    }

    /**
     * calculates the distance between two lat, long coordinate pairs
     * @param start
     * @param end
     * @return
     */
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

    /**
     * returns the lat an long of destination point given the start lat, long, aziuth, and distance
     * @param lat
     * @param lng
     * @param azimuth
     * @param distance
     * @return
     */
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

    /**
     * calculates the azimuth in degrees from start point to end point");
     double startLat = Math.toRadians(start.lat);
     * @param start
     * @param end
     * @return
     */
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