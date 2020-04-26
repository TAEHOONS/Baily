package com.example.baily;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

public class FirstPage extends AppCompatActivity {

    public static Activity activity;

    String dbName = "user.db";
    int dbVersion = 3;
    private DBlink helper;
    private SQLiteDatabase db;

    private String mLoginId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        Intent intent=getIntent();
        mLoginId =intent.getStringExtra("login");
        activity = this;

        usingDB();


    }

    public void mOnClick (View v){
        switch (v.getId()) {
            case R.id.fp_plusBtn: {
                SecondScreen();
                break;
            }

        }

    }


    private void SecondScreen() {
        Intent intent = new Intent(FirstPage.this, SecondPage.class);
        intent.putExtra("login",mLoginId);
        startActivity(intent);
    }


    private void usingDB(){
        helper = new DBlink(this, dbName, null, dbVersion);
        db = helper.getWritableDatabase();
    }
}
