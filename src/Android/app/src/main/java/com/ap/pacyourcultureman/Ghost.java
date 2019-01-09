package com.ap.pacyourcultureman;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;

import com.ap.pacyourcultureman.Helpers.ApiHelper;
import com.ap.pacyourcultureman.Helpers.JSONDeserializer;
import com.ap.pacyourcultureman.Helpers.LatLngInterpolator;
import com.ap.pacyourcultureman.Helpers.VolleyCallBack;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.ap.pacyourcultureman.GameActivity.getBitmapFromDrawable;

public class Ghost {
    private int id;
    LatLng initLoc;
    public Marker marker;
    List<Step> steps = new ArrayList<>();
    private int iter;
    public Handler handler = new Handler();
    ApiHelper apiHelper;
    int speed = 40;
    public MarkerAnimation markerAnimation = new MarkerAnimation();
    Boolean newDirections = false;
    public Runnable r;
    public Boolean isFrozen = false;
    public Boolean isDead = false;
    public Ghost(LatLng location) {
        apiHelper = new ApiHelper();
        initLoc = location;
    }

    public int setColor() {
        return 1;
    }
    // we moeten hier waarschijnlijk ook een soort van AI programmeren anders gaan de spookjes
    // op 1 rechte lijn terecht komen achter de speler.

    public void Draw(GoogleMap mMap, Context context) {
        int height = 80;
        int width = 80;
        Bitmap bitmap = getBitmapFromDrawable(ResourcesCompat.getDrawable(context.getResources(), R.mipmap.ic_launcher_foreground, null));
        Bitmap smallerblinky = Bitmap.createScaledBitmap(bitmap, width, height, false);
        marker = mMap.addMarker(new MarkerOptions()
                .position(initLoc)
                .icon(BitmapDescriptorFactory.fromBitmap(smallerblinky)));

    }

    public void getSteps(LatLng destination) {
        steps = new ArrayList<>();
        markerAnimation = new MarkerAnimation();
        String baseUrl = "https://maps.googleapis.com/maps/api/directions/json?origin=" + marker.getPosition().latitude + "," + marker.getPosition().longitude + "&destination=" + destination.latitude + "," + destination.longitude + "&mode=walking&key=" + BuildConfig.GoogleSecAPIKEY;
        Log.d("Steps", baseUrl);
        final JSONDeserializer jsonDeserializer = new JSONDeserializer();
        apiHelper.get(baseUrl, new VolleyCallBack() {
            @Override
            public void onSuccess() {
                steps.clear();
                steps = jsonDeserializer.getSteps(apiHelper.getJsonObject());
                newDirections = true;
                //marker.setPosition(steps.get(0).start);
                Log.d("Movement", "newdirections: " + newDirections);

                FollowPath();
            }
        });

    }


    public void FollowPath() {
        final long start = SystemClock.uptimeMillis();
        handler.post(r = new Runnable() {
            long elapsed;
            long time = 30000;

            @Override
            public void run() {
                if (1 < steps.size()) {
                    elapsed = SystemClock.uptimeMillis() - start;
                    Log.d("Movement", "Moving to point: " + iter);
                    time = (steps.get(0).distance * 1000)/(speed);
                    Log.d("Movement" ,"Step: " + steps.get(1));
                    Log.d("Movement", "Time for this step: " + time);
                    Move(steps.get(1).start, marker, time);
                    iter++;
                    steps.remove(0);
                    Log.d("Movement", "Steps to go = " + steps.size());
                    handler.postDelayed(this, time + 500);
                }
            }
        });
    }

    public void Move(LatLng dest, Marker mrkr, long time) {
        Log.d("Movement", "MarkerLocation: " + mrkr.getPosition());
        markerAnimation.animateMarkerToGB(marker, dest, new LatLngInterpolator.Spherical(), time);

    }

    public LatLng getLocation() {
        return marker.getPosition();
    }
}