package com.ap.pacyourcultureman;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ap.pacyourcultureman.Helpers.ApiHelper;
import com.ap.pacyourcultureman.Helpers.JSONSerializer;

import org.json.JSONObject;

public class Settings extends Activity {
    EditText edit_password,edit_firstname,edit_lastname,edit_email;
    String email, firstName, lastName, password;
    Button btn_apply;
    ApiHelper apiHelper;
    Player player;
    int skinId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_menu);

        edit_password = findViewById(R.id.edit_password);
        edit_firstname = findViewById(R.id.edit_firstName);
        edit_lastname = findViewById(R.id.edit_lastName);
        edit_email = findViewById(R.id.edit_email);
        btn_apply = findViewById(R.id.btn_apply);
        player = ApiHelper.player;
        edit_email.setText(player.getEmail());
        edit_firstname.setText(player.getFirstName());
        edit_lastname.setText(player.getLastName());
        edit_password.setText("");
        apiHelper = new ApiHelper();
        skinId = 1;

        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("pasw","hit");
                email = edit_email.getText().toString();
                firstName = edit_firstname.getText().toString();
                lastName = edit_lastname.getText().toString();
                password = edit_password.getText().toString();
                if(password.matches("")){
                    password = "string";
                }
                Log.d("xxx",password);
                JSONSerializer jsonSerializer = new JSONSerializer();
                JSONObject jsonObject = jsonSerializer.jsonPutUserData(skinId, firstName, lastName, email, password);
                apiHelper.put("https://aspcoreapipycm.azurewebsites.net/Users/updateuser/" + Integer.toString(ApiHelper.player.getId()), jsonObject);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while (apiHelper.run) {}
                            if(apiHelper.getmStatusCode() == 200) {
                                ApiHelper.player.setEmail(email);
                                ApiHelper.player.setLastName(lastName);
                                ApiHelper.player.setFirstName(firstName);
                                ApiHelper.player.setId(skinId);
                                Log.d("Test", "Test");
                            }
                            Intent i = new Intent(getBaseContext(), GameActivity.class);
                            startActivity(i);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        });
    }
}
