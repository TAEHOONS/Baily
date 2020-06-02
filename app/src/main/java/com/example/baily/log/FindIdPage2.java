package com.example.baily.log;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baily.R;

public class FindIdPage2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id_page2);
    }


    // 화면이동 -> FIND ID_확인BTN->이메일 확인페이지
    private void FindIdScreen3() {
        Intent intent = new Intent(FindIdPage2.this, FindIdPage3.class);
        startActivity(intent);
    }
}