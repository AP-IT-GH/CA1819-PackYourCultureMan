package com.ap.pacyourcultureman;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.ap.pacyourcultureman.Helpers.ApiHelper;
import com.ap.pacyourcultureman.Helpers.JSONDeserializer;
import com.ap.pacyourcultureman.Helpers.VolleyCallBack;
import org.json.JSONException;
import org.json.JSONObject;
import static com.ap.pacyourcultureman.Helpers.getDotsBetween2Points.GetDotsBetweenAanB;


public class DevOptions extends Activity{

    public static Button button;
    private ApiHelper apiHelper;
    private int counter,urlCounter,urlSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dev_options);
        apiHelper = new ApiHelper();
        counter  = 0;
        urlCounter = 0;
        urlSize = 1;
        button = findViewById(R.id.button_update);
        addListenerOnButton();
    }


    public void addListenerOnButton() {
     button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            deleteDots();
            correctDots();
            button.setEnabled(false);
        }
    });}


    public void deleteDots(){
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i <  ApiHelper.dots.size(); i++){
                        JSONObject objp = new JSONObject();
                        objp.put("latitude",ApiHelper.dots.get(i).getLat());
                        objp.put("longitude",ApiHelper.dots.get(i).getLon());
                        objp.put( "taken",ApiHelper.dots.get(i).getTaken());
                        String URL =  "https://aspcoreapipycm.azurewebsites.net/Dot";
                        apiHelper.delete(URL +"/"+ ApiHelper.dots.get(i).getId(), objp);
                    }
                    ApiHelper.dots.clear();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread1.start();

        //delete with truncate table dbo.dots
    }
    private  void streetsGenerate(){
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

    private  void correctDots(){
     Thread thread2 = new Thread(new Runnable() {
         @Override
         public void run() {
             try {
                 String url = "https://aspcoreapipycm.azurewebsites.net/street";
                 apiHelper.getArray(url, new VolleyCallBack() {
                     @Override
                     public void onSuccess() {
                         JSONDeserializer jsonDeserializer = new JSONDeserializer();
                         ApiHelper.streets = jsonDeserializer.getSreets(apiHelper.getJsonArray());
                         streetsGenerate();
                         correctAndPostDots();
                         Log.d("generatedDots", String.valueOf(ApiHelper.generatedDots.size() +" "+ ApiHelper.generatedDots.size()/urlSize ));
                     }
                 });
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }
     });
     thread2.start();
 }

    private  void correctAndPostDots(){
        final String URL = linkGenerator();
        Thread thread3 = new Thread(new Runnable() {
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
                                correctAndPostDots();
                            }
                            if (counter ==  ApiHelper.generatedDots.size()/urlSize){
                                postDots();
                            }

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread3.start();
    }

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
                Log.d("post", String.valueOf(objp));
            }
        } catch (JSONException e) {
            e.printStackTrace();
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



}