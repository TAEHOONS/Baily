package com.example.baily.log;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.baily.R;

public class FindIdPage3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id_page3);

    }

    public void mOnClick(View v){
        switch (v.getId()){
            case  R.id.fip_emailnumBtn: {
                AlertDialog.Builder ad = new AlertDialog.Builder(FindIdPage3.this);
                ad.setIcon(R.mipmap.ic_launcher);
                ad.setTitle("회원가입시 입력한 아이디입니다.");
                ad.setMessage("여기에 이제 아이디랑 이메일 확인해서 아이디 보여주기코드 넣어야함");


                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();
                break;
            }
        }

    }

}
