package com.ap.pacyourcultureman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ap.pacyourcultureman.Helpers.ApiHelper;
import com.ap.pacyourcultureman.Helpers.VolleyCallBack;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import static com.ap.pacyourcultureman.Helpers.ApiHelper.player;
import static com.ap.pacyourcultureman.Helpers.GetBitmap.getBitmapFromDrawable;

public class Skins extends AppCompatActivity {

    public static Integer skinId;
    public  static boolean redraw_skin;
    private RadioGroup radioGroup;
    private TextView textView;
    private ImageView imageView;
    private ApiHelper apiHelper;
    private Marker marker;
    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public static Bitmap player_pacman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skins);
        radioGroup= findViewById(R.id.radiogroup_skins);
        textView = findViewById(R.id.txt_selectedskin);
        imageView = findViewById(R.id.imgv_selectedskin);
        apiHelper = new ApiHelper();
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

        redraw_skin = true;
    }

    private void  selectSkin(Context context){
        int skin = skinId;
        String setText;
        switch (skin) {
            case 1:
                putSkinId();
                setText = "Selected skin: Yellow";
                textView.setText(setText);
                imageView.setImageResource(R.drawable.pacman_yellow_left);
                player_pacman = getBitmapFromDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.pacman_yellow_left, null));
                break;
            case 2:
                putSkinId();
                setText = "Selected skin: Red";
                textView.setText(setText);
                imageView.setImageResource(R.drawable.pacman_red_left);
                player_pacman = getBitmapFromDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.pacman_red_left, null));
                break;
            case 3:
                putSkinId();
                setText= "Selected skin: Green";
                textView.setText(setText);
                imageView.setImageResource(R.drawable.pacman_green_left);
                player_pacman = getBitmapFromDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.pacman_green_left, null));
                break;
            case 4:
                putSkinId();
                setText = "Selected skin: Blue";
                textView.setText(setText);
                imageView.setImageResource(R.drawable.pacman_blue_left);
                player_pacman = getBitmapFromDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.pacman_blue_left, null));
                break;

        }
    }


    public void DrawPlayer(GoogleMap mMap, Context context, int width, int height ){
        Bitmap scaledPacman = Bitmap.createScaledBitmap( player_pacman, width, height, false);
        marker = mMap.addMarker(new MarkerOptions()
                .position(GameActivity.currentPos)
                .icon(BitmapDescriptorFactory.fromBitmap(scaledPacman))
                .draggable(true));

    }

    public static void SkinInit(Context context){
        redraw_skin = false;
        //if empty retrieve at login
        if(skinId == null){
            skinId = player.getSkinId();
        }
        //set skin at start
        switch (skinId) {
            case 1:
                Skins.player_pacman = getBitmapFromDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.pacman_yellow_left, null));
                break;
            case 2:
                Skins.player_pacman = getBitmapFromDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.pacman_red_left, null));
                break;
            case 3:
                Skins.player_pacman = getBitmapFromDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.pacman_green_left, null));
                break;
            case 4:
                Skins.player_pacman = getBitmapFromDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.pacman_blue_left, null));
                break;

        }

    }

}

