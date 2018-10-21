package com.ap.pacyourcultureman;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.commons.validator.routines.EmailValidator;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Register extends Activity {
    TextView txt_Errorchecker;
    Button btn_Register;
    EditText edit_user, edit_email, edit_firstName, edit_lastName, edit_password, edit_confirmPassword;
    String username, email, firstName, lastName, password, confirmpassword, targetURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_form);
        targetURL = "http://localhost:56898/users/register";
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
                    JSONObject loginParams = new JSONObject();
                    try {
                        loginParams.put("username", username);
                        loginParams.put("password", password);
                        loginParams.put("firstname", firstName);
                        loginParams.put("lastname", lastName);
                        loginParams.put("email", email);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    executePost(targetURL,loginParams.toString());
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
    private void errorSetter(String errormsg) {
        txt_Errorchecker.setVisibility(View.VISIBLE);
        txt_Errorchecker.setText(errormsg);
    }
    public String executePost(String targetURL,String urlParameters) {
        int timeout=5000;
        URL url;
        HttpURLConnection connection = null;
        try {
            // Create connection

            url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/json");

            connection.setRequestProperty("Content-Length",
                    "" + Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);

            // Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            // Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            errorSetter(response.toString());
            return response.toString();

        } catch (SocketTimeoutException ex) {
            ex.printStackTrace();

        } catch (MalformedURLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException ex) {

            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }
}
