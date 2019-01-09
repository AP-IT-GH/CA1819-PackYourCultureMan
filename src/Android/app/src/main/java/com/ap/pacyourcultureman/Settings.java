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
import com.ap.pacyourcultureman.Helpers.VolleyCallBack;
import com.ap.pacyourcultureman.Menus.NavigationMenu;

import org.json.JSONObject;

public class Settings extends Activity {
    EditText edit_password,edit_firstname,edit_lastname,edit_email;
    Button btn_apply, btn_cancel;
    String email, firstName, lastName, password,currentUsername;
    ApiHelper apiHelper;
    Player player;
    int skinId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_menu);
        NavigationMenu navigationMenu = new NavigationMenu(this);
        edit_password = findViewById(R.id.edit_password);
        edit_firstname = findViewById(R.id.edit_firstName);
        edit_lastname = findViewById(R.id.edit_lastName);
        edit_email = findViewById(R.id.edit_email);
        btn_apply = findViewById(R.id.btn_apply);
        btn_cancel = findViewById(R.id.btn_cancel);
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
                btn_apply.setEnabled(false);
                apiHelper.put("https://aspcoreapipycm.azurewebsites.net/Users/updateuser/" + Integer.toString(ApiHelper.player.getId()), jsonObject, new VolleyCallBack() {
                    @Override
                    public void onSuccess() {
                        btn_apply.setEnabled(true);
                    }
                });
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while (apiHelper.run) {}
                            if(apiHelper.getmStatusCode() == 200) {
                                ApiHelper.player.setEmail(email);
                                ApiHelper.player.setLastName(lastName);
                                ApiHelper.player.setFirstName(firstName);
                                ApiHelper.player.setSkinId(skinId);
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
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
