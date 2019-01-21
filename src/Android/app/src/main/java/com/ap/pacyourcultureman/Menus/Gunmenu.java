package com.ap.pacyourcultureman.Menus;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ap.pacyourcultureman.Helpers.ApiHelper;
import com.ap.pacyourcultureman.R;

public class Gunmenu {
    TextView txtRifleAmmo, txtFreezeAmmo, txtPushbackAmmo;
    LinearLayout layout_rifle, layout_freeze, layout_pushback, dropdownLayout;
    Boolean isMenuDropped = false;
    public static Boolean gunSelected = false;
    public static String selectedGun;
    int rifleammo, freezeammo, pushbackammo;
    FloatingActionButton fab;
    public Gunmenu(final Activity activity) {
        fab = activity.findViewById(R.id.fabGun);
        dropdownLayout = activity.findViewById(R.id.game_lay_dropdown);
        layout_freeze = activity.findViewById(R.id.game_lay_freeze);
        layout_pushback = activity.findViewById(R.id.game_lay_pushback);
        layout_rifle = activity.findViewById(R.id.game_lay_rifle);
        txtFreezeAmmo = activity.findViewById(R.id.game_txt_freeze);
        txtRifleAmmo = activity.findViewById(R.id.game_txt_rifle);
        txtPushbackAmmo = activity.findViewById(R.id.game_txt_pushback);
        rifleammo = ApiHelper.player.getPlayerGameStats().getRifle();
        freezeammo = ApiHelper.player.getPlayerGameStats().getFreezeGun();
        pushbackammo = ApiHelper.player.getPlayerGameStats().getPushBackGun();
        txtFreezeAmmo.setText("x: " + Integer.toString(freezeammo));
        txtRifleAmmo.setText("x: " + Integer.toString(rifleammo));
        txtPushbackAmmo.setText("x: " + Integer.toString(pushbackammo));
        gunUpdater();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isMenuDropped) {
                    dropdownLayout.setVisibility(View.VISIBLE);
                    layout_freeze.setVisibility(View.VISIBLE);
                    layout_pushback.setVisibility(View.VISIBLE);
                    layout_rifle.setVisibility(View.VISIBLE);
                    fab.setImageResource(R.drawable.gunup);
                    isMenuDropped = true;
                    gunSelected = false;
                }
                else {
                    dropdownLayout.setVisibility(View.GONE);
                    layout_freeze.setVisibility(View.GONE);
                    layout_pushback.setVisibility(View.GONE);
                    layout_rifle.setVisibility(View.GONE);
                    fab.setImageResource(R.drawable.gundown);
                    isMenuDropped = false;
                    gunSelected = false;
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
    }
    private void layoutHandler(LinearLayout layout) {
            layout_freeze.setVisibility(View.GONE);
            layout_pushback.setVisibility(View.GONE);
            layout_rifle.setVisibility(View.GONE);
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
