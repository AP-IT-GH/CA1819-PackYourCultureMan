package com.ap.pacyourcultureman;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ap.pacyourcultureman.Helpers.ApiHelper;
import com.ap.pacyourcultureman.Helpers.VolleyCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import static com.ap.pacyourcultureman.Helpers.ApiHelper.player;

public class Skins extends AppCompatActivity {
    private RadioGroup radioGroup;
    public static int skinId;
    private TextView textView;
    private ImageView imageView;
    private ApiHelper apiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skins);
        radioGroup= findViewById(R.id.radiogroup_skins);
        textView = findViewById(R.id.txt_selectedskin);
        imageView = findViewById(R.id.imgv_selectedskin);
        apiHelper = new ApiHelper();
        skinId = player.getSkinId();
        selectSkin();
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
                       selectSkin();
                        break;
                    case R.id.pacmanR:
                        skinId = 2;
                       selectSkin();
                        break;
                    case R.id.pacmanG:
                        skinId = 3;
                        selectSkin();
                        break;
                    case R.id.pacmanB:
                        skinId = 4;
                        selectSkin();
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

    private void  selectSkin(){
        int skin = skinId;
        String setText;
        switch (skin) {
            case 1:
                putSkinId();
                setText = "Selected skin: Yellow";
                textView.setText(setText);
                imageView.setImageResource(R.drawable.pacman_yellow_left);
                break;
            case 2:
                putSkinId();
                setText = "Selected skin: Red";
                textView.setText(setText);
                imageView.setImageResource(R.drawable.pacman_red_left);
                break;
            case 3:
                putSkinId();
                setText= "Selected skin: Green";
                textView.setText(setText);
                imageView.setImageResource(R.drawable.pacman_green_left);
                break;
            case 4:
                putSkinId();
                setText = "Selected skin: Blue";
                textView.setText(setText);
                imageView.setImageResource(R.drawable.pacman_blue_left);
                break;

        }
    }

}

