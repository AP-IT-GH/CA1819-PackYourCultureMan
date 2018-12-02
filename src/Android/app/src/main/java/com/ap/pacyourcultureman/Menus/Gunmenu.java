package com.ap.pacyourcultureman.Menus;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ap.pacyourcultureman.Helpers.ApiHelper;
import com.ap.pacyourcultureman.R;

public class Gunmenu {
    ImageView dropdownMenu;
    TextView txtRifleAmmo, txtFreezeAmmo, txtPushbackAmmo;
    Button switchGun;
    LinearLayout layout_rifle, layout_freeze, layout_pushback, dropdownLayout;
    Boolean isMenuDropped = false;
    public static Boolean gunSelected = false;
    public static String selectedGun;
    int rifleammo, freezeammo, pushbackammo;
    public Gunmenu(final Activity activity) {
        dropdownMenu = activity.findViewById(R.id.game_img_dropdown);
        dropdownLayout = activity.findViewById(R.id.game_lay_dropdown);
        layout_freeze = activity.findViewById(R.id.game_lay_freeze);
        layout_pushback = activity.findViewById(R.id.game_lay_pushback);
        layout_rifle = activity.findViewById(R.id.game_lay_rifle);
        switchGun = activity.findViewById(R.id.game_btn_guncancel);
        txtFreezeAmmo = activity.findViewById(R.id.game_txt_freeze);
        txtRifleAmmo = activity.findViewById(R.id.game_txt_rifle);
        txtPushbackAmmo = activity.findViewById(R.id.game_txt_pushback);
        ApiHelper.player.getPlayerGameStats().setRifle(5);
        rifleammo = ApiHelper.player.getPlayerGameStats().getRifle();
        freezeammo = ApiHelper.player.getPlayerGameStats().getFreezeGun();
        pushbackammo = ApiHelper.player.getPlayerGameStats().getPushBackGun();
        txtFreezeAmmo.setText("x: " + Integer.toString(freezeammo));
        txtRifleAmmo.setText("x: " + Integer.toString(rifleammo));
        txtPushbackAmmo.setText("x: " + Integer.toString(pushbackammo));
        dropdownMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isMenuDropped) {
                    dropdownLayout.setVisibility(View.VISIBLE);
                    dropdownMenu.setRotation(180);
                    isMenuDropped = true;
                }
                else {
                    dropdownLayout.setVisibility(View.GONE);
                    dropdownMenu.setRotation(0);
                    isMenuDropped = false;
                }
            }
        });
        layout_rifle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!gunSelected) {
                    layoutHandler(layout_rifle);
                    Toast.makeText(activity.getApplicationContext(), "Select Target!", Toast.LENGTH_SHORT).show();
                    selectedGun = "Rifle";
                    gunSelected = true;
                }
            }
        });
        layout_freeze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutHandler(layout_freeze);
                Toast.makeText(activity.getApplicationContext(), "Select Target!", Toast.LENGTH_SHORT).show();
                selectedGun = "Freeze";
                gunSelected = true;
            }
        });
        layout_pushback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutHandler(layout_pushback);
                Toast.makeText(activity.getApplicationContext(), "Select Target!", Toast.LENGTH_SHORT).show();
                selectedGun = "Pushback";
                gunSelected = true;
            }
        });
        switchGun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_freeze.setVisibility(View.VISIBLE);
                layout_pushback.setVisibility(View.VISIBLE);
                layout_rifle.setVisibility(View.VISIBLE);
                switchGun.setVisibility(View.GONE);
                gunSelected = false;
            }
        });
    }
    private void layoutHandler(LinearLayout layout) {
            layout_freeze.setVisibility(View.GONE);
            layout_pushback.setVisibility(View.GONE);
            layout_rifle.setVisibility(View.GONE);
            switchGun.setVisibility(View.VISIBLE);
            layout.setVisibility(View.VISIBLE);
    }
    public void gunUpdater() {
        rifleammo = ApiHelper.player.getPlayerGameStats().getRifle();
        freezeammo = ApiHelper.player.getPlayerGameStats().getFreezeGun();
        pushbackammo = ApiHelper.player.getPlayerGameStats().getPushBackGun();
        txtFreezeAmmo.setText("x: " + Integer.toString(freezeammo));
        txtRifleAmmo.setText("x: " + Integer.toString(rifleammo));
        txtPushbackAmmo.setText("x: " + Integer.toString(pushbackammo));
    }

}
