package com.ap.pacyourcultureman;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;

import com.ap.pacyourcultureman.Helpers.ApiHelper;
import com.ap.pacyourcultureman.Helpers.BearingCalc;
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

import static com.ap.pacyourcultureman.Helpers.GetBitmap.getBitmapFromDrawable;


public class Ghost {
    public int id;
    LatLng initLoc;
    public Marker marker;
    public List<Step> steps = new ArrayList<>();
    public int iter;
    public Handler handler = new Handler();
    ApiHelper apiHelper;
    private float speed = 40;
    public MarkerAnimation markerAnimation;
    Boolean newDirections = false;
    public Runnable r;
    public Boolean isFrozen = false;
    public Boolean isDead = false;
    GameActivity gameActivity;
    public Ghost(LatLng location, GameActivity gameActivity) {
        apiHelper = new ApiHelper();
        initLoc = location;
        this.gameActivity = gameActivity;
        this.markerAnimation = new MarkerAnimation(this.gameActivity);
    }

    public void Draw(GoogleMap mMap, Context context) {
        int height = 80;
        int width = 80;
        Bitmap bitmap = getBitmapFromDrawable(ResourcesCompat.getDrawable(context.getResources(), R.mipmap.ic_launcher_foreground, null));
        switch (id){
            case 0:
                bitmap = getBitmapFromDrawable(ResourcesCompat.getDrawable(context.getResources(), R.mipmap.ic_launcher_foreground, null));
                break;
            case 1:
                bitmap = getBitmapFromDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.inky, null));
                break;
            case 2:
                bitmap = getBitmapFromDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.pinky, null));
                break;
            case 3:
                bitmap = getBitmapFromDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.clyde, null));
                break;
        }
        Bitmap smallerblinky = Bitmap.createScaledBitmap(bitmap, width, height, false);
        marker = mMap.addMarker(new MarkerOptions()
                .position(initLoc)
                .icon(BitmapDescriptorFactory.fromBitmap(smallerblinky)));

    }

    public void getSteps(final LatLng destination) {
        steps = new ArrayList<>();
        markerAnimation = new MarkerAnimation(gameActivity);
        String baseUrl = "https://maps.googleapis.com/maps/api/directions/json?origin=" + marker.getPosition().latitude + "," + marker.getPosition().longitude + "&destination=" + destination.latitude + "," + destination.longitude + "&mode=walking&key=" + BuildConfig.GoogleSecAPIKEY;
        Log.d("Steps", baseUrl);
        final JSONDeserializer jsonDeserializer = new JSONDeserializer();
        apiHelper.get(baseUrl, new VolleyCallBack() {
            @Override
            public void onSuccess() {
                steps.clear();
                iter = 0;
                steps = jsonDeserializer.getSteps(apiHelper.getJsonObject());
                newDirections = true;
                //marker.setPosition(steps.get(0).start);
                Log.d("Movement", "newdirections: " + newDirections);
                Location destinationloc = new Location("");
                destinationloc.setLatitude(destination.latitude);
                destinationloc.setLongitude(destination.longitude);

                Location otherLoc = new Location("");
                otherLoc.setLatitude(destination.latitude);
                otherLoc.setLongitude(destination.longitude);

                double finalDistance  = destinationloc.distanceTo(otherLoc);
                Step finalStep = new Step(destination, destination, (int)finalDistance);
                steps.add(finalStep);
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
                if (steps.size() > 1) {
                    elapsed = SystemClock.uptimeMillis() - start;
                    Log.d("Movement", "Logging for : " + id);
                    Log.d("Movement", "Moving to point: " + iter);
                    time = (long)Math.round((steps.get(0).distance) * 1000/(speed));
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
        markerAnimation.animateMarkerToGB(marker, dest, new LatLngInterpolator.Spherical(), time, id);

    }

    public LatLng getLocation() {
        return marker.getPosition();
    }

    public void setSpeed(float _speed){
        speed = _speed;
    }

    public void stopGhost(){
        handler.removeCallbacksAndMessages(r);
        handler = new Handler();
        markerAnimation.handler.removeCallbacksAndMessages(markerAnimation.r);
        markerAnimation.r = null;
        steps = new ArrayList<>();
        iter = 0;
    }
}