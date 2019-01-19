package com.ap.pacyourcultureman;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.ap.pacyourcultureman.Helpers.BearingCalc;
import com.ap.pacyourcultureman.Helpers.ApiHelper;
import com.ap.pacyourcultureman.Helpers.VolleyCallBack;
import com.ap.pacyourcultureman.Menus.NavigationMenu;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import static com.ap.pacyourcultureman.Helpers.ApiHelper.player;
import static com.ap.pacyourcultureman.Helpers.GetBitmap.getBitmapFromDrawable;

public class Skins extends Activity {

    public static Integer skinId;
    public static Bitmap player_pacman;
    private RadioGroup radioGroup;
    private TextView textView;
    private ImageView imageView;
    private ApiHelper apiHelper;
    private Marker marker;
    private Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skins);
        radioGroup= findViewById(R.id.radiogroup_skins);
        textView = findViewById(R.id.txt_selectedskin);
        imageView = findViewById(R.id.imgv_selectedskin);
        apiHelper = new ApiHelper();
        btnOk = findViewById(R.id.skins_btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        NavigationMenu navigationMenu = new NavigationMenu(this);
        //if empty retrieve from login
        if(skinId == null){
            skinId = player.getSkinId();
        }
        selectSkin(getApplicationContext());
        radiogroupSelect();
    }


    private void radiogroupSelect(){
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                switch (id) {
                    case R.id.pacmanY:
                        skinId = 1;
                       selectSkin(getApplicationContext());
                        break;
                    case R.id.pacmanR:
                        skinId = 2;
                       selectSkin(getApplicationContext());
                        break;
                    case R.id.pacmanG:
                        skinId = 3;
                        selectSkin(getApplicationContext());
                        break;
                    case R.id.pacmanB:
                        skinId = 4;
                        selectSkin(getApplicationContext());
                        break;
                }
            }
        });
    }

    private void putSkinId(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("skinId", skinId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("putSkinId","skind pushed with skinid " + skinId + " jsonobject:  " + jsonObject);
        apiHelper.put("https://aspcoreapipycm.azurewebsites.net/Users/updateuser/" + Integer.toString(player.getId()), jsonObject, new VolleyCallBack() {
            @Override
            public void onSuccess() {
            }
        });
    }

    private void  selectSkin(Context context){
        int skin = skinId;
        String setText;
        switch (skin) {
            case 1:
                putSkinId();
                setText = "Selected skin: Yellow";
                textView.setText(setText);
                imageView.setImageResource(R.drawable.pacman_yellow_right);
                player_pacman = getBitmapFromDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.pacman_yellow_top, null));
                break;
            case 2:
                putSkinId();
                setText = "Selected skin: Red";
                textView.setText(setText);
                imageView.setImageResource(R.drawable.pacman_red_right);
                player_pacman = getBitmapFromDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.pacman_red_top, null));
                break;
            case 3:
                putSkinId();
                setText= "Selected skin: Green";
                textView.setText(setText);
                imageView.setImageResource(R.drawable.pacman_green_right);
                player_pacman = getBitmapFromDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.pacman_green_top, null));
                break;
            case 4:
                putSkinId();
                setText = "Selected skin: Blue";
                textView.setText(setText);
                imageView.setImageResource(R.drawable.pacman_blue_right);
                player_pacman = getBitmapFromDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.pacman_blue_top, null));
                break;

        }
    }


    public void setDraggable(Marker marker) {
        marker.setDraggable(false);
        this.marker = marker;
    }

    public Marker getMarker() {
        return marker;
    }

    public void removeMarker() {
        marker.remove();
    }

    public void DrawPlayer(GoogleMap mMap, Context context, int width, int height ){
        Bitmap scaledPacman = Bitmap.createScaledBitmap( player_pacman, width, height, false);
        marker = mMap.addMarker(new MarkerOptions()
                .position(GameActivity.currentPos)
                .icon(BitmapDescriptorFactory.fromBitmap(scaledPacman))
                .draggable(true)
                .flat(true)
                .anchor(0.5f,0.5f)
                .rotation(0f));

    }

    public static void SkinInit(Context context){
        //if empty retrieve at login
        if(skinId == null){
            skinId = player.getSkinId();
        }
        //set skin at start
        switch (skinId) {
            case 1:
                Skins.player_pacman = getBitmapFromDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.pacman_yellow_top, null));
                break;
            case 2:
                Skins.player_pacman = getBitmapFromDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.pacman_red_top, null));
                break;
            case 3:
                Skins.player_pacman = getBitmapFromDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.pacman_green_top, null));
                break;
            case 4:
                Skins.player_pacman = getBitmapFromDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.pacman_blue_top, null));
                break;

        }

    }
    public void setRotations(Marker marker) {
        float rot = 0;
        float rota = GameActivity.rotation - 45;
        if (rota >= 0 && rota<= 90){
            rot = 90;
        }
        if (rota>= 90 && rota<= 180){
            rot = 180;
        }
        if (rota >= 180 && rota<= 270){
            rot = 270;
        }
        if (rota >= 270 && rota<= 360){
            rot = 360;
        }
        marker.setRotation(rot);
        this.marker = marker;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this.getBaseContext(), GameActivity.class);
        finish();
        this.startActivity(intent);
    }

}

