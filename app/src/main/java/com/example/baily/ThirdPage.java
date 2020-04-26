package com.example.baily;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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


    private void putDataTV(){
        Intent intent=getIntent();

        tvName.setText(intent.getStringExtra("name"));
        tvSex.setText(intent.getStringExtra("sex"));
        tvBrith.setText(intent.getExtras().getInt("year")+"년 "+
                intent.getExtras().getInt("month")+"월 "+intent.getExtras().getInt("day")+"일");
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

        fir.finish();
        scn.finish();
        finish();
    }

    private void usingDB() {
        helper = new DBlink(this, dbName, null, dbVersion);
        db = helper.getWritableDatabase();
    }
}
