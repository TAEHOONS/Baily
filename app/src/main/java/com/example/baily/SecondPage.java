package com.example.baily;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SecondPage extends AppCompatActivity {

    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_page);

        activity = this;

    }



    public void mOnClick (View v){
        switch (v.getId()) {
            case R.id.sp_plusBtn: {
                ThirdScreen();
                break;
            }

        }

    }

    private void ThirdScreen() {
        Intent intent = new Intent(SecondPage.this, ThirdPage.class);
        startActivity(intent);

    }
}
