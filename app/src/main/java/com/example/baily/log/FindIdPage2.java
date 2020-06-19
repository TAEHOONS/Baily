package com.example.baily.log;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baily.R;

public class FindIdPage2 extends AppCompatActivity {

    EditText fip_emailEdt;
    Button fip_emailBtn;
    String SendRandomCode,nowName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id_page2);
        fip_emailBtn = findViewById(R.id.fip_emailBtn);
        fip_emailEdt = findViewById(R.id.fip_emailEdt);
        Intent intent = getIntent();
        nowName = intent.getStringExtra("nowName");
    }

    public void mOnClick(View v){
        switch (v.getId()){
            case R.id.fip_emailBtn:
                if(fip_emailEdt.length()==0) {
                    Toast.makeText(FindIdPage2.this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                }
                SendMail mailServer = new SendMail();
                mailServer.sendSecurityCode(getApplicationContext(), fip_emailEdt.getText().toString());
                SendRandomCode = mailServer.getRandomNum();


                Log.d("email", "reg_numsendBtn: SendRandomCode=" + SendRandomCode);



                Intent intent1 = new Intent(this,FindIdPage3.class);
                intent1.putExtra("code",SendRandomCode);
                intent1.putExtra("nowName",nowName);
                Log.d("nowName", "email end "+nowName);
                startActivity(intent1);
        }

    }

}