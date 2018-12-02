package com.ap.pacyourcultureman.Helpers;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.ap.pacyourcultureman.GameActivity;
import com.ap.pacyourcultureman.Ghost;
import com.ap.pacyourcultureman.Menus.Gunmenu;

public class GunHandler {

    Activity activity;
    Ghost ghost;
    public GunHandler(Ghost ghost, Activity activity) {
        this.ghost = ghost;
        this.activity = activity;
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
        }
        else {
            Toast.makeText(activity.getApplicationContext(), "No ammo", Toast.LENGTH_SHORT).show();
        }
    }
    private void freezeHandler() {
        if(ApiHelper.player.getPlayerGameStats().getFreezeGun() != 0) {
            ApiHelper.player.getPlayerGameStats().setFreezeGun(ApiHelper.player.getPlayerGameStats().getFreezeGun() - 1);
            Log.d("Ghost", "hit with Freeze");
        }
        else {
            Toast.makeText(activity.getApplicationContext(), "No ammo", Toast.LENGTH_SHORT).show();
        }
    }
    private void pushBackHandler() {
        if(ApiHelper.player.getPlayerGameStats().getPushBackGun() != 0) {
            ApiHelper.player.getPlayerGameStats().setPushBackGun(ApiHelper.player.getPlayerGameStats().getPushBackGun() - 1);
            Log.d("Ghost", "hit with pushback");
        }
        else {
            Toast.makeText(activity.getApplicationContext(), "No ammo", Toast.LENGTH_SHORT).show();
        }
    }
}
