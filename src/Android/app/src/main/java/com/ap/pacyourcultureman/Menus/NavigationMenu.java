package com.ap.pacyourcultureman.Menus;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.ap.pacyourcultureman.DevOptions;
import com.ap.pacyourcultureman.Helpers.ApiHelper;
import com.ap.pacyourcultureman.Login;
import com.ap.pacyourcultureman.R;
import com.ap.pacyourcultureman.Settings;
import com.ap.pacyourcultureman.Shop;
import com.ap.pacyourcultureman.Sights;
import com.ap.pacyourcultureman.Skins;
import com.ap.pacyourcultureman.StatsPage;


public class NavigationMenu  {
    NavigationView navigationView;
    static Menu nav_Menu;
    public NavigationMenu(final Activity activity) {
        navigationView = activity.findViewById(R.id.menu);
        nav_Menu = navigationView.getMenu();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.nav_logout:
                        intent = new Intent(activity.getBaseContext(), Login.class);
                        activity.startActivity(intent);
                        resetStaticLists();
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
                    case R.id.nav_dev:
                        intent = new Intent(activity.getBaseContext(),DevOptions.class);
                        activity.startActivity(intent);
                        break;
                    case R.id.nav_skins:
                        intent = new Intent(activity.getBaseContext(),Skins.class);
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

    public static Menu getNav_Menu() {
        return nav_Menu;
    }

    public static void resetStaticLists(){
        ApiHelper.visitedSights.clear();
        ApiHelper.assignments.clear();
        ApiHelper.correctedDots.clear();
        ApiHelper.generatedDots.clear();
        ApiHelper.streets.clear();
        ApiHelper.dots.clear();
        Log.d("clearLists", "Static list are cleared");

    }
}
