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

import com.example.baily.FindPwPage4;
import com.example.baily.R;

public class FindPwPage3 extends AppCompatActivity {

    Button fpp_emailnumBtn;
    EditText fpp_emailnumEdt;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw_page3);
        fpp_emailnumEdt=(EditText)findViewById(R.id.fpp_emailnumEdt);
        fpp_emailnumBtn=(Button)findViewById(R.id.fpp_emailnumBtn);

    }

    public void mOnClick(View v){
        Intent intent = getIntent();
        String SendRandomCode = intent.getStringExtra("code");

        Log.d("email", " SendRandomCode=" + SendRandomCode);
        switch(v.getId()){
            case R.id.fpp_emailnumBtn:
                //인증번호를 입력하지 않은 경우 > null 값 처리
                String emailAuth_num = fpp_emailnumEdt.getText().toString();
                if (emailAuth_num.getBytes().length <= 0) {
                    Toast.makeText(this, "이메일 인증번호 입력", Toast.LENGTH_SHORT).show();
                } else {
                    String user_answer = fpp_emailnumEdt.getText().toString();
                    if (user_answer.equals(SendRandomCode)) {
                        Toast.makeText(this, "이메일 인증 성공", Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder ad = new AlertDialog.Builder(FindPwPage3.this);
                        ad.setIcon(R.mipmap.ic_launcher);
                        ad.setTitle("인증 완료");
                        ad.setMessage("새 비밀번호를 작성해주세요.");
                        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent2 = new Intent(FindPwPage3.this,FindPwPage4.class);
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
