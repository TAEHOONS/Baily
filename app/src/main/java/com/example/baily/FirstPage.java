package com.example.baily;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

public class FirstPage extends AppCompatActivity {

    String dbName = "user.db";
    int dbVersion = 3;
    private DBlink helper;
    private SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

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
        startActivity(intent);
        finish();
    }


    private void usingDB(){
        helper = new DBlink(this, dbName, null, dbVersion);
        db = helper.getWritableDatabase();
    }
}
