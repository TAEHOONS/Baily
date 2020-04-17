package com.example.baily;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button gotoRegisterPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gotoRegisterPage = (Button)findViewById(R.id.lp_logJoin);
        gotoRegisterPage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent01 = new Intent(MainActivity.this, RegisterPage.class);
        startActivity(intent01);
    }
}
