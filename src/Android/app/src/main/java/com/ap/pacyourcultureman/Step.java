package com.ap.pacyourcultureman;

import com.google.android.gms.maps.model.LatLng;

public class Step {
    public LatLng start;
    public LatLng end;
    public int distance;

    public Step(LatLng start, LatLng end, int distance){
        this.start = start;
        this.end = end;
        this.distance = distance;
    }
}