package com.ap.pacyourcultureman.Helpers;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.ap.pacyourcultureman.AppController;
import com.ap.pacyourcultureman.Assignment;
import com.ap.pacyourcultureman.Dot;
import com.ap.pacyourcultureman.Player;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okio.ByteString;

public class ApiHelper {
    String targetURL;
    HttpURLConnection conn;
    String resp = "";
    public String responseMessage;
    public Boolean run;
    static public List<Assignment> assignments;
    static public List<Dot> dots;
    static public Player player;
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
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("username", username);
                    jsonParam.put("password", password);
                    jsonParam.put("firstname", firstName);
                    jsonParam.put("lastname", lastName);
                    jsonParam.put("email", email);
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
                        String[] parts = reply.split(",");
                        for(int i = 0; i < parts.length; i++) {
                            if(i == 0) parts[i] = parts[i].substring(parts[i].indexOf(':') + 1, parts[i].length());
                            else {
                                parts[i] = parts[i].substring(parts[i].indexOf(':') + 2, parts[i].length() - 1);
                            }
                            Log.d("PARTS", parts[i]);
                        }
                 //       player = new Player(Integer.valueOf(parts[0]), parts[1], parts[2], parts[3], parts[4]);
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
                    final String url = "https://api.myjson.com/bins/1ekc7i";
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
                    });
                    AppController.getInstance().addToRequestQueue(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void getDots() {
        run = true;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String url = "https://api.myjson.com/bins/6r9im";
                    JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            dots = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    Integer id = jsonObject.getInt("id");
                                    Double lat = jsonObject.getDouble("latitude");
                                    Double lng = jsonObject.getDouble("longitude");
                                    Boolean taken = jsonObject.getBoolean("taken");
                                    dots.add(new Dot(id,lat,lng,taken ));
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
                    });
                    AppController.getInstance().addToRequestQueue(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }



    public void getStats() {

    }
    public HttpURLConnection getConn() {
        return this.conn;
    }
    public String getResponse() {
        return resp;
    }
}
