package com.example.baily.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.baily.DBlink;
import com.example.baily.MainActivity;
import com.example.baily.R;

public class setting extends AppCompatActivity {

    String dbName = "user.db";
    int dbVersion = 3;
    private DBlink helper;
    private SQLiteDatabase db;

    Button mLogout;
    ImageView s_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_home_setting);
        mLogout =(Button)findViewById(R.id.ms_Btn_LogoutBtn);
        s_close = (ImageView)findViewById(R.id.ms_img_closeBtn);

        usingDB();

        s_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thisLogout();
                finish();
            }
        });




    }

    private void thisLogout(){
        Log.d("setting", "delete 끝");
        String deleteThig = "DELETE FROM thisusing ";
        db.execSQL(deleteThig);
        Log.d("setting", "delete 끝");
        ActivityCompat.finishAffinity(this);

        ContentValues values = new ContentValues();
        values.put("_id", 1);
        db.insert("thisusing", null, values);


        Intent intent = new Intent(setting.this, MainActivity.class);
        startActivity(intent);

    }


    private void usingDB(){
        helper = new DBlink(this, dbName, null, dbVersion);
        db = helper.getWritableDatabase();
    }
}
