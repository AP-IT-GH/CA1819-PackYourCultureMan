package com.ap.pacyourcultureman;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.commons.validator.routines.EmailValidator;
import org.w3c.dom.Text;

public class Register extends Activity {
    TextView txt_Errorchecker;
    Button btn_Register;
    EditText edit_user, edit_email, edit_firstName, edit_lastName, edit_password, edit_confirmPassword;
    String username, email, firstName, lastName, password, confirmpassword, errorcheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_form);
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
                if(password != confirmpassword) {
                    txt_Errorchecker.setText("Passwords need to match");
                    txt_Errorchecker.setVisibility(View.VISIBLE);
                }
                Boolean correctEmail = EmailValidator.getInstance().isValid(email);
                if(!correctEmail) {
                    txt_Errorchecker.setText("Please enter a valid email address");
                    txt_Errorchecker.setVisibility(View.VISIBLE);
                }
                else {

                }
            }
        });
    }
}
