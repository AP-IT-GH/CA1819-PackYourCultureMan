package com.ap.pacyourcultureman.Helpers;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ap.pacyourcultureman.AppController;
import com.ap.pacyourcultureman.Assignment;
import com.ap.pacyourcultureman.GameActivity;
import com.ap.pacyourcultureman.Player;
import com.ap.pacyourcultureman.PlayerGameStats;
import com.ap.pacyourcultureman.PlayerStats;
import com.google.android.gms.common.api.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiHelper {
    String targetURL;
    HttpURLConnection conn;
    String resp = "";
    public String responseMessage;
    public Boolean run;
    static public List<Assignment> assignments;
    static public Player player;
    int userId;
    String jwt;
    public ApiHelper() {
    }
    public void sendPostRegister(final String urlstring, final String username, final String password, final String firstName, final String lastName, final String email) {
        run = true;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlstring);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
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
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("username", username);
                    jsonParam.put("password", password);
                    jsonParam.put("firstname", firstName);
                    jsonParam.put("lastname", lastName);
                    jsonParam.put("email", email);
                    jsonParam.put("stats",jsonPayerStats);
                    jsonParam.put("gameStats",jsonGameStats);

                    Log.i("JSON", jsonParam.toString());

                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    StringBuilder stringBuilder = new StringBuilder();
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
                        InputStreamReader streamReader = new InputStreamReader(conn.getErrorStream());
                        BufferedReader bufferedReader = new BufferedReader(streamReader);
                        String response = null;
                        while ((response = bufferedReader.readLine()) != null) {
                            stringBuilder.append(response + "\n");
                        }
                        bufferedReader.close();
                        resp = stringBuilder.toString();
                        resp = resp.substring(resp.indexOf(':') + 2, resp.lastIndexOf('"'));
                        Log.d("TAG", stringBuilder.toString());
                         run = false;
                    }
                    if(conn.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                        resp = "Server down";
                        run = false;
                    }
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        resp = "User created";
                        run = false;
                    }
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        resp = "Unauthorized";
                        run = false;
                    }
                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG", conn.getResponseMessage());
                    os.close();
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
       // run = false;
    }
    public void sendPostLogin(final String urlstring, final String username, final String password) {
        run = true;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlstring);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("username", username);
                    jsonParam.put("password", password);
                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());
                    os.flush();
                    StringBuilder stringBuilder = new StringBuilder();
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
                        InputStreamReader streamReader = new InputStreamReader(conn.getErrorStream());
                        BufferedReader bufferedReader = new BufferedReader(streamReader);
                        String response = null;
                        while ((response = bufferedReader.readLine()) != null) {
                            stringBuilder.append(response + "\n");
                        }
                        bufferedReader.close();
                        Log.d("TAG" ,stringBuilder.toString());
                        resp = stringBuilder.toString();
                        resp= resp.substring(resp.indexOf(':') + 2, resp.lastIndexOf('"'));
                        Log.i("Reply", resp);
                        run = false;
                    }
                    if(conn.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                        resp = "Server down";
                        run = false;
                    }
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        String reply;
                        InputStream in = conn.getInputStream();
                        StringBuffer sb = new StringBuffer();
                        try {
                            int chr;
                            while ((chr = in.read()) != -1) {
                                sb.append((char) chr);
                            }
                            reply = sb.toString();
                        } finally {
                            in.close();
                        }

                        JSONObject jsObject = new JSONObject(reply);
                        userId = jsObject.getInt("id");
                        jwt = jsObject.getString("token");
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

                        resp = "Login success";
                        run = false;
                    }
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        resp= "Unauthorized";
                        run = false;
                    }
                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG" , conn.getResponseMessage());
                    os.close();
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
    public void getAssignments() {
        run = true;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String url = "https://aspcoreapipycm.azurewebsites.net/Sights";
                    JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            assignments = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject jsonObject = response.getJSONObject(i);
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
                            Log.v("Data from the web: ", response.toString());
                            Log.d("Finish", "end");
                            responseMessage = "end";
                            run = false;
                        }

                    }, new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d("MainActivity", error.getMessage());
                        }
                    }){@Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Authorization", "Bearer "+ player.getJwt());
                        Log.d("xxx",player.getJwt());
                        return params;
                    }
                    };
                    AppController.getInstance().addToRequestQueue(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
    public void getUser(int userId){
        run = true;
        int _userId = userId;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    final String url = "https://aspcoreapipycm.azurewebsites.net/Sights";
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
    public void getStats() {

    }
    public HttpURLConnection getConn() {
        return this.conn;
    }
    public String getResponse() {
        return resp;
    }
    public String getJwt(){
        return jwt;
    }
    public int getUserId(){
        return userId;
    }

    public void updateStats(final int _userId,final int _highestScore,final int _totalScore,final int _totalSucces,final int _totalLost,final int _totalFailed){
        run = true;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final String url = "https://aspcoreapipycm.azurewebsites.net/Users/updatestats/" + Integer.toString(_userId);
                final JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("highestScore", _highestScore);
                    jsonObject.put("totalScore", _totalScore);
                    jsonObject.put("totalSucces", _totalSucces);
                    jsonObject.put("totalFailed", _totalFailed);
                    jsonObject.put("totalLost", _totalLost);
                } catch (JSONException e) {
                    // handle exception
                }
                JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // response
                                Log.d("Response", response.toString());
                                run = false;
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Log.d("Error.Response", error.toString());
                                run = false;
                            }
                        }

                ) {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type", "application/json");
                        params.put("Accept", "application/json");
                        params.put("Authorization", "Bearer " + player.getJwt());
                        Log.d("xxx", player.getJwt());
                        return params;
                    }
                };
                AppController.getInstance().addToRequestQueue(putRequest);

            }
        });
        thread.start();
    }
    public void updatePlayer(final int _userId,final String _firstName,final String _lastName,final int _skinId,final String _password,final String _email){
        run = true;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final String url = "https://aspcoreapipycm.azurewebsites.net/Users/updateuser/" + Integer.toString(_userId);
                final JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("skinId", _skinId);
                    jsonObject.put("firstName", _firstName);
                    jsonObject.put("lastName", _lastName);
                    jsonObject.put("email", _email);
                    jsonObject.put("password",_password);
                    Log.d("xx",jsonObject.toString());
                } catch (JSONException e) {
                    // handle exception
                }
                JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // response
                                Log.d("Response", response.toString());
                                run = false;
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Log.d("Error.Response", error.toString());
                                run = false;
                            }
                        }

                ) {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type", "application/json");
                        params.put("Accept", "application/json");
                        params.put("Authorization", "Bearer " + player.getJwt());
                        Log.d("xxx", player.getJwt());
                        return params;
                    }
                    @Override
                    public String getBodyContentType() {
                        return "application/json";
                    }
                };
                AppController.getInstance().addToRequestQueue(putRequest);

            }
        });
        thread.start();
    }
}

