package com.ap.pacyourcultureman.Helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.ap.pacyourcultureman.GameActivity;
import com.ap.pacyourcultureman.Player;

import org.json.JSONException;
import org.json.JSONObject;

public class CollisionHandler {
    Context context;
    Player player = ApiHelper.player;
    ApiHelper apiHelper;
    public CollisionHandler(Context context) {
        this.context = context;
        apiHelper = new ApiHelper();
    }
    public void ghostCollision() {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        if(player.getPlayerGameStats().getLifePoints() == 0) {
            alertDialog.setTitle("Game over");
            alertDialog.setMessage("Press OK to start over");
            JSONObject jsonObject = new JSONObject();
            JSONObject jsonParam = new JSONObject();
            try {
                jsonParam.put("totalFailed", player.getPlayerStats().getTotalFailed());
                jsonObject.put("stats", jsonParam);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            apiHelper.put("https://aspcoreapipycm.azurewebsites.net/Users/updatestats/" + Integer.toString(ApiHelper.player.getId()), jsonObject);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        else {
            ApiHelper.player.getPlayerGameStats().setLifePoints(ApiHelper.player.getPlayerGameStats().getLifePoints() - 1);
            JSONObject jsonObject = new JSONObject();
            JSONObject jsonParam = new JSONObject();
            try {
                jsonParam.put("lifePoint", Integer.toString(player.getPlayerGameStats().getLifePoints()));
                jsonObject.put("gameStats", jsonParam);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //  apiHelper.put("https://aspcoreapipycm.azurewebsites.net/Users/updatestats/" + Integer.toString(ApiHelper.player.id), jsonObject);
            Toast.makeText(context, "You got hit", Toast.LENGTH_LONG).show();
            Log.d("Toast", "Test");
            //TODO Reset ghost
        }
    }
}
