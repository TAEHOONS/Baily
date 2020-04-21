package com.example.baily;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class FindPwPage2 extends AppCompatActivity {

    Button fpp_emailBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw_page2);

        fpp_emailBtn = (Button)findViewById(R.id.fpp_emailBtn);

    }
    public void mOnClick(View v){
        switch (v.getId()){
            case R.id.fpp_emailBtn:
                FindEmailCkScreen();
        }
    }

    // 화면이동 -> FindPwPage2 이메일_확인Btn->이메일 인증번호 확인페이지
    private void FindEmailCkScreen() {
        Intent intent = new Intent(FindPwPage2.this, FindPwPage3.class);
        startActivity(intent);
    }
}
