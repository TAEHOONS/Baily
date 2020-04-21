package com.example.baily;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class FindIdPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);
    }

    //아이디 확인 버튼 처리
    public void mOnClick(View v){
        switch (v.getId()){
            case  R.id.fip_nameBtn: {
                FindIdScreen();
                break;
            }
        }

    }


    // 화면이동 -> FIND ID_확인BTN->이메일 확인페이지
    private void FindIdScreen() {
        Intent intent = new Intent(FindIdPage.this, FindIdPage2.class);
        startActivity(intent);
    }
}
