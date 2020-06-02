package com.example.baily.main;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.baily.DBlink;
import com.example.baily.R;
import com.example.baily.caldate;
import com.example.baily.main.home.FragHome;
import com.google.android.material.tabs.TabLayout;
import com.google.protobuf.Value;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//20200521
public class MainPage extends AppCompatActivity {

    String dbName = "user.db";
    int dbVersion = 3;
    private DBlink helper;
    private SQLiteDatabase db;

    String mId = "", mBabyname = "", mBirthd = "";
    TextView DayText;
    private FragmentPagerAdapter fragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        ViewPager viewPager = findViewById(R.id.viewPager);
        fragmentPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        usingDB();


        TabLayout tabLayout = findViewById(R.id.tab_layout);
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        ImageView setBtn = (ImageView) findViewById(R.id.mft_setBtn);
        DayText = (TextView) findViewById(R.id.mft_dDayTxt);
        getDay();
        setBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), setting.class);
                startActivity(intent);
            }
        });
    }

    // DB 연결
    private void usingDB() {
        helper = new DBlink(this, dbName, null, dbVersion);
        db = helper.getWritableDatabase();

    }


    // 현재값 받기
    public void getDay() {
        String sql = "select * from thisusing where _id=1"; // 검색용
        Cursor cursor = db.rawQuery(sql, null);
        int BYear = 0, BMonth = 0, BDay = 0;

        // 기본 데이터
        while (cursor.moveToNext()) {
            mId = cursor.getString(1);
            mBabyname = cursor.getString(2);
            Log.d("Home", "db받기 id = " + mId + "  현재 아기 = " + mBabyname);
        }

        // 현재 사용 아기데이터
        sql = "select * from baby where name='" + mBabyname + "'"; // 검색용
        cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            BYear = cursor.getInt(3);
            BMonth = cursor.getInt(4);
            BDay = cursor.getInt(5);
        }
        caldate caldate=new caldate(BYear,BMonth,BDay);
        DayText.setText("D + "+caldate.result);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ImageView setBtn = (ImageView) findViewById(R.id.mft_setBtn);
        DayText = (TextView) findViewById(R.id.mft_dDayTxt);
        getDay();
        setBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), setting.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        ViewPager viewPager = findViewById(R.id.viewPager);
        fragmentPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        usingDB();


        TabLayout tabLayout = findViewById(R.id.tab_layout);
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

}