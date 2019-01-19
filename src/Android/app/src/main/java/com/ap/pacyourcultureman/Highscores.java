package com.ap.pacyourcultureman;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ap.pacyourcultureman.Helpers.ApiHelper;
import com.ap.pacyourcultureman.Helpers.JSONDeserializer;
import com.ap.pacyourcultureman.Helpers.VolleyCallBack;

import java.util.List;

public class Highscores extends AppCompatActivity {
    ApiHelper apiHelper;
    List<HighscoreProfile> highscoreProfile;
    ListView listview_Top10;
    CustomAdapter customAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);
        apiHelper = new ApiHelper();
        customAdapter = new CustomAdapter();
        listview_Top10 = findViewById(R.id.list_top10);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    apiHelper.getArray("https://aspcoreapipycm.azurewebsites.net/users/gettop10/high", new VolleyCallBack() {
                        @Override
                        public void onSuccess() {
                            JSONDeserializer jsonDeserializer = new JSONDeserializer();
                            highscoreProfile = jsonDeserializer.getTop10(apiHelper.getJsonArray());
                            listview_Top10.setAdapter(customAdapter);
                        }
                    });

                    Log.d("Nailed", "it");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }
    public class CustomAdapter extends BaseAdapter {
        public CustomAdapter() {
        }
        @Override
        public int getCount() {
           return highscoreProfile.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.list_highscoreprofile, viewGroup, false);
            TextView txtUsername = view.findViewById(R.id.txt_username);
            TextView txtHighScore = view.findViewById(R.id.top_txt_Highscore);
            TextView txtTotalScore = view.findViewById(R.id.top_txt_totalScore);

            txtUsername.setText(highscoreProfile.get(i).getUsername());
            txtHighScore.setText(Integer.toString(highscoreProfile.get(i).getHighestScore()));
            txtTotalScore.setText(Integer.toString(highscoreProfile.get(i).getTotalScore()));
            return view;
        }
    }
}
