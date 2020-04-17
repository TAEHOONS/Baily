package com.example.baily;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ThirdPage extends AppCompatActivity {

    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_page);


    }


    public void mOnClick (View v){
        switch (v.getId()) {
            case R.id.tp_plusBtn: {
                MainScreen();
                break;
            }

        }

    }


    private void MainScreen() {
        Intent intent = new Intent(ThirdPage.this, MainPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        startActivity(intent);

        FirstPage fir=(FirstPage)FirstPage.activity;
        SecondPage scn=(SecondPage)SecondPage.activity;

        fir.finish();
        scn.finish();
        finish();
    }

}
