package com.ap.pacyourcultureman;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ap.pacyourcultureman.Helpers.ApiHelper;
import com.ap.pacyourcultureman.Helpers.VolleyCallBack;
import com.ap.pacyourcultureman.Menus.NavigationMenu;

import org.json.JSONException;
import org.json.JSONObject;

public class StatsPage extends Activity {
    TextView txt_highscore,txt_totalfailed,txt_totallost,txt_totalsucces,txt_totalscore;
    int userId;
    String jwt;
    Intent iin;
    Bundle b;
    Player player;
    Button btn_resetStats, btnBack;
    PlayerStats zeroPlayerStats;
    ApiHelper apiHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats_page);
        NavigationMenu navigationMenu = new NavigationMenu(this);
        txt_highscore = findViewById(R.id.txt_highscore);
        txt_totalfailed = findViewById(R.id.txt_totalfailed);
        txt_totallost = findViewById(R.id.txt_totallost);
        txt_totalsucces = findViewById(R.id.txt_totalsucces);
        txt_totalscore = findViewById(R.id.txt_totalscore);
        btn_resetStats = findViewById(R.id.btn_resetStats);
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        player = ApiHelper.player;
        apiHelper = new ApiHelper();
        zeroPlayerStats = new PlayerStats(0,0,0,0,0);
        txt_highscore.setText(Integer.toString(player.getPlayerStats().getHighestScore()));
        txt_totalfailed.setText(Integer.toString(player.getPlayerStats().getTotalFailed()));
        txt_totallost.setText(Integer.toString(player.getPlayerStats().getTotalLost()));
        txt_totalscore.setText(Integer.toString(player.getPlayerStats().getTotalScore()));
        txt_totalsucces.setText(Integer.toString(player.getPlayerStats().getTotalSuccess()));
        btn_resetStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openResetDialog();
                Log.d("toch","reset");
            }
        });


    }
    private void openResetDialog(){

        final Dialog dialog = new Dialog(StatsPage.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_reset);
        dialog.setCancelable(true);
        final Button btn_dialog_reset = dialog.findViewById(R.id.btn_dialog_reset);
        final Button btn_dialog_cancel = dialog.findViewById(R.id.btn_dialog_cancel);
        btn_dialog_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.setPlayerStats(zeroPlayerStats);
                JSONObject jsonObject = new JSONObject();
                JSONObject jsonParam = new JSONObject();
                try {
                    jsonParam.put("totalFailed", player.getPlayerStats().getTotalFailed());
                    jsonParam.put("highestScore", player.getPlayerStats().getHighestScore());
                    jsonParam.put("totalScore", player.getPlayerStats().getTotalScore());
                    jsonParam.put("totalSucces", player.getPlayerStats().getTotalScore());
                    jsonParam.put("totalLost", player.getPlayerStats().getTotalLost());
                    jsonObject.put("stats", jsonParam);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                apiHelper.put("https://aspcoreapipycm.azurewebsites.net/Users/updatestats/" + Integer.toString(player.getId()), jsonObject, new VolleyCallBack() {
                    @Override
                    public void onSuccess() {
                        //Toast.makeText(StatsPage.this, "Stats Reset", Toast.LENGTH_SHORT).show();
                        Log.d("stats", Integer.toString(player.getPlayerStats().getHighestScore()));
                        txt_highscore.setText(Integer.toString(player.getPlayerStats().getHighestScore()));
                        txt_totalfailed.setText(Integer.toString(player.getPlayerStats().getTotalFailed()));
                        txt_totallost.setText(Integer.toString(player.getPlayerStats().getTotalLost()));
                        txt_totalscore.setText(Integer.toString(player.getPlayerStats().getTotalScore()));
                        txt_totalsucces.setText(Integer.toString(player.getPlayerStats().getTotalSuccess()));
                        dialog.dismiss();
                    }
                });
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
}
