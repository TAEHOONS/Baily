package com.example.baily;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ThirdPage extends AppCompatActivity {

    String dbName = "user.db";
    int dbVersion = 3;
    private DBlink helper;
    private SQLiteDatabase db;

    TextView tvName, tvSex, tvBrith, tvHeadline, tvHAW;

    public static Activity activity;


    private String mLoginId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_page);
        Intent intent = getIntent();
        mLoginId = intent.getStringExtra("login");
        usingDB();

        tvName = (TextView) findViewById(R.id.tp_nameTV);
        tvSex = (TextView) findViewById(R.id.tp_sexTV);
        tvBrith = (TextView) findViewById(R.id.tp_berthTV);
        tvHeadline = (TextView) findViewById(R.id.tp_headlineTV);
        tvHAW = (TextView) findViewById(R.id.tp_HWATV);
        putDataTV();
    }


    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.tp_plusBtn: {
                MainScreen();
                break;
            }

        }

    }


    private void putDataTV() {
        Intent intent = getIntent();

        tvName.setText(intent.getStringExtra("name"));
        tvSex.setText(intent.getStringExtra("sex"));
        tvBrith.setText(intent.getExtras().getInt("year") + "년 " +
                intent.getExtras().getInt("month") + "월 " + intent.getExtras().getInt("day") + "일");
        tvHeadline.setText(intent.getStringExtra("headline"));
        tvHAW.setText(intent.getStringExtra("weight") + "Kg  " + intent.getStringExtra("height") + "cm");
    }

    private void MainScreen() {
        Intent intent = new Intent(ThirdPage.this, MainPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.putExtra("login", mLoginId);
        startActivity(intent);

        FirstPage fir = (FirstPage) FirstPage.activity;
        SecondPage scn = (SecondPage) SecondPage.activity;

        insertBabyData();

        fir.finish();
        scn.finish();
        finish();
    }

    private void insertBabyData() {
        Intent intent = getIntent();
        Log.d("insertBabyData", "시작");
        Log.d("insertBabyData", intent.getStringExtra("name"));
        Log.d("insertBabyData", intent.getStringExtra("sex"));
        Log.d("insertBabyData", "숫자 : "+intent.getExtras().getInt("year"));
        //선언
        ContentValues values = new ContentValues();
        //values에 테이블의 column에 넣을값 x 넣기
        //고정 변수는 ""로 하고 변수로 할꺼면 그냥 하기
        //컬럼 이름은 DBlink.java 참조
        values.put("name", intent.getStringExtra("name"));
        values.put("sex", intent.getStringExtra("sex"));
        values.put("ybirth", intent.getExtras().getInt("year"));
        values.put("mbirth", intent.getExtras().getInt("month"));
        values.put("dbirthy", intent.getExtras().getInt("day"));
        values.put("headline", intent.getStringExtra("headline"));
        values.put("tall", intent.getStringExtra("height"));
        values.put("weight", intent.getStringExtra("weight"));
        values.put("parents", mLoginId);
        // 테이블 이름 + 이제까지 입력한것을 저장한 변수(values)
        db.insert("baby", null, values);
    }


    private void usingDB() {
        helper = new DBlink(this, dbName, null, dbVersion);
        db = helper.getWritableDatabase();
    }
}
