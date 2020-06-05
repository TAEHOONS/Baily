package com.example.baily.log;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.example.baily.R;

public class FindIdPage3 extends AppCompatActivity {

    EditText fip_emailnunEdt;
    Button fip_emailnumBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id_page3);
        fip_emailnunEdt = findViewById(R.id.fip_emailnunEdt);
        fip_emailnumBtn = findViewById(R.id.fip_emailnumBtn);
    }

    public void mOnClick(View v) {
        Intent intent = getIntent();
        String SendRandomCode = intent.getStringExtra("code");

        Log.d("email", " SendRandomCode=" + SendRandomCode);
        switch (v.getId()) {
            case R.id.fip_emailnumBtn:
                //인증번호를 입력하지 않은 경우 > null 값 처리
                String emailAuth_num = fip_emailnunEdt.getText().toString();
                if (emailAuth_num.getBytes().length <= 0) {
                    Toast.makeText(this, "이메일 인증번호 입력", Toast.LENGTH_SHORT).show();
                } else {
                    String user_answer = fip_emailnunEdt.getText().toString();
                    if (user_answer.equals(SendRandomCode)) {
                        Toast.makeText(this, "이메일 인증 성공", Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder ad = new AlertDialog.Builder(FindIdPage3.this);
                        ad.setIcon(R.mipmap.ic_launcher);
                        ad.setTitle("인증 완료");
                        ad.setMessage("여기에 이제 가입시 아이디 보여줘야댐");
                        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent2 = new Intent(FindIdPage3.this, MainActivity.class);
                                startActivity(intent2);
                                dialog.dismiss();

                            }
                        });
                        ad.show();

                    } else {
                        Toast.makeText(this, "이메일 인증 실패", Toast.LENGTH_SHORT).show();
                    }
                }

        }

    }
}


