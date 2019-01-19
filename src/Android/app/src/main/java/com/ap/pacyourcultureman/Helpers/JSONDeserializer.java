package com.ap.pacyourcultureman.Helpers;

import android.util.JsonReader;
import android.util.Log;

import com.ap.pacyourcultureman.Assignment;
import com.ap.pacyourcultureman.Dot;
import com.ap.pacyourcultureman.Player;
import com.ap.pacyourcultureman.PlayerGameStats;
import com.ap.pacyourcultureman.PlayerStats;
import com.ap.pacyourcultureman.Street;
import com.ap.pacyourcultureman.Step;
import com.ap.pacyourcultureman.VisitedSight;
import com.google.android.gms.maps.model.LatLng;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JSONDeserializer {
    Player player;
    private PlayerStats playerStats;
    private PlayerGameStats playerGameStats;
    public JSONDeserializer() {
    }



    public Player setPlayer(String reply) {
        JSONObject jsObject;
        try {
            jsObject = new JSONObject(reply);
            String jwt = jsObject.getString("token");
            JSONObject jsUser = jsObject.getJSONObject("user");
            int userId = jsUser.getInt("Id");
            String username = jsUser.getString("Username");
            String firstName = jsUser.getString("FirstName");
            String lastName = jsUser.getString("LastName");
            String email = jsUser.getString("Email");
            int skinId = jsUser.getInt("skinId");
            JSONObject stats = jsUser.getJSONObject("Stats");
            int totalScore = stats.getInt("totalScore");
            int totalSucces = stats.getInt("totalSucces");
            int totalFailed = stats.getInt("totalFailed");
            int totalLost = stats.getInt("totalLost");
            int highestScore = stats.getInt("highestScore");
            JSONObject gameStats = jsUser.getJSONObject("gameStats");
            int lifePoints = gameStats.getInt("lifePoints");
            int rifle = gameStats.getInt("rifle");
            int freezeGun = gameStats.getInt("freezeGun");
            int pushBackGun = gameStats.getInt("pushBackGun");
            int coins = gameStats.getInt("coins");
            JSONArray jsUserSights = jsUser.getJSONArray("visitedSights");
            for(int i = 0; i < jsUserSights.length(); i++) {
                JSONObject visitedSight = jsUserSights.getJSONObject(i);
                int id = visitedSight.getInt("id");
                int buildingId = visitedSight.getInt("buildingId");
                Boolean isChecked = visitedSight.getBoolean("isChecked");
                ApiHelper.visitedSights.add(new VisitedSight(id,buildingId,userId,isChecked));
            }
            PlayerStats playerStats = new PlayerStats(highestScore, totalScore, totalFailed, totalSucces, totalLost);
            PlayerGameStats playerGameStats = new PlayerGameStats(lifePoints, rifle, freezeGun, pushBackGun,coins);
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
                int id = jsonObject.getInt("id");
                String name = jsonObject.getString("name");
                String website = jsonObject.getString("website");
                String shortDesc = jsonObject.getString("shortDescription");
                String longDesc = jsonObject.getString("longDescription");
                String imgUrl = jsonObject.getString("sightImage");
                String lat = jsonObject.getString("latitude");
                String lng = jsonObject.getString("longitude");
                assignments.add(new Assignment(id,name, website, Double.valueOf(lng), Double.valueOf(lat), shortDesc, longDesc, imgUrl));
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

    public List<Street> getSreets (JSONArray reply) {
        List<Street> streets = new ArrayList<>();
        for (int i = 0; i < reply.length(); i++) {
            try {
                JSONObject jsonObject = reply.getJSONObject(i);
                Integer id = jsonObject.getInt("id");
                Double latA = jsonObject.getDouble("latitudeA");
                Double lonA = jsonObject.getDouble("longitudeA");
                Double latB = jsonObject.getDouble("latitudeB");
                Double lonB = jsonObject.getDouble("longitudeB");
                streets.add(new Street(id,latA,lonA,latB,lonB));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return streets;
    }

    public List<Dot> correctedDots(JSONObject response){
        List<Dot> dotsRoad = new ArrayList<>();
        try {
            JSONArray snappedPoints = response.getJSONArray("snappedPoints");
            for (int i = 0; i < snappedPoints.length(); i++) {
                JSONObject location = snappedPoints.getJSONObject(i);
                JSONObject loc = location.getJSONObject("location");
                Double lat = loc.getDouble("latitude");
                Double lng = loc.getDouble("longitude");
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
            boolean firstStep = true;
            JSONArray array = reply.getJSONArray("routes");
            JSONObject route = array.getJSONObject(0);
            JSONArray jsonArray = route.getJSONArray("legs");
            JSONObject legObject = jsonArray.getJSONObject(0);
            JSONArray stepssArray = legObject.getJSONArray("steps");
            Log.d("Steps", stepssArray.toString());

            Step step;
            Log.d("Steps", Integer.toString(stepssArray.length()));
            for (int i = 0; i < stepssArray.length(); i++) {
                if (i == 1 && firstStep){
                    i = 0;
                    firstStep = false;
                }
/*                if (i == stepssArray.length() - 1) {
                    JSONObject endObject = stepssArray.getJSONObject(i);
                    JSONObject distanceJSON = endObject.getJSONObject("distance");
                    JSONObject startJSON = endObject.getJSONObject("start_location");
                    JSONObject endJSON = endObject.getJSONObject("end_location");

                    int distance = distanceJSON.getInt("value");
                    LatLng end = new LatLng(startJSON.getDouble("lat"), startJSON.getDouble("lng"));
                    LatLng start = new LatLng(endJSON.getDouble("lat"), startJSON.getDouble("lng"));

                    step = new Step(start, end, distance);
                    steps.add(step);
                }*/
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
        Log.d("Movement", steps.toString());
        return steps;
    }

}
