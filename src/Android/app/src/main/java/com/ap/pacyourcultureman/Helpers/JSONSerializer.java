package com.ap.pacyourcultureman.Helpers;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONSerializer {
    public JSONSerializer() {}
    public JSONObject jsonPostRegister(String username,String password, String firstName, String lastName,String email) {
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonPayerStats = new JSONObject();
        try {
            jsonPayerStats.put("highestScore", 0);
            jsonPayerStats.put("totalScore", 0);
            jsonPayerStats.put("totalFailed", 0);
            jsonPayerStats.put("totalSucces", 0);
            jsonPayerStats.put("totalLost", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject jsonGameStats = new JSONObject();
        try {
            jsonGameStats.put("lifePoints", 2);
            jsonGameStats.put("rifle", 0);
            jsonGameStats.put("pushBackGun", 0);
            jsonGameStats.put("freezeGun", 0);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            jsonParam.put("username", username);
            jsonParam.put("password", password);
            jsonParam.put("firstname", firstName);
            jsonParam.put("lastname", lastName);
            jsonParam.put("email", email);
            jsonParam.put("stats",jsonPayerStats);
            jsonParam.put("gameStats",jsonGameStats);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonParam;
    }
    public JSONObject jsonPostLogin(String username, String password) {
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("username", username);
            jsonParam.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonParam;
    }
    public JSONObject jsonPutStats(int userId, int highestScore, int totalScore, int totalSucces, int totalLost, int totalFailed) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", userId);
            jsonObject.put("highestScore", highestScore);
            jsonObject.put("totalScore", totalScore);
            jsonObject.put("totalSucces", totalSucces);
            jsonObject.put("totalFailed", totalFailed);
            jsonObject.put("totalLost", totalLost);
            Log.d("JSONUserStats",jsonObject.toString());
        } catch (JSONException e) {
            // handle exception
        }
        return jsonObject;
    }
    public JSONObject jsonPutGameStats(int lifePoints, int rifle, int freezeGun, int pushBackGun) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("lifePoints", lifePoints);
            jsonObject.put("rifle", rifle);
            jsonObject.put("freezeGun", freezeGun);
            jsonObject.put("pushBackGun", pushBackGun);
            Log.d("JSONGameStats",jsonObject.toString());

        } catch (JSONException e) {
        }
        return jsonObject;
    }
    public JSONObject jsonPutUserData(int skinID, String firstName, String lastName, String email, String password) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("skinId", skinID);
            jsonObject.put("firstName", firstName);
            jsonObject.put("lastName", lastName);
            jsonObject.put("email", email);
            jsonObject.put("password",password);
            Log.d("JSONUserData",jsonObject.toString());
        } catch (JSONException e) {
            // handle exception
        }
        return jsonObject;
    }
}
