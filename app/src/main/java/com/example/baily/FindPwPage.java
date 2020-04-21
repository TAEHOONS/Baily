package com.example.baily;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class FindPwPage extends AppCompatActivity {

    Button fpp_idBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw_page);

        fpp_idBtn=(Button)findViewById(R.id.fpp_idBtn);
    }

    //아이디 입력 후 확인버튼 누를 시
    public void mOnClick(View v){
        switch (v.getId()){
            case R.id.fpp_idBtn:
                FindEmailScreen();


        }

    }

    // 화면이동 -> FINDPWpage 아이디_확인BTN->이메일 확인페이지
    private void FindEmailScreen() {
        Intent intent = new Intent(FindPwPage.this, FindPwPage2.class);
        startActivity(intent);
    }
}
