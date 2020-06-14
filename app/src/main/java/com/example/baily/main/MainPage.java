package com.example.baily.main;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.baily.DBlink;
import com.example.baily.R;
import com.example.baily.babyPlus.HeightAndWeight;
import com.example.baily.caldate;
import com.example.baily.main.home.FragHome;
import com.google.android.material.tabs.TabLayout;
import com.google.protobuf.Value;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//20200521
public class MainPage extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    String dbName = "user.db";
    int dbVersion = 3;
    private DBlink helper;
    private SQLiteDatabase db;

    String mId = "", mBabyname = "", mBirthd = "";
    TextView DayText;
    private FragmentPagerAdapter fragmentPagerAdapter;
    private BackPressClose backPressClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        ViewPager viewPager = findViewById(R.id.viewPager);
        fragmentPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        usingDB();

        backPressClose=new BackPressClose(this);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(0);
        viewPager.setCurrentItem(2);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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
        fragmentPagerAdapter.notifyDataSetChanged();
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
        fragmentPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        backPressClose.onBackPressed();
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                refresh();
                break;
            case 1:
                refresh();
                break;
            case 2:
                refresh();
                break;
            case 3:
                refresh();
                break;
            case 4:
                refresh();
                break;
        }

    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }

    private void refresh() {
        fragmentPagerAdapter.notifyDataSetChanged();
    }


}