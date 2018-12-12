package com.ap.pacyourcultureman;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ap.pacyourcultureman.Helpers.ApiHelper;

public class Shop extends AppCompatActivity {
    TextView freezegun;
    TextView pushBack;
    TextView rifle;
    Player player;
    Button minFreeze,plusFreeze,minPushback,plusPushback,minRifle,plusRifle;
    int freezegunBullets,pushbackBullets,rifleBullets;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_page);
        freezegun = findViewById(R.id.freezeView);
        pushBack = findViewById(R.id.pushbackView);
        rifle = findViewById(R.id.rifleView);
        player = ApiHelper.player;
        minFreeze = findViewById(R.id.minFreeze);
        plusFreeze = findViewById(R.id.plusFreeze);
        minPushback = findViewById(R.id.minPushback);
        plusPushback = findViewById(R.id.plusPushback);
        minRifle = findViewById(R.id.minRifle);
        plusRifle = findViewById(R.id.plusRifle);
        freezegunBullets = 0;
        pushbackBullets =0;
        rifleBullets = 0;
        minFreeze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                freezegunBullets = freezegunBullets -1 ;

            }
        });
        plusFreeze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                freezegunBullets = freezegunBullets + 1;
            }
        });
        minPushback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushbackBullets = pushbackBullets -1;
            }
        });
        plusPushback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushbackBullets = pushbackBullets +1;
                
            }
        });
        minRifle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rifleBullets = rifleBullets - 1;
            }
        });
        plusRifle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rifleBullets = rifleBullets +1 ;
            }
        });
    }
}
