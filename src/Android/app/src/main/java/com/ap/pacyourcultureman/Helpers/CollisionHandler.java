package com.ap.pacyourcultureman.Helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.ap.pacyourcultureman.GameActivity;
import com.ap.pacyourcultureman.Ghost;
import com.ap.pacyourcultureman.Player;
import com.ap.pacyourcultureman.VisitedSight;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.logging.LogRecord;

public class CollisionHandler {
    Context context;
    Player player = ApiHelper.player;
    ApiHelper apiHelper;
    public static LatLng playerLatLng;
    public static LatLng ghostLatLng;
    CollisionDetection collisionDetection = new CollisionDetection();
    GameActivity gameActivity;
    Handler mHandler = new Handler();
    public CollisionHandler(Context context, GameActivity gameActivity) {
        this.context = context;
        apiHelper = new ApiHelper();
        this.gameActivity = gameActivity;
    }
    public void ghostCollision(int id) {

        if(player.getPlayerGameStats().getLifePoints() == 0 ) {
     //       gameActivity.ghostCollideTimer = true;
            JSONObject jsonObject = new JSONObject();
            JSONObject jsonParam = new JSONObject();
            try {
                jsonParam.put("totalFailed", player.getPlayerStats().getTotalFailed());
                jsonObject.put("stats", jsonParam);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            apiHelper.put("https://aspcoreapipycm.azurewebsites.net/Users/updatestats/" + Integer.toString(ApiHelper.player.getId()), jsonObject, new VolleyCallBack() {
                @Override
                public void onSuccess() {
                    Toast.makeText(context, "No lifepoints left, resetting to a new game", Toast.LENGTH_SHORT).show();
                    ApiHelper.player.getPlayerStats().setCurrentScore(0);
                    gameActivity.resetAllGhosts();
                }
            });
        }
        else {
          //  gameActivity.ghostCollideTimer = true;
            int newScore = Integer.valueOf(ApiHelper.player.getPlayerStats().getCurrentScore() / 2);
            ApiHelper.player.getPlayerStats().setCurrentScore(newScore);
            Log.d("Score", Integer.toString(newScore));
            gameActivity.resetGhost(id);
            ApiHelper.player.getPlayerGameStats().setLifePoints(ApiHelper.player.getPlayerGameStats().getLifePoints() - 1);
            JSONObject jsonObject = new JSONObject();
            JSONObject jsonParam = new JSONObject();
            try {
                jsonParam.put("lifePoint", Integer.toString(player.getPlayerGameStats().getLifePoints()));
                jsonObject.put("gameStats", jsonParam);
            } catch (JSONException e) {
                e.printStackTrace();
            }
              apiHelper.put("https://aspcoreapipycm.azurewebsites.net/Users/updategamestats/" + Integer.toString(ApiHelper.player.getId()), jsonObject, new VolleyCallBack() {
                  @Override
                  public void onSuccess() {

                  }
              });
            Toast.makeText(context, "You got hit", Toast.LENGTH_LONG).show();
            Log.d("Toast", "Test");
            //TODO Reset ghost
        }
    }
    public void currentAssigmentCollision() {
        Boolean newhighscore = false;
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonParam = new JSONObject();

        player.getPlayerGameStats().setCoins(player.getPlayerGameStats().getCoins() + player.getPlayerStats().getCurrentScore());

        player.getPlayerStats().setTotalScore(player.getPlayerStats().getTotalScore() + player.getPlayerStats().getCurrentScore());
        player.getPlayerStats().setTotalSuccess(player.getPlayerStats().getTotalSuccess() + 1);
        if(player.getPlayerStats().getCurrentScore() > player.getPlayerStats().getHighestScore()) {
            player.getPlayerStats().setHighestScore(player.getPlayerStats().getCurrentScore());
            Toast.makeText(context, "New highscore!", Toast.LENGTH_SHORT).show();
            newhighscore = true;
        }
        try {
            jsonParam.put("totalScore", player.getPlayerStats().getTotalScore());
            jsonParam.put("totalSucces", player.getPlayerStats().getTotalSuccess());
            if(newhighscore) {jsonParam.put("highestScore", player.getPlayerStats().getHighestScore());
            Log.d("Json", "High");}
            jsonObject.put("stats", jsonParam);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        apiHelper.put("https://aspcoreapipycm.azurewebsites.net/Users/updatestats/" + Integer.toString(ApiHelper.player.getId()), jsonObject, new VolleyCallBack() {
            @Override
            public void onSuccess() {

            }
        });
        player.getPlayerStats().setCurrentScore(0);
        Log.d("Json", jsonObject.toString());}


    public void visitedSightsSetBoolean() {
        for (int i = 0; i <  ApiHelper.visitedSights.size(); i++) {
            if(GameActivity.currentAssigment.getId() == ApiHelper.visitedSights.get(i).getBuildingId()) {
                ApiHelper.visitedSights.get(i).setChecked(true);
            }}
    }
    public void visitedSightsPut(){
        // jsonobject with json array put
        JSONObject object = new JSONObject();
        JSONArray array =new JSONArray();
        try {
            for (int i = 0; i <  ApiHelper.visitedSights.size(); i++){
                JSONObject objp = new JSONObject();
                objp.put("id",ApiHelper.visitedSights.get(i).getId());
                objp.put("buildingId",ApiHelper.visitedSights.get(i).getBuildingId());
                objp.put("isChecked",ApiHelper.visitedSights.get(i).isChecked());
                objp.put("userId",ApiHelper.visitedSights.get(i).getUserId());
                array.put(objp); }
                object.put("visitedSights",array);}
        catch (JSONException e) {
            e.printStackTrace();
        }
        apiHelper.put("https://aspcoreapipycm.azurewebsites.net/Users/updatevisitedsights/" + Integer.toString(ApiHelper.player.getId()), object, new VolleyCallBack() {
            @Override
            public void onSuccess() {

            }
        });
    }

}
