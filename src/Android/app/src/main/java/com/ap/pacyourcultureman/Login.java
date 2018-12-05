package com.ap.pacyourcultureman;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.ap.pacyourcultureman.Helpers.ApiHelper;
import com.ap.pacyourcultureman.Helpers.JSONDeserializer;
import com.ap.pacyourcultureman.Helpers.JSONSerializer;
import com.ap.pacyourcultureman.Helpers.VolleyCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.ap.pacyourcultureman.Helpers.getDotsBetween2Points.GetDotsBetweenAanB;

public class Login extends Activity {
    private String email, password;
    Button btn_login, btn_register, btn_dev;
    EditText edit_email, edit_password;
    TextView errorChecker;
    CheckBox chb_rememberme, chb_loginauto;
    Boolean rememberMe, loginauto;
    HttpURLConnection conn;
    URL url;
    private Handler mHandler;
    RequestQueue queue;  // this = context
    static List<Assignment> assignments;
    List<Step> steps = new ArrayList<>();
    ApiHelper apiHelper, apiHelper2, apiHelper3;
    Boolean run1 = false;
    Boolean run2 = false;
    Boolean run3 = false;
    Boolean run4 = false;
    int userId;
    int counter  = 0;
    int urlCounter = 0;
    String jwt;
    int size = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_form);
        apiHelper = new ApiHelper();
        apiHelper2 = new ApiHelper();
        apiHelper3 = new ApiHelper();
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        btn_dev = findViewById(R.id.btn_dev);
        edit_email = findViewById(R.id.edit_mail);
        edit_password = findViewById(R.id.edit_pass);
        errorChecker = findViewById(R.id.txt_errorchecker);
        chb_rememberme = findViewById(R.id.login_chb_rememember);
        chb_loginauto = findViewById(R.id.login_chb_autologin);
        Intent intent = getIntent();
        queue = Volley.newRequestQueue(this);
        Load();
        String intentuser = intent.getStringExtra("username");
        String intentpassword = intent.getStringExtra("pass");
        if (intentuser != null && intentpassword != null) {
            edit_password.setText(intentpassword);
            edit_email.setText(intentuser);
            chb_loginauto.setChecked(false);
            chb_rememberme.setChecked(false);
        }
        if (chb_loginauto.isChecked()) {
            email = edit_email.getText().toString();
            password = edit_password.getText().toString();
        }
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorSetter("Logging in");
                String user = edit_email.getText().toString();
                String pass = edit_password.getText().toString();
                JSONSerializer jsonSerializer = new JSONSerializer();
                JSONObject jsonObject = jsonSerializer.jsonPostLogin(user, pass);
                apiHelper.sendPost("https://aspcoreapipycm.azurewebsites.net/Users/authenticate", jsonObject);
                if (apiHelper.getResponse() == "Success") {
                    errorSetter("Fetching data");
                    apiHelper.setPlayer(apiHelper.getReply());
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                apiHelper.getArray("https://aspcoreapipycm.azurewebsites.net/Sights", new VolleyCallBack() {
                                    @Override
                                    public void onSuccess() {
                                        JSONDeserializer jsonDeserializer = new JSONDeserializer();
                                        ApiHelper.assignments = jsonDeserializer.getAssignnments(apiHelper.getJsonArray());
                                        run1 = true;
                                        startGame();
                                    }
                                });
                                if (chb_rememberme.isChecked()) {
                                    Save();
                                }
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
                                String url = "https://aspcoreapipycm.azurewebsites.net/Dot";
                                //String url = "http://172.16.211.234:56898/Street";
                                apiHelper.getArray(url, new VolleyCallBack() {
                                    @Override
                                    public void onSuccess() {
                                        JSONDeserializer jsonDeserializer2 = new JSONDeserializer();
                                        ApiHelper.streets = jsonDeserializer2.getDots(apiHelper.getJsonArray());
                                        //ApiHelper.streets2 = jsonDeserializer2.getSreets(apiHelper.getJsonArray());
                                        run2 = true;
                                        streetsGenerate();
                                        correctDots();
                                        Log.d("generatedDots", String.valueOf(ApiHelper.generatedDots.size() +" "+ ApiHelper.generatedDots.size()/size ));
                                        startGame();
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread2.start();
                    Thread thread3 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                apiHelper3.getDirectionsApi("https://maps.googleapis.com/maps/api/directions/json?origin=51.229963%2C%204.420749&destination=51.226304%2C%204.426475&mode=walking&key=AIzaSyB4HgIDhaV6sv3ddo_Xol9r4fDLj7RpOaU&fbclid=IwAR3KBusU_zvFk_F4-6K9bhHoT6B2thi_nceJHXLXdMdtCzeuB0k-1m1tMzE", new VolleyCallBack() {
                                    @Override
                                    public void onSuccess() {
                                        JSONDeserializer jsonDeserializer = new JSONDeserializer();
                                        steps = jsonDeserializer.getSteps(apiHelper3.getJsonObject());
                                        run3 = true;
                                        startGame();
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread3.start();
                    userId = apiHelper.getUserId();
                    jwt = apiHelper.getJwt();
                }
            }
        });


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Register.class);
                startActivity(intent);
            }
        });
        btn_dev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), GameActivity.class);
                startActivity(intent);
            }
        });
    }

    private void errorSetter(final String errormsg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                errorChecker.setVisibility(View.VISIBLE);
                errorChecker.setText(errormsg);
            }
        });
    }

    private void Save() {
        SharedPreferences sp = getSharedPreferences("DATA", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        email = edit_email.getText().toString();
        password = edit_password.getText().toString();
        if (chb_rememberme.isChecked()) {
            rememberMe = true;
            editor.putString("email", email);
            editor.putString("pass", password);
        } else {
            rememberMe = false;
            loginauto = false;
            editor.putString("email", "");
            editor.putString("pass", "");
        }
        if (chb_loginauto.isChecked()) {
            loginauto = true;
        } else {
            loginauto = false;
        }
        editor.putBoolean("chbremember", rememberMe);
        editor.putBoolean("chbloginauto", loginauto);
        editor.apply();
    }

    public void Load() {
        SharedPreferences sp = getSharedPreferences("DATA", MODE_PRIVATE);
        String txtuser = sp.getString("email", null);
        String txtpass = sp.getString("pass", null);
        Boolean remembered = sp.getBoolean("chbremember", false);
        Boolean loginauto = sp.getBoolean("chbloginauto", false);
        edit_email.setText(txtuser);
        edit_password.setText(txtpass);
        chb_rememberme.setChecked(remembered);
        chb_loginauto.setChecked(loginauto);
        if (loginauto) {
            JSONObject loginParams = new JSONObject();
            try {
                loginParams.put("password", edit_password.getText().toString());
                loginParams.put("email", edit_email.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private  void correctDots(){
            final String URL = linkGenerator();
            Thread thread4 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        apiHelper.getDirectionsApi(URL, new VolleyCallBack() {
                            @Override
                            public void onSuccess() {
                                JSONDeserializer jsonDeserializer = new JSONDeserializer();
                                ApiHelper.correctedDots.addAll(jsonDeserializer.corrected(apiHelper.getJsonObject()));
                                counter++;
                                if (counter < ApiHelper.generatedDots.size()/size ){
                                    Log.d("correctedDots", String.valueOf(ApiHelper.correctedDots.size()));
                                    correctDots();
                                } else  {
                                    run4 = true;
                                    startGame();
                                }


                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread4.start();
       // }
    }

    private  void streetsGenerate(){
        for (int i = 0; i < ApiHelper.streets.size(); i+=2) {
        GetDotsBetweenAanB(ApiHelper.streets.get(i).getLat(),ApiHelper.streets.get(i).getLon(),ApiHelper.streets.get(i+1).getLat(),ApiHelper.streets.get(i+1).getLon(),ApiHelper.generatedDots);}
        //for (int i = 0; i < ApiHelper.streets2.size(); i++) {
        //GetDotsBetweenAanB(ApiHelper.streets2.get(i).getLatA(),ApiHelper.streets2.get(i).getLonA(),ApiHelper.streets2.get(i).getLatB(),ApiHelper.streets2.get(i).getLonB(),ApiHelper.generatedDots);}
    }

    private String linkGenerator(){
        String getItem = "";
        for(int i = 0; i < size  ; i++) {
            getItem += ApiHelper.generatedDots.get(urlCounter).getLat()+","+ApiHelper.generatedDots.get(urlCounter).getLon();
           if (i < size -1){
               getItem += "|";
           }
           urlCounter ++;
        }

        String URL = "https://roads.googleapis.com/v1/snapToRoads?path="+getItem+"&interpolate=false&key=AIzaSyB4HgIDhaV6sv3ddo_Xol9r4fDLj7RpOaU";

        Log.d("link", URL);
        return URL;
    }
    private void startGame() {
        if (run1 && run2 && run3 && run4 ) {
            Intent intent = new Intent(getBaseContext(), GameActivity.class);
            intent.putExtra("userid", userId);
            intent.putExtra("jwt", jwt);
            startActivity(intent);
        }


    }
}

