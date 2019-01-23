package com.ap.pacyourcultureman;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ap.pacyourcultureman.Helpers.ApiHelper;
import com.ap.pacyourcultureman.Helpers.JSONDeserializer;
import com.ap.pacyourcultureman.Helpers.VolleyCallBack;
import com.ap.pacyourcultureman.Menus.NavigationMenu;

import org.json.JSONException;
import org.json.JSONObject;
import static com.ap.pacyourcultureman.Helpers.GetDotsBetween2Points.GetDotsBetweenAanB;


public class DevOptions extends Activity{

    public static Button button;
    private ApiHelper apiHelper;
    private int counter,urlCounter,urlSize;
    private int posted,deleted;
    private ProgressBar progressBar;
    private Intent intent;
    private TextView textView,txtDeleted,txtPosted,txtGenerated,txtCorrected;
    private EditText set_distDots,set_distAssign,set_distCoins;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dev_options);
        init();
    }

    private void init(){
    apiHelper = new ApiHelper();
    counter  = 0;
    urlCounter = 0;
    urlSize = 1;
    posted = 0;
    deleted = 0;
    progressBar = findViewById(R.id.progressBar);
    progressBar.setVisibility(View.INVISIBLE);
    button = findViewById(R.id.button_update);
    textView =findViewById(R.id.txt_update);
    textView.setVisibility(View.INVISIBLE);
    txtCorrected =findViewById(R.id.txt_corrected);
    txtCorrected.setVisibility(View.INVISIBLE);
    txtDeleted =findViewById(R.id.txt_deleted);
    txtDeleted.setVisibility(View.INVISIBLE);
    txtGenerated =findViewById(R.id.txt_generated);
    txtGenerated.setVisibility(View.INVISIBLE);
    txtPosted =findViewById(R.id.txt_posted);
    txtPosted.setVisibility(View.INVISIBLE);
    set_distAssign = findViewById(R.id.dev_txt_assignments);
    set_distCoins = findViewById(R.id.dev_txt_coins);
    set_distDots = findViewById(R.id.dev_txt_distdots);
    addListenerOnButton(); }

    private void addListenerOnButton() {
     button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            progressBar.setVisibility(View.VISIBLE);
            deleteDots();
            correctDots();
            button.setEnabled(false);
            button.setVisibility(View.INVISIBLE);
        }
    });}


    private void deleteDots(){
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
                        deleted ++;
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

    private void correctAndPostDots(){
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

    private void postDots(){

        String URL =  "https://aspcoreapipycm.azurewebsites.net/Dot";
        try {
            for (int i = 0; i <  ApiHelper.correctedDots.size(); i++){
                JSONObject objp = new JSONObject();
                objp.put("latitude",i);
                objp.put("latitude",ApiHelper.correctedDots.get(i).getLat());
                objp.put("longitude",ApiHelper.correctedDots.get(i).getLon());
                objp.put( "taken",ApiHelper.correctedDots.get(i).getTaken());
                apiHelper.post(URL, objp);
                posted ++;
            }
           updateDone();
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

    private void updateDone(){
        textView.setText("update dots done ! ");
        txtPosted.setText("Posted Dots: "+posted);
        txtDeleted.setText("Deleted Dots: "+deleted);
        txtGenerated.setText("Generated Dots "+ ApiHelper.generatedDots.size());
        txtCorrected.setText("Corrected Dots "+ ApiHelper.correctedDots.size());
        deleted = 0;
        posted = 0;
        textView.setTextColor(Color.RED);
        progressBar.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.VISIBLE);
        txtCorrected.setVisibility(View.VISIBLE);
        txtGenerated.setVisibility(View.VISIBLE);
        txtDeleted.setVisibility(View.VISIBLE);
        txtPosted.setVisibility(View.VISIBLE);
        GameActivity.dist_assignment = Integer.parseInt(set_distAssign.getText().toString());
        GameActivity.dist_dots = Integer.parseInt(set_distDots.getText().toString());
        GameActivity.dist_coins = Integer.parseInt(set_distCoins.getText().toString());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavigationMenu.resetStaticLists();
        intent = new Intent(this.getBaseContext(),Login.class);
        this.startActivity(intent);

    }

}