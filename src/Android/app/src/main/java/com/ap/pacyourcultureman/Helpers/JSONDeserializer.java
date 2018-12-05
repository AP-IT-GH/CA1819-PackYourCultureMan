package com.ap.pacyourcultureman.Helpers;

import android.util.JsonReader;
import android.util.Log;

import com.ap.pacyourcultureman.Assignment;
import com.ap.pacyourcultureman.Dot;
import com.ap.pacyourcultureman.Player;
import com.ap.pacyourcultureman.PlayerGameStats;
import com.ap.pacyourcultureman.PlayerStats;
import com.ap.pacyourcultureman.Step;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONDeserializer {
    Player player;
    private PlayerStats playerStats;
    private PlayerGameStats playerGameStats;

    public JSONDeserializer() {
    }

    ;

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
            Log.d("totalScore", gameStats.toString());
            PlayerStats playerStats = new PlayerStats(highestScore, totalScore, totalFailed, totalSucces, totalLost);
            PlayerGameStats playerGameStats = new PlayerGameStats(lifePoints, rifle, freezeGun, pushBackGun);
            player = new Player(userId, username, firstName, lastName, email, playerStats, playerGameStats, jwt, skinId);
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

    public List<Dot> getDots(JSONArray reply) {
        List<Dot> dots = new ArrayList<>();
        for (int i = 0; i < reply.length(); i++) {
            try {
                JSONObject jsonObject = reply.getJSONObject(i);
                Integer id = jsonObject.getInt("id");
                Double lat = jsonObject.getDouble("latitude");
                Double lng = jsonObject.getDouble("longitude");
                Boolean taken = jsonObject.getBoolean("taken");
                dots.add(new Dot(id, lat, lng, taken));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return dots;
    }


    public List<Dot> corrected(JSONObject response){
        Log.d("GoogleRoadsApi", "1 object");
        Log.d("GoogleRoadsApi", response.toString());
        List<Dot> dotsRoad = new ArrayList<>();
        try {
            JSONArray snappedPoints = response.getJSONArray("snappedPoints");
            for (int i = 0; i < snappedPoints.length(); i++) {
                JSONObject location = snappedPoints.getJSONObject(i);
                JSONObject loc = location.getJSONObject("location");
                Double lat = loc.getDouble("latitude");
                Double lng = loc.getDouble("longitude");
                Log.d("GoogleRoadsApi", lat + "," + lng);
                dotsRoad.add(new Dot(lat,lng));
                //for (int j = 0; j < dotsRoad.size(); j++) {  Log.d("roadscheck3", dotsRoad.get(i).getLat()+","+dotsRoad.get(i).getLon());}
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dotsRoad;
    }

    public List<Step> getSteps(JSONObject reply)  {
        List<Step> steps = new ArrayList<>();
        try {
            JSONArray array = reply.getJSONArray("routes");
            JSONObject route = array.getJSONObject(0);
            JSONArray jsonArray = route.getJSONArray("legs");
            JSONObject legObject = jsonArray.getJSONObject(0);
            JSONArray stepssArray = legObject.getJSONArray("steps");
            Log.d("Steps", stepssArray.toString());

            Step step;
            Log.d("Steps", Integer.toString(stepssArray.length()));
            for (int i = 0; i < stepssArray.length(); i++) {
                JSONObject endObject = stepssArray.getJSONObject(i);
                JSONObject distanceJSON = endObject.getJSONObject("distance");
                JSONObject startJSON = endObject.getJSONObject("start_location");
                JSONObject endJSON = endObject.getJSONObject("end_location");

                int distance = distanceJSON.getInt("value");
                LatLng start = new LatLng(startJSON.getDouble("lat"), startJSON.getDouble("lng"));
                LatLng end = new LatLng(endJSON.getDouble("lat"), startJSON.getDouble("lng"));

                step = new Step(start, end, distance);
                steps.add(step);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("Steps", steps.toString());
        return steps;
    }

}
