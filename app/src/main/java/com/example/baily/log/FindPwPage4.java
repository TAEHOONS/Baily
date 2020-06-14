package com.example.baily.log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.baily.R;

public class FindPwPage4 extends AppCompatActivity {

    EditText pwEdt;
    EditText pwChkEdt;
    Button pwChkBtn;
    String mpwEdt, mpwChkEdt; //비번같은지 체크하려고


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw_page4);

        pwEdt = (EditText) findViewById(R.id.pwEdt);
        pwChkEdt = (EditText) findViewById(R.id.pwChkEdt);
        pwChkBtn = (Button) findViewById(R.id.pwChkBtn);
    }

    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.pwChkBtn:
                mpwEdt = pwEdt.getText().toString();
                mpwChkEdt = pwChkEdt.getText().toString();
                AlertDialog.Builder ad = new AlertDialog.Builder(FindPwPage4.this);
                ad.setIcon(R.mipmap.ic_launcher);
                if (mpwEdt.equals("")) {
                    ad.setTitle("정보 재확인 요망");
                    ad.setMessage("비밀번호를 입력해주세요");
                    ad.setPositiveButton("확인", null);
                    ad.show();
                } else if (pwChkEdt.equals("")) {
                    ad.setTitle("정보 재확인 요망");
                    ad.setMessage("비밀번호 확인란에 비밀번호를 입력해주세요");
                    ad.setPositiveButton("확인", null);
                    ad.show();
                } else if(!mpwChkEdt.equals(mpwEdt)){
                ad.setTitle("정보 재확인 요망");
                ad.setMessage("비밀번호가 일치하지 않습니다.");
                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();
                }else{//로그인페이지로보내기
                Intent intent = new Intent(FindPwPage4.this, MainActivity.class);
                startActivity(intent);
                }
        }
    }
}
