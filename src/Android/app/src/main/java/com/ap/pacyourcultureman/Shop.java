package com.ap.pacyourcultureman;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ap.pacyourcultureman.Helpers.ApiHelper;
import com.ap.pacyourcultureman.Helpers.JSONSerializer;
import com.ap.pacyourcultureman.Helpers.VolleyCallBack;
import com.ap.pacyourcultureman.Menus.NavigationMenu;

import org.json.JSONException;
import org.json.JSONObject;

public class Shop extends Activity {
    TextView freezeguncnt,pushBackcnt,riflecnt,freezeView,pushbackView,rifleView,priceView,coinView,lifepointscnt,lifepointsView;
    Player player;
    ApiHelper apiHelper;
    Button minFreeze,plusFreeze,minPushback,plusPushback,minRifle,plusRifle,buybtn,minLifepoints,plusLifePoints, btnBack;
    int cntfreezegunBullets,cntpushbackBullets,cntrifleBullets,cntLifepoints,_FreezegunBullets,_PushbackGunBullets,_RifleBullets,_lifePoints,_coins,_totalprice;
    int totPriceRifle,totPriceFreeze,totPricePushback,totPriceLifepoints;
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
        priceView = findViewById(R.id.price);
        coinView = findViewById(R.id._coins);
        minFreeze = findViewById(R.id.minFreeze);
        plusFreeze = findViewById(R.id.plusFreeze);
        minPushback = findViewById(R.id.minPushback);
        plusPushback = findViewById(R.id.plusPushback);
        minRifle = findViewById(R.id.minRifle);
        plusRifle = findViewById(R.id.plusRifle);
        buybtn = findViewById(R.id.btn_buy);
        minLifepoints = findViewById(R.id.minLifepoints);
        plusLifePoints = findViewById(R.id.plusLifepoints);
        lifepointscnt = findViewById(R.id.lifepointsView);
        lifepointsView = findViewById(R.id.currentLifepoints);
        btnBack = findViewById(R.id.btn_back);
        NavigationMenu navigationMenu = new NavigationMenu(this);
        player = ApiHelper.player;
        apiHelper = new ApiHelper();
        cntfreezegunBullets = 0;
        cntpushbackBullets =0;
        cntrifleBullets = 0;
        cntLifepoints = 0;
        player = apiHelper.player;

        _playerGameStats = player.getPlayerGameStats();
        _FreezegunBullets = _playerGameStats.getFreezeGun();
        _PushbackGunBullets = _playerGameStats.getPushBackGun();
        _RifleBullets = _playerGameStats.getRifle();
        _coins = _playerGameStats.getCoins();
        _lifePoints = _playerGameStats.getLifePoints();


        freezeView.setText(Integer.toString(_FreezegunBullets));
        pushbackView.setText(Integer.toString(_PushbackGunBullets));
        rifleView.setText(Integer.toString(_RifleBullets));
        lifepointsView.setText(Integer.toString(_lifePoints));


        //test coins
        _coins = ApiHelper.player.getPlayerGameStats().getCoins();

        coinView.setText(Integer.toString(_coins));
        minFreeze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cntfreezegunBullets = cntfreezegunBullets -1 ;
                if(cntfreezegunBullets < 0){
                    cntfreezegunBullets = 0;
                }
                totPriceFreeze = 20 * cntfreezegunBullets;

