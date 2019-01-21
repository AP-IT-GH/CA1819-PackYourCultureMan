package com.ap.pacyourcultureman;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ap.pacyourcultureman.Helpers.ApiHelper;
import com.ap.pacyourcultureman.Helpers.JSONDeserializer;
import com.ap.pacyourcultureman.Helpers.VolleyCallBack;
import com.ap.pacyourcultureman.Menus.NavigationMenu;

import java.util.List;

public class Highscores extends Activity {
    ApiHelper apiHelper;
    List<HighscoreProfile> highscoreProfile;
    HighscoreProfile userProfile;
    ListView listview_Top10;
    CustomAdapter customAdapter;
    Button btnHigh,btnTotal, btnBack;
    TextView txt_playername,txt_rank;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);
        apiHelper = new ApiHelper();
        btnTotal = findViewById(R.id.btn_bytotal);
        btnHigh = findViewById(R.id.btn_byhigh);
        txt_rank = findViewById(R.id.HS_txt_rank);
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        customAdapter = new CustomAdapter();
        listview_Top10 = findViewById(R.id.list_top10);
        NavigationMenu navigationMenu = new NavigationMenu(this);
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

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    apiHelper.get("https://aspcoreapipycm.azurewebsites.net/users/gethighscoreuser/"+ Integer.toString(ApiHelper.player.getId())+"/high", new VolleyCallBack() {
                        @Override
                        public void onSuccess() {
                            JSONDeserializer jsonDeserializer = new JSONDeserializer();
                            userProfile = jsonDeserializer.getPlayerHsProfile(apiHelper.getJsonObject());
                            txt_rank.setText("Your current rank: " + Integer.toString(userProfile.getRanking()));
                        }
                    });

                    Log.d("Nailed", userProfile.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread2.start();

        btnHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnHigh.setTextColor(Color.parseColor("#ffffff"));
                btnTotal.setTextColor(Color.parseColor("#000000"));

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
                Thread thread2 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            apiHelper.get("https://aspcoreapipycm.azurewebsites.net/users/gethighscoreuser/"+ Integer.toString(ApiHelper.player.getId())+"/high", new VolleyCallBack() {
                                @Override
                                public void onSuccess() {
                                    JSONDeserializer jsonDeserializer = new JSONDeserializer();
                                    userProfile = jsonDeserializer.getPlayerHsProfile(apiHelper.getJsonObject());
                                }
                            });

                            Log.d("Nailed", userProfile.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread2.start();
            }
        });
        btnTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnTotal.setTextColor(Color.parseColor("#ffffff"));
                btnHigh.setTextColor(Color.parseColor("#000000"));
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            apiHelper.getArray("https://aspcoreapipycm.azurewebsites.net/users/gettop10/total", new VolleyCallBack() {
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
                Thread thread2 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            apiHelper.get("https://aspcoreapipycm.azurewebsites.net/users/gethighscoreuser/"+ Integer.toString(ApiHelper.player.getId())+"/total", new VolleyCallBack() {
                                @Override
                                public void onSuccess() {
                                    JSONDeserializer jsonDeserializer = new JSONDeserializer();
                                    userProfile = jsonDeserializer.getPlayerHsProfile(apiHelper.getJsonObject());
                                }
                            });

                            Log.d("Nailed", userProfile.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread2.start();
            }
        });


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
