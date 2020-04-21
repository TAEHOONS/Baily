package com.example.baily;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class FindPwPage3 extends AppCompatActivity {

    Button fpp_emailnumBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw_page3);

        fpp_emailnumBtn=(Button)findViewById(R.id.fpp_emailnumBtn);
    }

    public void mOnClick(View v){
        switch(v.getId()){
            case R.id.fpp_emailnumBtn:
                AlertDialog.Builder ad = new AlertDialog.Builder(FindPwPage3.this);
                ad.setIcon(R.mipmap.ic_launcher);
                ad.setTitle("인증 완료");
                ad.setMessage("새 비밀번호를 작성해주세요.");


                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();
        }
    }
}
