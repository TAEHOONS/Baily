package com.example.baily.log;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baily.R;

public class FindIdPage2 extends AppCompatActivity {
    EditText fip_emailEdt;
    Button fip_emailBtn;
    String SendRandomCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id_page2);
        fip_emailBtn = findViewById(R.id.fip_emailBtn);
        fip_emailEdt = findViewById(R.id.fip_emailEdt);
    }

    public void mOnClick(View v){
        switch (v.getId()){
            case R.id.fip_emailBtn:
                SendMail mailServer = new SendMail();
                mailServer.sendSecurityCode(getApplicationContext(), fip_emailEdt.getText().toString());
                SendRandomCode = mailServer.getRandomNum();


                Log.d("email", "reg_numsendBtn: SendRandomCode=" + SendRandomCode);

                Log.d("email", "email end");

                Intent intent = new Intent(this,FindIdPage3.class);
                intent.putExtra("code",SendRandomCode);

                startActivity(intent);
               // FindIdScreen3();
        }
    }

    // 화면이동 -> FIND ID_확인BTN->이메일 확인페이지
    private void FindIdScreen3() {
       // Intent intent = new Intent(FindIdPage2.this, FindIdPage3.class);
       // startActivity(intent);
    }
}