package com.ap.pacyourcultureman;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Response;
import com.ap.pacyourcultureman.Helpers.ApiHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DevOptions extends Activity{

    private Button button;
    private Context mContext;
    private ApiHelper apiHelper;
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
            postArrayDots();
        }
    });}




    public void postArrayDots(){



        //Create json array for filter
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

        String url =" https://aspcoreapipycm.azurewebsites.net/Dot/postarray";
        

    }
}