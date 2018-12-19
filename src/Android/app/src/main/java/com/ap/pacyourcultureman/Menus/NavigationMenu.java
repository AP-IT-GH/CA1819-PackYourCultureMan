package com.ap.pacyourcultureman.Menus;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.ap.pacyourcultureman.Helpers.ApiHelper;
import com.ap.pacyourcultureman.Login;
import com.ap.pacyourcultureman.R;
import com.ap.pacyourcultureman.Settings;
import com.ap.pacyourcultureman.Shop;
import com.ap.pacyourcultureman.Sights;
import com.ap.pacyourcultureman.StatsPage;

public class NavigationMenu {
    NavigationView navigationView;
    public NavigationMenu(final Activity activity) {
        navigationView = activity.findViewById(R.id.menu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.nav_logout:
                        intent = new Intent(activity.getBaseContext(), Login.class);
                        activity.startActivity(intent);
                        break;
                    case R.id.nav_stats:
                        intent = new Intent(activity.getBaseContext(),StatsPage.class);
                        Log.d("Activity", "Act");
                        intent.putExtra("userid",ApiHelper.player.getId());
                        Log.e("jwt", ApiHelper.player.getJwt());
                        activity.startActivity(intent);
                        break;
                    case R.id.nav_settings:
                        intent = new Intent(activity.getBaseContext(),Settings.class);
                        Log.e("jwt", ApiHelper.player.getJwt());
                        activity.startActivity(intent);
                        break;
                    case R.id.nav_sights:
                        intent = new Intent(activity.getBaseContext(),Sights.class);
                        activity.startActivity(intent);
                        break;
                    case R.id.nav_shop:
                        intent = new Intent(activity.getBaseContext(),Shop.class);

                        activity.startActivity(intent);
                        break;
                }
                return false;
            }
        });
    }
}
