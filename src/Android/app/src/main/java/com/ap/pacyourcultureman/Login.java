package com.ap.pacyourcultureman;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;

import org.apache.commons.validator.routines.EmailValidator;

public class Login extends Activity {
    private String email;
    private String password;
    Button btn_login, btn_register, btn_dev;
    EditText edit_email, edit_password;
    TextView errorChecker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_form);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        btn_dev = findViewById(R.id.btn_dev);
        edit_email = findViewById(R.id.edit_mail);
        edit_password = findViewById(R.id.edit_pass);
        errorChecker = findViewById(R.id.txt_errorchecker);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorChecker.setVisibility(View.GONE);
                email = edit_email.getText().toString();
                Boolean correctEmail = EmailValidator.getInstance().isValid(email);
                if(correctEmail) {

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


}
