package com.ap.pacyourcultureman;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.ap.pacyourcultureman.Helpers.ApiHelper;
import com.ap.pacyourcultureman.Helpers.SightsAdapter;
import com.ap.pacyourcultureman.Menus.NavigationMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Sights extends Activity implements SightsAdapter.OnItemClickListener {
    private RecyclerView mRecyclerview;
    private SightsAdapter mSighsAdapter;
    private ArrayList<Assignment> mSightList;
    private RequestQueue mRequestQueue;
    private ApiHelper apiHelper;
    public static final String DETAIL_IMAGE = "sightImage";
    public static final String DETAIL_NAME = "name";
    public static final String DETAIL_SHORTD = "shortDescription";
    public static final String DETAIL_LONGD = "longDescription";
    public static final String DETAIL_LAT = "latitude";
    public static final String DETAIL_LONG = "longitude";
    public static final String DETAIL_WEBSITE = "website";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sights_menu);
        apiHelper = new ApiHelper();
        mRecyclerview = findViewById(R.id.recycler_view);
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        NavigationMenu navigationMenu = new NavigationMenu(this);
        mSightList = new ArrayList<>();

        mRequestQueue = Volley.newRequestQueue(this);
        parseJSON();
    }

    private void parseJSON() {
        final String url = "https://aspcoreapipycm.azurewebsites.net/sights";
        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String name = jsonObject.getString("name");
                            String website = jsonObject.getString("website");
                            String shortDesc = jsonObject.getString("shortDescription");
                            String longDesc = jsonObject.getString("longDescription");
                            String imgUrl = jsonObject.getString("sightImage");
                            String lat = jsonObject.getString("latitude");
                            String lng = jsonObject.getString("longitude");
                            apiHelper.visitedSights.get(5).setChecked(true);
                            apiHelper.visitedSights.get(6).setChecked(true);
                            apiHelper.visitedSights.get(7).setChecked(true);
                            for (int j = 0; j <  apiHelper.assignments.size(); j++) {
                                if(apiHelper.visitedSights.get(j).getBuildingId() == apiHelper.assignments.get(i).getId() && apiHelper.visitedSights.get(j).isChecked() == true){
                                    mSightList.add(new Assignment(id,name, website, Double.valueOf(lng), Double.valueOf(lat), shortDesc, longDesc, imgUrl));
                                }
                            }


                        }
                        mSighsAdapter = new SightsAdapter(Sights.this,mSightList);
                        mRecyclerview.setAdapter(mSighsAdapter);
                        mSighsAdapter.setonItemClickListener(Sights.this);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(request);
    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this,SightsDetail.class);
        Assignment clickedSight = mSightList.get(position);
        detailIntent.putExtra(DETAIL_NAME,clickedSight.getName());
        detailIntent.putExtra(DETAIL_LONGD,clickedSight.getLongDescr());
        detailIntent.putExtra(DETAIL_SHORTD,clickedSight.getShortDescr());
        detailIntent.putExtra(DETAIL_IMAGE,clickedSight.getImgUrl());
        detailIntent.putExtra(DETAIL_WEBSITE,clickedSight.getWebsite());
        startActivity(detailIntent);
    }
}
