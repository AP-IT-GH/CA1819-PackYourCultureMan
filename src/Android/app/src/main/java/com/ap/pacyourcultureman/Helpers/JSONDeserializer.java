package com.ap.pacyourcultureman.Helpers;

import android.util.Log;

import com.ap.pacyourcultureman.Assignment;
import com.ap.pacyourcultureman.Player;
import com.ap.pacyourcultureman.PlayerGameStats;
import com.ap.pacyourcultureman.PlayerStats;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONDeserializer {
    Player player;
    private PlayerStats playerStats;
    private PlayerGameStats playerGameStats;
    public  JSONDeserializer() {};
    public Player setPlayer(String reply) {
        JSONObject jsObject;
        try {
            jsObject = new JSONObject(reply);
            int userId = jsObject.getInt("id");
            String jwt = jsObject.getString("token");
            String username = jsObject.getString("username");
            String firstName = jsObject.getString("firstName");
            String lastName = jsObject.getString("lastName");
            String email = jsObject.getString("email");
            int skinId = jsObject.getInt("skinId");

            JSONObject stats = jsObject.getJSONObject("stats");
            int totalScore = stats.getInt("totalScore");
            int totalSucces = stats.getInt("totalSucces");
            int totalFailed = stats.getInt("totalFailed");
            int totalLost = stats.getInt("totalLost");
            int highestScore = stats.getInt("highestScore");

            JSONObject gameStats = jsObject.getJSONObject("gameStats");
            int lifePoints = gameStats.getInt("lifePoints");
            int rifle = gameStats.getInt("rifle");
            int freezeGun = gameStats.getInt("freezeGun");
            int pushBackGun = gameStats.getInt("pushBackGun");
            Log.d("totalScore",gameStats.toString());
            PlayerStats playerStats = new PlayerStats(highestScore,totalScore,totalFailed,totalSucces,totalLost);
            PlayerGameStats playerGameStats = new PlayerGameStats(lifePoints,rifle,freezeGun,pushBackGun);
            player = new Player(userId,username,firstName,lastName,email,playerStats,playerGameStats,jwt,skinId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return player;
    }
    public List<Assignment> getAssignnments(JSONArray reply) {
       List<Assignment> assignments = new ArrayList<>();
        for (int i = 0; i < reply.length(); i++) {
            try {
                JSONObject jsonObject = reply.getJSONObject(i);
                String name = jsonObject.getString("name");
                String website = jsonObject.getString("website");
                String shortDesc = jsonObject.getString("shortDescription");
                String longDesc = jsonObject.getString("longDescription");
                String imgUrl = jsonObject.getString("sightImage");
                String lat = jsonObject.getString("latitude");
                String lng = jsonObject.getString("longitude");
                assignments.add(new Assignment(name, website, Double.valueOf(lng), Double.valueOf(lat), shortDesc, longDesc, imgUrl));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return assignments;
    }
}
