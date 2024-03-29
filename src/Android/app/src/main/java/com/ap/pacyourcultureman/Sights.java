package com.ap.pacyourcultureman;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.ap.pacyourcultureman.Helpers.ApiHelper;
import com.ap.pacyourcultureman.Helpers.SightsAdapter;
import com.ap.pacyourcultureman.Helpers.VolleyCallBack;
import com.ap.pacyourcultureman.Menus.NavigationMenu;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

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
    private Button button, btnBack;
    private Intent intent;
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
        NavigationMenu navigationMenu = new NavigationMenu(this);
        apiHelper = new ApiHelper();
        button = this.findViewById(R.id.btnReset);
        btnBack = this.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        addListenerOnButton();
        mRecyclerview = findViewById(R.id.recycler_view);
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
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
                            for (int j = 0; j <  ApiHelper.visitedSights.size(); j++) {
                                if(ApiHelper.visitedSights.get(j).getBuildingId() == ApiHelper.assignments.get(i).getId() && ApiHelper.visitedSights.get(j).isChecked()){
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
    public void onBackPressed() {
            finish();
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

    private void addListenerOnButton() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Dialog dialog = new Dialog(Sights.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_reset);
                dialog.setCancelable(true);
                final Button btn_dialog_reset = dialog.findViewById(R.id.btn_dialog_reset);
                final Button btn_dialog_cancel = dialog.findViewById(R.id.btn_dialog_cancel);
                dialog.show();
                btn_dialog_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btn_dialog_reset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        visitedSightsReset();
                        button.setEnabled(false);
                    }
                });
            }
        });}

    public void visitedSightsReset(){
        // jsonobject with json array put
        JSONObject object = new JSONObject();
        JSONArray array =new JSONArray();
        try {
            for (int i = 0; i <  ApiHelper.visitedSights.size(); i++){
                JSONObject objp = new JSONObject();
                objp.put("id",ApiHelper.visitedSights.get(i).getId());
                objp.put("buildingId",ApiHelper.visitedSights.get(i).getBuildingId());
                objp.put("isChecked",false);
                objp.put("userId",ApiHelper.visitedSights.get(i).getUserId());
                array.put(objp);
                ApiHelper.visitedSights.get(i).setChecked(false);}
            object.put("visitedSights",array);
            }
        catch (JSONException e) {
            e.printStackTrace();
        }
        apiHelper.put("https://aspcoreapipycm.azurewebsites.net/Users/updatevisitedsights/" + Integer.toString(ApiHelper.player.getId()), object, new VolleyCallBack() {
            @Override
            public void onSuccess() {
                finish();
            }
        });
    }
}
