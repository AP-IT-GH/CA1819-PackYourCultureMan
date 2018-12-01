package com.ap.pacyourcultureman.Menus;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ap.pacyourcultureman.Helpers.ApiHelper;
import com.ap.pacyourcultureman.Helpers.CollisionHandler;
import com.ap.pacyourcultureman.R;
import com.google.android.gms.common.api.Api;

import org.w3c.dom.Text;

public class Gunmenu {
    ImageView dropdownMenu;
    TextView txtRifleAmmo, txtFreezeAmmo, txtPushbackAmmo;
    Button switchGun;
    LinearLayout layout_rifle, layout_freeze, layout_pushback, dropdownLayout;
    Boolean isMenuDropped = false;
    Boolean gunSelected = false;
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
        rifleammo = ApiHelper.player.getPlayerGameStats().getRifle();
        freezeammo = ApiHelper.player.getPlayerGameStats().getFreezeGun();
        pushbackammo = ApiHelper.player.getPlayerGameStats().getPushBackGun();
        txtFreezeAmmo.setText(Integer.toString(freezeammo));
        txtRifleAmmo.setText(Integer.toString(rifleammo));
        txtPushbackAmmo.setText(Integer.toString(pushbackammo));
        final CollisionHandler collisionHandler = new CollisionHandler(activity.getApplicationContext());
        dropdownMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isMenuDropped) {
                    dropdownLayout.setVisibility(View.VISIBLE);
                    isMenuDropped = true;
                }
                else {
                    dropdownLayout.setVisibility(View.GONE);
                    isMenuDropped = false;
                }
            }
        });
        layout_rifle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!gunSelected) {
                    layoutHandler(layout_rifle);
                    gunSelected = true;
                }
                else {
                    if(rifleammo != 0) {
                        Toast.makeText(activity.getApplicationContext(), "Select Target!", Toast.LENGTH_SHORT).show();
                        collisionHandler.gunCollision();
                    }
                    else {
                        Toast.makeText(activity.getApplicationContext(), "No ammo!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        layout_freeze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutHandler(layout_freeze);
            }
        });
        layout_pushback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutHandler(layout_pushback);
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

}
