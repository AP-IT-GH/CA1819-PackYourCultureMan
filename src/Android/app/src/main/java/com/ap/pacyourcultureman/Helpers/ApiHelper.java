package com.ap.pacyourcultureman.Helpers;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.ap.pacyourcultureman.Assignment;
import com.ap.pacyourcultureman.Dot;
import com.ap.pacyourcultureman.Player;
import com.ap.pacyourcultureman.Street;
import com.ap.pacyourcultureman.VisitedSight;
import com.google.android.gms.maps.model.LatLng;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static java.util.Collections.*;

public class ApiHelper {
    HttpURLConnection conn;
    String resp = "";
    public String responseMessage;
    public Boolean run;
    static public List<Dot> dots = new ArrayList<>();
    static public List<Street> streets = new ArrayList<>();
    static public List<Assignment> assignments = new ArrayList<>();
    static public List<Dot> correctedDots = new ArrayList<>();
    static public List<Dot> generatedDots = new ArrayList<>();
    static public List<VisitedSight> visitedSights = new ArrayList<>();
    static public Player player;
    int userId;
    String jwt;
    String reply;
    int mStatusCode;
    JSONArray jsonArray;
    JSONObject jsonObject;
    Thread thread;

    public ApiHelper() {
    }
    public void sendPost(final String urlstring, final JSONObject jsonObject, final VolleyCallBack callBack) {
        thread = new Thread(new Runnable() {
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
                    Log.i("JSON", jsonObject.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(jsonObject.toString());
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
                        callBack.onSuccess();

                    }
                    if(conn.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                        resp = "Server down";
                        callBack.onSuccess();

                    }
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        resp = "Success";
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
                        Log.d("LOGIN", reply);
                        callBack.onSuccess();

                    }
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        resp = "Unauthorized";
                        callBack.onSuccess();

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
    public void getArray(final String url, final VolleyCallBack callBack) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.v("Data from the web: ", response.toString());
                            Log.d("Finish", "end");
                            jsonArray = response;
                            responseMessage = "end";
                            callBack.onSuccess();
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
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void get(final String url, final VolleyCallBack callBack) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        Log.v("Data from the web ", response.toString());
                        Log.d("Finish", "end");
                        try {
                            jsonObject = new JSONObject(response.toString());

                            callBack.onSuccess();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Failure Callback
                    }
                });
        AppController.getInstance().addToRequestQueue(jsonObjReq);
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
    public String getReply() {return reply;}
    public JSONArray getJsonArray() {return jsonArray;}
    public JSONObject getJsonObject() {return jsonObject;}
    public int getmStatusCode() {return mStatusCode;}
    public void setPlayer(String reply) {
        JSONDeserializer jsonDeserializer = new JSONDeserializer();
        player = jsonDeserializer.setPlayer(reply);
    }
    public int getUserId(){
        return userId;
    }

    public void put(final String url, final JSONObject jsonObject){
        run = true;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("Response", response.toString());
                                run = false;
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Error.Response", error.toString());
                                run = false; }
                        }
                )
                {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type", "application/json");
                        params.put("Accept", "application/json");
                        params.put("Authorization", "Bearer " + ApiHelper.player.getJwt());
                        Log.d("xxx", ApiHelper.player.getJwt());
                        return params;
                    }
                    @Override
                    protected  Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                        mStatusCode = response.statusCode;
                        Log.d("Status", Integer.toString(mStatusCode));
                        return super.parseNetworkResponse(response);
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

    public void post(final String url, final JSONObject jsonObject){
        run = true;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("Response", response.toString());
                                run = false;
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Error.Response", error.toString());
                                run = false; }
                        }
                )
                {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type", "application/json");
                        params.put("Accept", "application/json");
                        return params;
                    }
                    @Override
                    protected  Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                        mStatusCode = response.statusCode;
                        Log.d("Status", Integer.toString(mStatusCode));
                        return super.parseNetworkResponse(response);
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
    public void delete(final String url, final JSONObject jsonObject){
        run = true;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.DELETE, url, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("Response", response.toString());
                                run = false;
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Error.Response", error.toString());
                                run = false; }
                        }
                )
                {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type", "application/json");
                        params.put("Accept", "application/json");
                        return params;
                    }
                    @Override
                    protected  Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                        mStatusCode = response.statusCode;
                        Log.d("Status", Integer.toString(mStatusCode));
                        return super.parseNetworkResponse(response);
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

