package com.ap.pacyourcultureman;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ap.pacyourcultureman.Helpers.ApiHelper;
import com.ap.pacyourcultureman.Helpers.JSONDeserializer;
import com.ap.pacyourcultureman.Helpers.VolleyCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static com.ap.pacyourcultureman.Helpers.getDotsBetween2Points.GetDotsBetweenAanB;


public class DevOptions extends Activity{

    private Button button;
    private Context mContext;
    private ApiHelper apiHelper;
    int counter  = 0;
    int urlCounter = 0;
    int urlSize = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dev_options);
        apiHelper = new ApiHelper();
        button = findViewById(R.id.button_update);
        mContext = this;
        addListenerOnButton();
    }



    public void addListenerOnButton() {
     button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
           // deleteDots();
            postDots();
        }
    });}




    public void postDots(){

        String URL =  "https://aspcoreapipycm.azurewebsites.net/Dot";
        try {
            for (int i = 0; i <  ApiHelper.correctedDots.size(); i++){
                JSONObject objp = new JSONObject();
                objp.put("latitude",i);
                objp.put("latitude",ApiHelper.correctedDots.get(i).getLat());
                objp.put("longitude",ApiHelper.correctedDots.get(i).getLon());
                objp.put( "taken",ApiHelper.correctedDots.get(i).getTaken());
                apiHelper.post(URL, objp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

}

    public void deleteDots(){

      try {
            for (int i = 0; i <  ApiHelper.correctedDots.size(); i++){
                JSONObject objp = new JSONObject();
                objp.put("latitude",ApiHelper.correctedDots.get(i+1).getLat());
                objp.put("longitude",ApiHelper.correctedDots.get(i+1).getLon());
                objp.put( "taken",ApiHelper.correctedDots.get(i+1).getTaken());
                String URL =  "https://aspcoreapipycm.azurewebsites.net/Dot";
                apiHelper.delete(URL +"/"+ i+1, objp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

/*    private  void correctDots(){
        final String URL = linkGenerator();
        Thread thread4 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    apiHelper.get(URL, new VolleyCallBack() {
                        @Override
                        public void onSuccess() {
                            JSONDeserializer jsonDeserializer = new JSONDeserializer();
                            ApiHelper.correctedDots.addAll(jsonDeserializer.correctedDots(apiHelper.getJsonObject()));
                            counter++;
                            if (counter < ApiHelper.generatedDots.size()/urlSize ){
                                Log.d("correctedDots", String.valueOf(ApiHelper.correctedDots.size()));
                                correctDots();
                            } else  {
                                run3 = true;
                                startGame();
                            }


                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread4.start();
        // }
    }*/

    private  void streetsGenerate(){
        //for (int i = 0; i < ApiHelper.dotStreets.size(); i+=2) {
        //GetDotsBetweenAanB(ApiHelper.dotStreets.get(i).getLat(),ApiHelper.dotStreets.get(i).getLon(),ApiHelper.dotStreets.get(i+1).getLat(),ApiHelper.dotStreets.get(i+1).getLon(),ApiHelper.generatedDots);}
        for (int i = 0; i < ApiHelper.streets.size(); i++) {
            GetDotsBetweenAanB(ApiHelper.streets.get(i).getLatA(),ApiHelper.streets.get(i).getLonA(),ApiHelper.streets.get(i).getLatB(),ApiHelper.streets.get(i).getLonB(),ApiHelper.generatedDots);}
    }

    private String linkGenerator(){
        String getItem = "";
        for(int i = 0; i < urlSize  ; i++) {
            getItem += ApiHelper.generatedDots.get(urlCounter).getLat()+","+ApiHelper.generatedDots.get(urlCounter).getLon();
            if (i < urlSize-1){
                getItem += "|";
            }
            urlCounter ++;
        }
        String key = BuildConfig.GoogleSecAPIKEY;
        String URL = "https://roads.googleapis.com/v1/snapToRoads?path="+getItem+"&interpolate=false&key="+key;
        Log.d("link",URL);
        return URL;
    }


/*        //Create json array for filter
        final JSONArray array = new JSONArray();
        try {
            for (int i = 0; i <  ApiHelper.correctedDots.size(); i++){
                JSONObject objp = new JSONObject();
                objp.put("latitude",ApiHelper.correctedDots.get(i).getLat());
                objp.put("longitude",ApiHelper.correctedDots.get(i).getLon());
                objp.put( "taken",ApiHelper.correctedDots.get(i).getTaken());
                array.put(objp); }
                Log.d("test",array.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String URL =  "https://aspcoreapipycm.azurewebsites.net/Dot/postarray";*/

}