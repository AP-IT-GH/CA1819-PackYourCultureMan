package com.ap.pacyourcultureman;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.ap.pacyourcultureman.Helpers.ApiHelper;
import com.ap.pacyourcultureman.Helpers.CollisionDetection;
import com.ap.pacyourcultureman.Helpers.CollisionHandler;
import com.ap.pacyourcultureman.Helpers.LatLngInterpolator;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class MarkerAnimation {
    public Handler handler = new Handler();
    public Runnable r;
    public Boolean isFrozen = false;
    GameActivity gameActivity;
    public MarkerAnimation(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
    }
    public void animateMarkerToGB(final Marker marker, final LatLng finalPosition, final LatLngInterpolator latLngInterpolator, long time, final int id) {
        final LatLng startPosition = marker.getPosition();
        final long start = SystemClock.uptimeMillis();
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final float durationInMs = time;
        if(!isFrozen) {
            handler.post(r =new Runnable() {
                long elapsed;
                float t;
                float v;
                Boolean test = isFrozen;
                @Override
                public void run() {
                    if(!test) {
                        // Calculate progress using interpolator
                        elapsed = SystemClock.uptimeMillis() - start;
                        t = elapsed / durationInMs;
                        v = interpolator.getInterpolation(t);
                        marker.setPosition(latLngInterpolator.interpolate(v, startPosition, finalPosition));
                        CollisionDetection collisionDetection = new CollisionDetection();
                        CollisionHandler.ghostLatLng = marker.getPosition();
                        if(collisionDetection.collisionDetect(gameActivity.getCurrentPos(), marker.getPosition(), 15) && !gameActivity.ghostCollide) {
                            gameActivity.ghostCollide = true;
                            Log.d("Ghost hit", "Ghost hit");
                            gameActivity.getCollisionHandler().ghostCollision(id);
                        }
                        if(collisionDetection.collisionDetect(gameActivity.getCurrentLocation(), marker.getPosition(), 15) && !gameActivity.ghostCollide) {
                            gameActivity.ghostCollide = true;
                            gameActivity.getCollisionHandler().ghostCollision(id);
                            Log.d("Ghost hit", "Ghost hit");
                        }
                        // Repeat till progress is complete.
                        if (t < 1 && !test) {
                            // Post again 16ms later.
                            handler.postDelayed(r, 16);
                        }
                    }
                }
            });
        }
    }
}