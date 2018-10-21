package com.ap.pacyourcultureman.Helpers;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.common.api.Api;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiHelper {
    String targetURL;
    URL url;
    HttpURLConnection conn;
    String resp;
    String username, password, firstName, lastName, email;
    public Boolean running;
    public ApiHelper(String targetURL) {
        this.targetURL = targetURL;
        running = false;
    }
    public void sendPost() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    running = true;
                    url = new URL(targetURL);
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
        running = false;
    }
    public void setRegister ( String username, String email, String password, String firstName, String lastName ) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public HttpURLConnection getConn() {
        return this.conn;
    }
    public String getResponse() {
        return resp;
    }
}
