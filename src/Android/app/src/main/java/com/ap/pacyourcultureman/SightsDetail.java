package com.ap.pacyourcultureman;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ap.pacyourcultureman.Menus.NavigationMenu;
import com.squareup.picasso.Picasso;

import static com.ap.pacyourcultureman.Sights.DETAIL_IMAGE;
import static com.ap.pacyourcultureman.Sights.DETAIL_LAT;
import static com.ap.pacyourcultureman.Sights.DETAIL_LONG;
import static com.ap.pacyourcultureman.Sights.DETAIL_LONGD;
import static com.ap.pacyourcultureman.Sights.DETAIL_NAME;
import static com.ap.pacyourcultureman.Sights.DETAIL_SHORTD;
import static com.ap.pacyourcultureman.Sights.DETAIL_WEBSITE;

public class SightsDetail extends Activity implements View.OnClickListener {


    private String imageUrl;
    private String name;
    private String shortD;
    private String longD;
    private String web;
    private ImageView imageView;
    private TextView textViewName;
    private TextView textViewShort;
    private TextView textViewLong;
    private Button buttonMaps;
    private Button buttonweb;
    private Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sights_detail);

        Intent intent = getIntent();
        NavigationMenu navigationMenu = new NavigationMenu(this);
        imageUrl = intent.getStringExtra(DETAIL_IMAGE);
        name =  intent.getStringExtra(DETAIL_NAME);
        shortD = intent.getStringExtra(DETAIL_SHORTD);
        longD=  intent.getStringExtra(DETAIL_LONGD);
        web =  intent.getStringExtra(DETAIL_WEBSITE);

        imageView = findViewById(R.id.imageView_sight_detail);
        textViewName = findViewById(R.id.textView_sightName_detail);
        textViewShort = findViewById(R.id.textView_short_detail);
        textViewLong = findViewById(R.id.textView_long_detail);
        buttonMaps = findViewById(R.id.buttonGoogle_detail);
        buttonweb = findViewById(R.id.buttonWeb_detail);
        btnBack = findViewById(R.id.buttonBack);
        if (web.equals("N/A")){
            buttonweb.setVisibility(View.INVISIBLE);
        } else {
            buttonweb.setVisibility(View.VISIBLE);
        }


        Picasso.get().load(imageUrl).fit().centerInside().into(imageView);
        textViewName.setText(name);
        textViewShort.setText(shortD);
        textViewLong.setText(longD);

        buttonweb.setOnClickListener(this);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), Sights.class);
                startActivity(intent1);
                finish();
            }
        });
        addListenerOnButton();
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(web));
        startActivity(intent);
    }
    public void addListenerOnButton() {
        buttonMaps.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/search/?api=1&query="+name));
                startActivity(intent);
            }

        });

    }
}
