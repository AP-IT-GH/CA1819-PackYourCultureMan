package com.ap.pacyourcultureman;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

public class Perimeter {

    public static void DrawGameFieldLine(GoogleMap mMap){
        mMap.addPolygon(new PolygonOptions().add(
                new LatLng(51.208187, 4.384678), new LatLng(51.207181, 4.386604),
                new LatLng(51.205057, 4.388015), new LatLng(51.204835, 4.388629),
                new LatLng(51.204428, 4.388991), new LatLng(51.210132, 4.406642),
                new LatLng(51.214943, 4.413262), new LatLng(51.215252, 4.413283),
                new LatLng(51.216186, 4.414217), new LatLng(51.220990, 4.416567),
                new LatLng(51.228645, 4.413866), new LatLng(51.229040, 4.418705),
                new LatLng(51.229816, 4.418737), new LatLng(51.229797, 4.413367),
                new LatLng(51.230891, 4.413121), new LatLng(51.231049, 4.412558),
                new LatLng(51.231083, 4.408443), new LatLng(51.231325, 4.407488),
                new LatLng(51.231313, 4.405128), new LatLng(51.234400, 4.405026),
                new LatLng(51.234467, 4.403918), new LatLng(51.234373, 4.403333),
                new LatLng(51.233599, 4.403078), new LatLng(51.231142, 4.402863),
                new LatLng(51.230208, 4.402348), new LatLng(51.229673, 4.401967),
                new LatLng(51.227898, 4.401870), new LatLng(51.226393, 4.400411),
                new LatLng(51.222813, 4.398169), new LatLng(51.223087, 4.397072),
                new LatLng(51.222943, 4.396975), new LatLng(51.222353, 4.397187),
                new LatLng(51.221862, 4.396830), new LatLng(51.221724, 4.396492),
                new LatLng(51.221487, 4.396505), new LatLng(51.221343, 4.397210),
                new LatLng(51.218344, 4.395123), new LatLng(51.215286, 4.392913),
                new LatLng(51.210386, 4.387924), new LatLng(51.210306, 4.387794),
                new LatLng(51.208187, 4.384678)).strokeColor(Color.RED).strokeWidth(7));
    } }