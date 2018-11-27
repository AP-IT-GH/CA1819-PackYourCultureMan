package com.ap.pacyourcultureman;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.ap.pacyourcultureman.Helpers.ApiHelper;

public class StatsPage extends AppCompatActivity {
    TextView txt_highscore;
    TextView txt_totalscore;
    TextView txt_totalsucces;
    TextView txt_totallost;
    TextView txt_totalfailed;
    int userId;
    String jwt;
    Intent iin;
    Bundle b;
    Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats_page);

        txt_highscore = findViewById(R.id.txt_highscore);
        txt_totalfailed = findViewById(R.id.txt_totalfailed);
        txt_totallost = findViewById(R.id.txt_totallost);
        txt_totalsucces = findViewById(R.id.txt_totalsucces);
        txt_totalscore = findViewById(R.id.txt_totalscore);

        player = ApiHelper.player;

        txt_highscore.setText(Integer.toString(player.playerStats.highestScore));
        txt_totalfailed.setText(Integer.toString(player.playerStats.totalFailed));
        txt_totallost.setText(Integer.toString(player.playerStats.totalLost));
        txt_totalscore.setText(Integer.toString(player.playerStats.totalScore));
        txt_totalsucces.setText(Integer.toString(player.playerStats.totalSuccess));



    }

}