                freezeguncnt.setText(Integer.toString(cntfreezegunBullets));
                _totalprice = calcTotalPrice();
                priceView.setText(Integer.toString(_totalprice));
            }
        });
        plusFreeze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cntfreezegunBullets = cntfreezegunBullets + 1;
                totPriceFreeze = 20 * cntfreezegunBullets;
                freezeguncnt.setText(Integer.toString(cntfreezegunBullets));
                _totalprice = calcTotalPrice();
                priceView.setText(Integer.toString(_totalprice));
            }
        });
        minPushback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cntpushbackBullets = cntpushbackBullets -1;
                if(cntpushbackBullets < 0){
                    cntpushbackBullets = 0;

                }
                totPricePushback = 10 * cntpushbackBullets;

                pushBackcnt.setText(Integer.toString(cntpushbackBullets));
                _totalprice = calcTotalPrice();
                priceView.setText(Integer.toString(_totalprice));
            }
        });
        plusPushback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cntpushbackBullets = cntpushbackBullets +1;
                totPricePushback = 10 * cntpushbackBullets;
                pushBackcnt.setText(Integer.toString(cntpushbackBullets));
                _totalprice = calcTotalPrice();
                priceView.setText(Integer.toString(_totalprice));
            }
        });
        minRifle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cntrifleBullets = cntrifleBullets - 1;
                if(cntrifleBullets < 0){
                    cntrifleBullets = 0;

                }
                totPriceRifle = 30 * cntrifleBullets;


                riflecnt.setText(Integer.toString(cntrifleBullets));
                _totalprice = calcTotalPrice();
                priceView.setText(Integer.toString(_totalprice));
            }
        });
        plusRifle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cntrifleBullets = cntrifleBullets +1 ;
                totPriceRifle = 30 * cntrifleBullets;

                riflecnt.setText(Integer.toString(cntrifleBullets));
                _totalprice = calcTotalPrice();
                priceView.setText(Integer.toString(_totalprice));
            }
        });
        minLifepoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cntLifepoints = cntLifepoints - 1;
                if(cntLifepoints < 0){
                    cntLifepoints = 0;

                }
                totPriceLifepoints = 50 * cntLifepoints;


                lifepointscnt.setText(Integer.toString(cntLifepoints));
                _totalprice = calcTotalPrice();
                priceView.setText(Integer.toString(_totalprice));
            }
        });
        plusLifePoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cntLifepoints = cntLifepoints +1 ;
                totPriceLifepoints = 50 * cntLifepoints;

                lifepointscnt.setText(Integer.toString(cntLifepoints));
                _totalprice = calcTotalPrice();
                priceView.setText(Integer.toString(_totalprice));
            }
        });
        buybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openResetDialog();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void openResetDialog(){

        final Dialog dialog = new Dialog(Shop.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_shop);
        dialog.setCancelable(true);
        final Button btn_dialog_buy = dialog.findViewById(R.id.btn_dialog_buy);
        final Button btn_dialog_cancel = dialog.findViewById(R.id.btn_dialogshop_cancel);
        btn_dialog_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _totalprice = calcTotalPrice();
                if(_totalprice <= _coins){
                    _PushbackGunBullets = _PushbackGunBullets + cntpushbackBullets;
                    _RifleBullets = _RifleBullets + cntrifleBullets;
                    _FreezegunBullets = _FreezegunBullets + cntfreezegunBullets;
                    _lifePoints = _lifePoints +cntLifepoints;

                    cntrifleBullets = 0;
                    cntpushbackBullets = 0;
                    cntfreezegunBullets = 0;
                    cntLifepoints = 0;

                    riflecnt.setText(Integer.toString(cntrifleBullets));
                    pushBackcnt.setText(Integer.toString(cntpushbackBullets));
                    freezeguncnt.setText(Integer.toString(cntfreezegunBullets));
                    lifepointscnt.setText(Integer.toString(cntLifepoints));

                    playerGameStats = new PlayerGameStats(_lifePoints, _RifleBullets, _FreezegunBullets, _PushbackGunBullets,_coins);
                    player.setPlayerGameStats(playerGameStats);
                    _coins = _coins - _totalprice;
                    freezeView.setText(Integer.toString(player.getPlayerGameStats().getFreezeGun()));
                    pushbackView.setText(Integer.toString(player.getPlayerGameStats().getPushBackGun()));
                    rifleView.setText(Integer.toString(player.getPlayerGameStats().getRifle()));
                    lifepointsView.setText(Integer.toString(player.getPlayerGameStats().getLifePoints()));


                    

                    JSONSerializer jsonSerializer = new JSONSerializer();
                    JSONObject jsonObject = jsonSerializer.jsonPutGameStats(_lifePoints,_RifleBullets,_FreezegunBullets,_PushbackGunBullets,_coins);
                    apiHelper.put("https://aspcoreapipycm.azurewebsites.net/Users/updategamestats/" + Integer.toString(ApiHelper.player.getId()), jsonObject, new VolleyCallBack() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(Shop.this, "Thank you!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    coinView.setText(Integer.toString(_coins));
                    _totalprice = 0;
                    totPricePushback = 0;
                    totPriceFreeze = 0;
                    totPriceRifle = 0;
                    totPriceLifepoints = 0;

                    priceView.setText(Integer.toString(_totalprice));
                }else{
                    Toast.makeText(Shop.this, "You don't have enough credits", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        btn_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    private int calcTotalPrice(){
        int totalprice = totPriceFreeze + totPricePushback + totPriceRifle + totPriceLifepoints;
        return totalprice;
    }
}
