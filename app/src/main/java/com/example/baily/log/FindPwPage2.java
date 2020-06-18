package com.example.baily.log;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baily.R;

public class FindPwPage2 extends AppCompatActivity {

    Button fpp_emailBtn;
    EditText fpp_emailEdt;
    String SendRandomCode;
    String nowId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw_page2);
        fpp_emailBtn = findViewById(R.id.fpp_emailBtn);
        fpp_emailEdt = findViewById(R.id.fpp_emailEdt);

        Intent intent = getIntent();
        nowId = intent.getStringExtra("nowId");
    }
    public void mOnClick(View v){
        switch (v.getId()){
            case R.id.fpp_emailBtn:
                SendMail mailServer = new SendMail();
                mailServer.sendSecurityCode(getApplicationContext(), fpp_emailEdt.getText().toString());
                SendRandomCode = mailServer.getRandomNum();


                Log.d("email", "reg_numsendBtn: SendRandomCode=" + SendRandomCode);

                Log.d("email", "email end");

                Intent intent = new Intent(this,FindPwPage3.class);
                intent.putExtra("code",SendRandomCode);
                intent.putExtra("nowId",nowId);
                startActivity(intent);

        }
    }

}
