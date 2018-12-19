package com.ap.pacyourcultureman;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
                        putSkinId();
                        Toast.makeText(getBaseContext(), "Yellow pacman selected, skinid:" + skinId,
                                Toast.LENGTH_LONG).show();

                        break;
                    case R.id.pacmanR:
                        skinId = 2;
                        putSkinId();
                        Toast.makeText(getBaseContext(), "Red pacman selected, skinid:" + skinId,
                                Toast.LENGTH_LONG).show();
                        break;
                    case R.id.pacmanG:
                        putSkinId();
                        skinId = 3;
                        Toast.makeText(getBaseContext(), "Green pacman selected, skinid:" + skinId,
                                Toast.LENGTH_LONG).show();
                        break;
                    case R.id.pacmanB:
                        skinId = 4;
                        putSkinId();
                        Toast.makeText(getBaseContext(), "Blue pacman selected, skinid:" + skinId,
                                Toast.LENGTH_LONG).show();
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
        apiHelper.put("https://aspcoreapipycm.azurewebsites.net/Users/updateuser/" + Integer.toString(player.getId()), jsonObject, new VolleyCallBack() {
            @Override
            public void onSuccess() {
                finish();
            }
        });
    }

}
