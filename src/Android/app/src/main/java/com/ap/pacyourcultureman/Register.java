package com.ap.pacyourcultureman;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.commons.validator.routines.EmailValidator;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.Handler;
import com.ap.pacyourcultureman.Helpers.ApiHelper;

public class Register extends Activity {
    TextView txt_Errorchecker;
    Button btn_Register;
    EditText edit_user, edit_email, edit_firstName, edit_lastName, edit_password, edit_confirmPassword;
    String username, email, firstName, lastName, password, confirmpassword, targetURL, resp, reply;
    private Handler mHandler;
    ApiHelper apiHelper;
    HttpURLConnection conn;
    URL url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_form);
        targetURL = "https://pacyourculturemanapi.azurewebsites.net/users/register";
        resp = "";
        reply = "";
        apiHelper = new ApiHelper(targetURL);
        txt_Errorchecker = findViewById(R.id.reg_txt_errorCecker);
        btn_Register = findViewById(R.id.reg_btn_register);
        edit_user = findViewById(R.id.reg_edit_username);
        edit_email = findViewById(R.id.reg_edit_email);
        edit_firstName = findViewById(R.id.reg_edit_firstName);
        edit_lastName = findViewById(R.id.reg_edit_lastName);
        edit_password = findViewById(R.id.reg_edit_password);
        edit_confirmPassword = findViewById(R.id.reg_edit_checkPassword);
        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = edit_user.getText().toString();
                email = edit_email.getText().toString();
                firstName = edit_firstName.getText().toString();
                lastName = edit_lastName.getText().toString();
                password = edit_password.getText().toString();
                confirmpassword = edit_confirmPassword.getText().toString();
                txt_Errorchecker.setVisibility(View.GONE);
                if(!password.equals(confirmpassword)) {
                    errorSetter("Passwords must match");
                }
                if(username == null || username.length() <= 3) {
                    errorSetter("Please enter a username with a minimum of 3 characters");
                }
                if(lastName == null || lastName == null || firstName.equals(lastName)) {
                    errorSetter("Please enter your full name");
                }
                Boolean correctEmail = EmailValidator.getInstance().isValid(email);
                if(!correctEmail) {
                    errorSetter("Please enter a valid email address");
                }
                if(!passwordChecker(password)) {
                    errorSetter("Password must include atleast 1 lowercase char, 1 uppercase char and 1 digit");
                }
                else {
                    sendPost();
                }
            }
        });
    }
    private boolean passwordChecker(String password) {
        String regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}";
        if(password.matches(regexp)) {
            return true;
        }
        else {
            return false;
        }
    }
    private void errorSetter(final String errormsg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txt_Errorchecker.setVisibility(View.VISIBLE);
                txt_Errorchecker.setText(errormsg);
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
                    jsonParam.put("username", username);
                    jsonParam.put("password", password);
                    jsonParam.put("firstname", firstName);
                    jsonParam.put("lastname", lastName);
                    jsonParam.put("email", email);
                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
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
                        reply = "User created";
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
    private Runnable mLaunchTask = new Runnable() {
        public void run() {
            Intent i = new Intent(getApplicationContext(),Login.class);
            i.putExtra("username", username);
            i.putExtra("pass", password);
            startActivity(i);
        }
    };
}
