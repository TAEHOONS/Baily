package com.example.baily;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ThirdPage extends AppCompatActivity {

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
        startActivity(intent);

    }

}
