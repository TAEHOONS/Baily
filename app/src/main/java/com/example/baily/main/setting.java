package com.example.baily.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.baily.DBlink;
import com.example.baily.log.MainActivity;
import com.example.baily.R;

public class setting extends AppCompatActivity {

    String dbName = "user.db";
    int dbVersion = 3;
    private DBlink helper;
    private SQLiteDatabase db;

    private String mId, mBabyname,mUserName,mEmail;

    Button mLogout,mDelete;
    ImageView s_close;
    TextView NameTV,EmailTV,IdTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_home_setting);

        NameTV=(TextView)findViewById(R.id.ms_TV_username);
        EmailTV=(TextView)findViewById(R.id.ms_TV_Email);
        IdTV=(TextView)findViewById(R.id.ms_TV_id);
        mLogout =(Button)findViewById(R.id.ms_Btn_LogoutBtn);
        mDelete = (Button)findViewById(R.id.ms_Btn_delete);
        s_close = (ImageView)findViewById(R.id.ms_img_closeBtn);


        usingDB();
        getDBdata();
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
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteId();
                finish();
            }
        });



    }
    // 로그아웃
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
    private void DeleteId(){
        // 지금 로그인 지우기
        String deleteThig = "DELETE FROM thisusing ";
        db.execSQL(deleteThig);
        // 아이디 데이터들 지우기
        deleteThig = "DELETE FROM baby ";
        db.execSQL(deleteThig);
        // 아이디 삭제
        deleteThig = "DELETE FROM user ";
        db.execSQL(deleteThig);

        ActivityCompat.finishAffinity(this);
        ContentValues values = new ContentValues();
        values.put("_id", 1);
        db.insert("thisusing", null, values);


        Intent intent = new Intent(setting.this, MainActivity.class);
        startActivity(intent);

    }

    private void getDBdata() {


        String sql = "select * from thisusing where _id=1"; // 검색용
        Cursor cursor = db.rawQuery(sql, null);

        // 기본 데이터
        while (cursor.moveToNext()) {
            mId = cursor.getString(1);
            mBabyname = cursor.getString(2);
            Log.d("Home", "db받기 id = " + mId + "  현재 아기 = " + mBabyname);
        }

        // 현재 사용 아기데이터
        sql = "select * from user where id='" + mId + "'"; // 검색용
        cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            mUserName = cursor.getString(3);
            mEmail = cursor.getString(4);
        }
        NameTV.setText(mUserName);
        EmailTV.setText(mEmail);
        IdTV.setText(mId);;


    }

    private void usingDB(){
        helper = new DBlink(this, dbName, null, dbVersion);
        db = helper.getWritableDatabase();
    }
}
