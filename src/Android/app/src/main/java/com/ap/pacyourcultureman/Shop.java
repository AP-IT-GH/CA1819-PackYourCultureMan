package com.ap.pacyourcultureman;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ap.pacyourcultureman.Helpers.ApiHelper;

public class Shop extends AppCompatActivity {
    TextView freezeguncnt,pushBackcnt,riflecnt,freezeView,pushbackView,rifleView;
    Player player;
    ApiHelper apiHelper;
    Button minFreeze,plusFreeze,minPushback,plusPushback,minRifle,plusRifle,buybtn;
    int cntfreezegunBullets,cntpushbackBullets,cntrifleBullets,_FreezegunBullets,_PushbackGunBullets,_RifleBullets;
    PlayerGameStats _playerGameStats,playerGameStats;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_page);
        freezeguncnt = findViewById(R.id.freezeView);
        pushBackcnt = findViewById(R.id.pushbackView);
        riflecnt = findViewById(R.id.rifleView);
        freezeView = findViewById(R.id.currentFreeze);
        pushbackView = findViewById(R.id.currentPush);
        rifleView = findViewById(R.id.currentRifle);

        minFreeze = findViewById(R.id.minFreeze);

        plusFreeze = findViewById(R.id.plusFreeze);
        minPushback = findViewById(R.id.minPushback);
        plusPushback = findViewById(R.id.plusPushback);
        minRifle = findViewById(R.id.minRifle);
        plusRifle = findViewById(R.id.plusRifle);
        buybtn = findViewById(R.id.btn_buy);

        player = ApiHelper.player;
        apiHelper = new ApiHelper();
        cntfreezegunBullets = 0;
        cntpushbackBullets =0;
        cntrifleBullets = 0;
        player = apiHelper.player;

        _playerGameStats = player.getPlayerGameStats();
        _FreezegunBullets = _playerGameStats.getFreezeGun();
        _PushbackGunBullets = _playerGameStats.getPushBackGun();
        _RifleBullets = _playerGameStats.getRifle();

        freezeView.setText(_FreezegunBullets);
        pushbackView.setText(_PushbackGunBullets);
        rifleView.setText(_RifleBullets);

        minFreeze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cntfreezegunBullets = cntfreezegunBullets -1 ;
                if(cntrifleBullets < 0)cntrifleBullets = 0;
                _FreezegunBullets = _FreezegunBullets -1;
            }
        });
        plusFreeze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cntfreezegunBullets = cntfreezegunBullets + 1;
                _FreezegunBullets = _FreezegunBullets +1;
            }
        });
        minPushback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cntpushbackBullets = cntpushbackBullets -1;
                if(cntrifleBullets < 0)cntrifleBullets = 0;
                _PushbackGunBullets = _PushbackGunBullets -1;
            }
        });
        plusPushback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cntpushbackBullets = cntpushbackBullets +1;
                _PushbackGunBullets = _PushbackGunBullets +1;
            }
        });
        minRifle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cntrifleBullets = cntrifleBullets - 1;
                if(cntrifleBullets < 0)cntrifleBullets = 0;
                _RifleBullets = _RifleBullets - 1;
            }
        });
        plusRifle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cntrifleBullets = cntrifleBullets +1 ;
                _RifleBullets = _RifleBullets +1;
            }
        });
        buybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerGameStats = new PlayerGameStats(_playerGameStats.getLifePoints(), _RifleBullets, _FreezegunBullets, _PushbackGunBullets);
                player.setPlayerGameStats(playerGameStats);

            }
        });
    }
}
