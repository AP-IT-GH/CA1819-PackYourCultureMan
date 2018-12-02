package com.ap.pacyourcultureman;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;

import com.ap.pacyourcultureman.Helpers.ApiHelper;
import com.ap.pacyourcultureman.Helpers.LatLngInterpolator;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import static com.ap.pacyourcultureman.GameActivity.getBitmapFromDrawable;

public class Ghost {
    private LatLng location;
    private int id;
    public Marker marker;
    private ArrayList<LatLng> steps = new ArrayList<LatLng>(){};
    private int iter;
    Handler handler;
    ApiHelper apiHelper;

    public Ghost(){
        apiHelper = new ApiHelper();
        steps.add(new LatLng(51.2298337, 4.4208078));
        steps.add(new LatLng(51.227979, 4.418715));
        steps.add(new LatLng(51.227706, 4.416001));
        steps.add(new LatLng(51.229796, 4.415240));
        steps.add(new LatLng(51.229816, 4.420570));
    }

    public int setColor(){
        return 1;
    }
    // we moeten hier waarschijnlijk ook een soort van AI programmeren anders gaan de spookjes
    // op 1 rechte lijn terecht komen achter de speler.

    public void Draw(GoogleMap mMap, Context context){
        int height = 80;
        int width = 80;
        Bitmap bitmap = getBitmapFromDrawable(ResourcesCompat.getDrawable(context.getResources(), R.mipmap.ic_launcher_foreground, null));
        Bitmap smallerblinky = Bitmap.createScaledBitmap(bitmap, width, height, false);

        LatLng spookyloc = new LatLng(51.230108, 4.418516);
        location = spookyloc;
        marker = mMap.addMarker(new MarkerOptions()
                .position(spookyloc)
                .icon(BitmapDescriptorFactory.fromBitmap(smallerblinky)));

    }

    public void goToLocation(LatLng destination){

    }

    public void getSteps(){
        steps = new ArrayList<LatLng>(){};
    }


    public void FollowPath(LatLng origin, LatLng dest){
        handler = new Handler();
        final long start = SystemClock.uptimeMillis();

        handler.post(new Runnable() {
            long elapsed;
            long time = 3000;
            @Override
            public void run() {
                elapsed = SystemClock.uptimeMillis() - start;
                Log.d("Movement", "Moving to point: " + iter);
                Move(steps.get(iter), marker, time);
                iter++;
                if (iter < steps.size()){
                    handler.postDelayed(this, time);
                }
            }
        });
    }

    public void Move(LatLng dest, Marker mrkr, long time){
        Log.d("Movement", "MarkerLocation: " + mrkr.getPosition());
        MarkerAnimation.animateMarkerToGB(marker, dest, new LatLngInterpolator.Spherical(), time);

    }

    public LatLng getLocation(){
        return marker.getPosition();
    }
}