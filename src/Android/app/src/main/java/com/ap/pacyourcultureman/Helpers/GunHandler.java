package com.ap.pacyourcultureman.Helpers;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.ap.pacyourcultureman.GameActivity;
import com.ap.pacyourcultureman.Ghost;
import com.ap.pacyourcultureman.Menus.Gunmenu;

import org.json.JSONObject;

public class GunHandler {

    Activity activity;
    Ghost ghost;
    ApiHelper apiHelper;
    public GunHandler(Ghost ghost, Activity activity) {
        this.ghost = ghost;
        this.activity = activity;
        apiHelper = new ApiHelper();
    }
    public void gunHandler() {
        if(Gunmenu.gunSelected) {
            switch (Gunmenu.selectedGun) {
                case "Rifle":    rifleHandler();
                    break;
                case "Freeze":   freezeHandler();
                    break;
                case "Pushback": pushBackHandler();
                    break;
            }
        }
    }
    private void rifleHandler() {
        if(ApiHelper.player.getPlayerGameStats().getRifle() != 0) {
            ApiHelper.player.getPlayerGameStats().setRifle(ApiHelper.player.getPlayerGameStats().getRifle() - 1);
            Log.d("Ghost", "hit with rifle");
            apiHelper.put("https://aspcoreapipycm.azurewebsites.net/Users/updategamestats/" + Integer.toString(ApiHelper.player.getId()), putGameStats());
            ghost.handler.removeCallbacks(ghost.r);
            ghost.markerAnimation.handler.removeCallbacks(ghost.markerAnimation.r);
        }
        else {
            Toast.makeText(activity.getApplicationContext(), "No ammo", Toast.LENGTH_SHORT).show();
            ghost.handler.removeCallbacks(ghost.r);
            ghost.markerAnimation.handler.removeCallbacks(ghost.markerAnimation.r);
        }
    }
    private void freezeHandler() {
        if(ApiHelper.player.getPlayerGameStats().getFreezeGun() != 0) {
            ApiHelper.player.getPlayerGameStats().setFreezeGun(ApiHelper.player.getPlayerGameStats().getFreezeGun() - 1);
            Log.d("Ghost", "hit with Freeze");
            apiHelper.put("https://aspcoreapipycm.azurewebsites.net/Users/updategamestats/" + Integer.toString(ApiHelper.player.getId()), putGameStats());
            ghost.handler.postDelayed(ghost.r, 5000);
            ghost.markerAnimation.handler.postDelayed(ghost.markerAnimation.r, 5000);

        }
        else {
            Toast.makeText(activity.getApplicationContext(), "No ammo", Toast.LENGTH_SHORT).show();
            ghost.markerAnimation.isFrozen = true;
            ghost.isFrozen = true;
            ghost.handler.removeCallbacksAndMessages(ghost.r);
            ghost.handler = null;
            ghost.markerAnimation.handler.removeCallbacksAndMessages(ghost.markerAnimation.r);
            ghost.markerAnimation.handler = null;
            ghost.r = null;
            ghost.markerAnimation.r = null;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ghost.getSteps(ApiHelper.assignments.get(1).getLatLng());
                    ghost.markerAnimation.isFrozen = false;
                    ghost.isFrozen = false;
                    ghost.handler = new Handler();
                    ghost.markerAnimation.handler = new Handler();
                    ghost.handler.postDelayed(ghost.r, 100);
                    ghost.markerAnimation.handler.postDelayed(ghost.markerAnimation.r, 100);
                }
            }, 5000);
        }
    }
    private void pushBackHandler() {
        if(ApiHelper.player.getPlayerGameStats().getPushBackGun() != 0) {
            ApiHelper.player.getPlayerGameStats().setPushBackGun(ApiHelper.player.getPlayerGameStats().getPushBackGun() - 1);
            Log.d("Ghost", "hit with pushback");
            apiHelper.put("https://aspcoreapipycm.azurewebsites.net/Users/updategamestats/" + Integer.toString(ApiHelper.player.getId()), putGameStats());

        }
        else {
            Toast.makeText(activity.getApplicationContext(), "No ammo", Toast.LENGTH_SHORT).show();
        }
    }
    private JSONObject putGameStats() {
        JSONSerializer jsonSerializer = new JSONSerializer();
        JSONObject jsonObject = jsonSerializer.jsonPutGameStats(ApiHelper.player.getPlayerGameStats().getLifePoints(), ApiHelper.player.getPlayerGameStats().getRifle(), ApiHelper.player.getPlayerGameStats().getFreezeGun(), ApiHelper.player.getPlayerGameStats().getPushBackGun());
        return jsonObject;
    }
}
