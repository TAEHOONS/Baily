package com.example.baily;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.baily.log.FindPwPage3;
import com.example.baily.log.MainActivity;

public class FindPwPage4 extends AppCompatActivity {

    EditText pwEdt;
    EditText pwChkEdt;
    Button pwChkBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw_page4);

        pwEdt=(EditText)findViewById(R.id.pwEdt);
        pwChkEdt=(EditText)findViewById(R.id.pwChkEdt);
        pwChkBtn=(Button)findViewById(R.id.pwChkBtn);
    }
    private void mOnClick(View v){
        switch (v.getId()){
            case R.id.pwChkBtn:
                if (pwEdt.equals(pwChkEdt)) {
                    Intent intent = new Intent(FindPwPage4.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    AlertDialog.Builder ad = new AlertDialog.Builder(FindPwPage4.this);
                    ad.setIcon(R.mipmap.ic_launcher);
                    ad.setTitle("비민번호 재확인 요망");
                    ad.setMessage("비밀번호를 재확인 해주세요.");
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
}
