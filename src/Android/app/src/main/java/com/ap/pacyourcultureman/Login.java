package com.ap.pacyourcultureman;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import org.apache.commons.validator.routines.EmailValidator;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends Activity {
    private String email, password, targetURL, reply;
    Button btn_login, btn_register, btn_dev;
    EditText edit_email, edit_password;
    TextView errorChecker;
    CheckBox chb_rememberme, chb_loginauto;
    Boolean rememberMe, loginauto;
    HttpURLConnection conn;
    URL url;
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_form);
        targetURL = "https://pacyourculturemanapi.azurewebsites.net/users/authenticate";
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        btn_dev = findViewById(R.id.btn_dev);
        edit_email = findViewById(R.id.edit_mail);
        edit_password = findViewById(R.id.edit_pass);
        errorChecker = findViewById(R.id.txt_errorchecker);
        chb_rememberme = findViewById(R.id.login_chb_rememember);
        chb_loginauto = findViewById(R.id.login_chb_autologin);
        Intent intent = getIntent();
        Load();
        String intentuser = intent.getStringExtra("username");
        String intentpassword = intent.getStringExtra("pass");
        if(intentuser != null && intentpassword != null) {
            edit_password.setText(intentpassword);
            edit_email.setText(intentuser);
            chb_loginauto.setChecked(false);
            chb_rememberme.setChecked(false);
        }
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorChecker.setVisibility(View.GONE);
                email = edit_email.getText().toString();
                password = edit_password.getText().toString();
                Boolean correctEmail = EmailValidator.getInstance().isValid(email);
                if(correctEmail) {
                        Save();
                        sendPost();
                }
                else {
                    errorChecker.setVisibility(View.VISIBLE);
                    errorChecker.setText("Please enter a correct email address.");
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
    public void sendPost() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    url = new URL(targetURL);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("username", email);
                    jsonParam.put("password", password);
                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());
                    os.flush();
                    StringBuilder stringBuilder = new StringBuilder();
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
                        InputStreamReader streamReader = new InputStreamReader(conn.getErrorStream());
                        BufferedReader bufferedReader = new BufferedReader(streamReader);
                        String response = null;
                        while ((response = bufferedReader.readLine()) != null) {
                            stringBuilder.append(response + "\n");
                        }
                        bufferedReader.close();
                        Log.d("TAG" ,stringBuilder.toString());
                        reply = stringBuilder.toString();
                        reply = reply.substring(reply.indexOf(':') + 2, reply.lastIndexOf('"'));
                        Log.i("Reply", reply);
                        errorSetter(reply);
                    }
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        reply = "Login success";
                        errorSetter(reply);
                        Looper.prepare();
                        mHandler = new Handler();
                        mHandler.postDelayed(mLaunchTask, 1500);
                        Looper.loop();
                    }
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        reply = "Unauthorized";
                    }
                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG" , conn.getResponseMessage());
                    os.close();
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

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
        if (chb_rememberme.isChecked()) {
            rememberMe = true;
            editor.putString("email", email);
            editor.putString("pass", password);
        }
        else {
            rememberMe = false;
            loginauto = false;
            editor.putString("email", "");
            editor.putString("pass",  "");
        }
        if(chb_loginauto.isChecked()) {
            loginauto = true;
        }
        else {
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
        if(loginauto) {
            JSONObject loginParams = new JSONObject();
            try {
                loginParams.put("password", edit_password.getText().toString());
                loginParams.put("email", edit_email.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private Runnable mLaunchTask = new Runnable() {
        public void run() {
            Intent i = new Intent(getApplicationContext(),GameActivity.class);
            startActivity(i);
        }
    };
}
