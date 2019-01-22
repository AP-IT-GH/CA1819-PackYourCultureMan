package com.ap.pacyourcultureman.Helpers;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.ap.pacyourcultureman.BuildConfig;
import com.ap.pacyourcultureman.Dot;
import com.ap.pacyourcultureman.GameActivity;
import com.ap.pacyourcultureman.Ghost;
import com.ap.pacyourcultureman.Menus.Gunmenu;
import com.ap.pacyourcultureman.Skins;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GunHandler {

    Activity activity;
    Ghost ghost;
    ApiHelper apiHelper;
    Skins player;
    BearingCalc bearingCalc = new BearingCalc();
    double pushDistance = 0.003;

    public GunHandler(Ghost ghost, Skins player, Activity activity) {
        this.ghost = ghost;
        this.activity = activity;
        this.player = player;
        apiHelper = new ApiHelper();
    }
    public void gunHandler(Marker marker) {
        if(Gunmenu.gunSelected) {
            switch (Gunmenu.selectedGun) {
                case "Rifle":    rifleHandler(marker);
                    break;
                case "Freeze":   freezeHandler();
                    break;
                case "Pushback": pushBackHandler();
                    break;
            }
        }
    }
    private void rifleHandler(Marker marker) {
        if(ApiHelper.player.getPlayerGameStats().getRifle() > 0) {
            ApiHelper.player.getPlayerGameStats().setRifle(ApiHelper.player.getPlayerGameStats().getRifle() - 1);
            Log.d("Ghost", "hit with rifle");
            apiHelper.put("https://aspcoreapipycm.azurewebsites.net/Users/updategamestats/" + Integer.toString(ApiHelper.player.getId()), putGameStats(), new VolleyCallBack() {
                @Override
                public void onSuccess() {

                }
            });
            ghost.handler.removeCallbacks(ghost.r);
            ghost.markerAnimation.handler.removeCallbacks(ghost.markerAnimation.r);
            marker.remove();
        }
        else {
            Toast.makeText(activity.getApplicationContext(), "No ammo", Toast.LENGTH_SHORT).show();
        }
    }
    private void freezeHandler() {
        if(ApiHelper.player.getPlayerGameStats().getFreezeGun() > 0) {
            ApiHelper.player.getPlayerGameStats().setFreezeGun(ApiHelper.player.getPlayerGameStats().getFreezeGun() - 1);
            Log.d("Ghost", "hit with Freeze");
            apiHelper.put("https://aspcoreapipycm.azurewebsites.net/Users/updategamestats/" + Integer.toString(ApiHelper.player.getId()), putGameStats(), new VolleyCallBack() {
                @Override
                public void onSuccess() {

                }
            });
            ghost.markerAnimation.isFrozen = true;
            ghost.isFrozen = true;
            ghost.stopGhost();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ghost.getSteps(ApiHelper.assignments.get(1).getLatLng());
                    Log.d("Assignment", ApiHelper.assignments.get(1).getName());
                    ghost.markerAnimation.isFrozen = false;
                    ghost.isFrozen = false;
                    //          ghost.handler = new Handler();
                    ghost.markerAnimation.handler = new Handler();
                    ghost.handler.postDelayed(ghost.r, 100);
                    ghost.markerAnimation.handler.postDelayed(ghost.markerAnimation.r, 100);
                }
            }, 60000);

        }
        else {
            Toast.makeText(activity.getApplicationContext(), "No ammo", Toast.LENGTH_SHORT).show();
        }
    }
    private void pushBackHandler() {
        if(ApiHelper.player.getPlayerGameStats().getPushBackGun() > 0) {
            //ApiHelper.player.getPlayerGameStats().setPushBackGun(ApiHelper.player.getPlayerGameStats().getPushBackGun() - 1);
            Log.d("Ghost", "hit with pushback");
            apiHelper.put("https://aspcoreapipycm.azurewebsites.net/Users/updategamestats/" + Integer.toString(ApiHelper.player.getId()), putGameStats(), new VolleyCallBack() {
                @Override
                public void onSuccess() {

                }
            });
            LatLng spookyloc = ghost.getLocation();
            LatLng playerloc = player.getMarker().getPosition();
            LatLng destination =  new LatLng(51, 4);

            String bearing = bearingCalc.getBearingInString(playerloc.latitude, playerloc.longitude, spookyloc.latitude, spookyloc.longitude);
            Log.d("push", bearing);
            switch (bearing){
                case "N":
                    destination = new LatLng(spookyloc.latitude + pushDistance, spookyloc.longitude);
                    break;
                case "NNE":
                    destination = new LatLng(spookyloc.latitude + pushDistance, spookyloc.longitude + pushDistance);
                    break;
                case "NE":
                    destination = new LatLng(spookyloc.latitude + pushDistance, spookyloc.longitude + pushDistance);
                    break;
                case "ENE":
                    destination = new LatLng(spookyloc.latitude + pushDistance, spookyloc.longitude + pushDistance);
                    break;
                case "E":
                    destination = new LatLng(spookyloc.latitude, spookyloc.longitude + pushDistance);
                    break;
                case "ESE":
                    destination = new LatLng(spookyloc.latitude - pushDistance, spookyloc.longitude + pushDistance);
                    break;
                case "SE":
                    destination = new LatLng(spookyloc.latitude - pushDistance, spookyloc.longitude + pushDistance);
                    break;
                case "SSE":
                    destination = new LatLng(spookyloc.latitude - pushDistance, spookyloc.longitude + pushDistance);
                    break;
                case "S":
                    destination = new LatLng(spookyloc.latitude - pushDistance, spookyloc.longitude);
                    break;
                case "SSW":
                    destination = new LatLng(spookyloc.latitude - pushDistance, spookyloc.longitude - pushDistance);
                    break;
                case "SW":
                    destination = new LatLng(spookyloc.latitude - pushDistance, spookyloc.longitude - pushDistance);
                    break;
                case "WSW":
                    destination = new LatLng(spookyloc.latitude - pushDistance, spookyloc.longitude - pushDistance);
                    break;
                case "W":
                    destination = new LatLng(spookyloc.latitude, spookyloc.longitude - pushDistance);
                    break;
                case "WNW":
                    destination = new LatLng(spookyloc.latitude + pushDistance, spookyloc.longitude + pushDistance);
                    break;
                case "NW":
                    destination = new LatLng(spookyloc.latitude + pushDistance, spookyloc.longitude + pushDistance);
                    break;
                case "NNW":
                    destination = new LatLng(spookyloc.latitude + pushDistance, spookyloc.longitude + pushDistance);
                    break;
            }

            double fakelat = destination.latitude + 0.001;
            double fakelong = destination.longitude + 0.001;
            
            ghost.stopGhost();

            String url = "https://roads.googleapis.com/v1/snapToRoads?path=" + destination.latitude + "," + destination.longitude + "|" + fakelat + "," + fakelong + "&interpolate=false&key=" + BuildConfig.GoogleSecAPIKEY;
            Log.d("push", url);

            apiHelper.get(url, new VolleyCallBack() {
                @Override
                public void onSuccess() {
                    Log.d("push", apiHelper.getJsonObject().toString());
                    JSONDeserializer jsonDeserializer = new JSONDeserializer();
                    List<Dot> dots = jsonDeserializer.correctedDots(apiHelper.getJsonObject());
                    Dot dot = dots.get(0);
                    LatLng defdest = new LatLng(dot.getLat(), dot.getLon());
                    ghost.Move(defdest, ghost.marker, 2000);
                }
            });
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ghost.getSteps(ApiHelper.assignments.get(1).getLatLng());
                    Log.d("Assignment", ApiHelper.assignments.get(1).getName());
                    ghost.markerAnimation.isFrozen = false;
                    ghost.isFrozen = false;
                    //          ghost.handler = new Handler();
                    ghost.markerAnimation.handler = new Handler();
                    ghost.handler.postDelayed(ghost.r, 100);
                    ghost.markerAnimation.handler.postDelayed(ghost.markerAnimation.r, 100);
                }
            }, 3000);
        }
        else {
            Toast.makeText(activity.getApplicationContext(), "No ammo", Toast.LENGTH_SHORT).show();
        }
    }
    private JSONObject putGameStats() {
        JSONSerializer jsonSerializer = new JSONSerializer();
        JSONObject jsonObject = jsonSerializer.jsonPutGameStats(ApiHelper.player.getPlayerGameStats().getLifePoints(), ApiHelper.player.getPlayerGameStats().getRifle(), ApiHelper.player.getPlayerGameStats().getFreezeGun(), ApiHelper.player.getPlayerGameStats().getPushBackGun(),apiHelper.player.getPlayerGameStats().getCoins());
        return jsonObject;
    }
}
