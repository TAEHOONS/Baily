package com.example.baily;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;

public class RegisterPage extends AppCompatActivity {
    private String userID;
    private String userPassword;
    private String userPasswordCk;
    private String userEmail;
    //여기에 추가적으로 인증번호 해야합니다요요요우


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        final EditText nameText = (EditText) findViewById(R.id.reg_nameEdt);
        final EditText idText = (EditText) findViewById(R.id.reg_idEdt);
        final EditText pwdText = (EditText)findViewById(R.id.reg_pwdEdt);
        final EditText repwdText = (EditText)findViewById(R.id.reg_repwdEdt);
        final EditText emailText = (EditText)findViewById(R.id.reg_emailnumEdt);

    }
}
