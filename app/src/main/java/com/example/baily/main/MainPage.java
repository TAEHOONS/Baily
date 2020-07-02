package com.example.baily.main;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.FragmentTransaction;
import android.content.Context;
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
import com.example.baily.log.RegisterPage;
import com.example.baily.main.home.FragHome;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.protobuf.Value;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

//20200521
public class MainPage extends AppCompatActivity implements ViewPager.OnPageChangeListener {

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
        Context context = getApplicationContext();
        fragmentPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), context);
        usingDB();

        backPressClose = new BackPressClose(this);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


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
        caldate caldate = new caldate(BYear, BMonth, BDay);
        DayText.setText("D + " + caldate.result);
        fragmentPagerAdapter.notifyDataSetChanged();
    }
// 파이어 베이스 저장용
/*
    @Override
    protected void onPause() {
        super.onPause();
        FirebaseFirestore firedb = FirebaseFirestore.getInstance();
        firedb.collection(mId + "baby").document().delete();
        firedb.collection(mId + "growth").document().delete();
        firedb.collection(mId + "recode").document().delete();
        firedb.collection(mId + "event").document().delete();

        // baby 검색
        String sql = "select * from baby where parents='" + mId + "' "; // 검색용
        Cursor c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            baby member = new baby(c.getString(1), c.getString(2), c.getInt(3)
                    , c.getInt(4), c.getInt(5), c.getString(6), c.getString(7)
                    , c.getString(8), c.getString(9), c.getString(10));
            firedb.collection(mId + "baby").document(c.getString(0)).set(member);
        }
        c.close();
        // growlog 검색
         sql = "select * from growlog where parents='" + mId + "' "; // 검색용
         c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            growthlog member = new growthlog(c.getString(1), c.getString(2), c.getString(3)
                    , c.getString(4), c.getString(5), c.getString(6), c.getString(7)
                    , c.getString(8));
            firedb.collection(mId + "growlog").document(c.getString(0)).set(member);
        }
        c.close();
        // recode 검색
         sql = "select * from recode where parents='" + mId + "' "; // 검색용
         c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            recode member = new recode(c.getString(1), c.getString(2), c.getString(3)
                    , c.getString(4), c.getString(5), c.getString(6), c.getString(7)
                    , c.getString(8));
            firedb.collection(mId + "recode").document(c.getString(0)).set(member);
        }
        c.close();
        // event 검색
        sql = "select * from events where parents='" + mId + "' "; // 검색용
        c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            diary member = new diary(c.getString(1), c.getString(2), c.getString(3)
                    , c.getString(4), c.getString(5));
            firedb.collection(mId + "events").document(c.getString(0)).set(member);
        }
        c.close();
    }*/

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

    public class baby {
        public int year, month, day;
        public String name, sex, headline, tall, weight, parents, imgpath;

        public baby() {
        }

        public baby(String name, String sex, int year, int month, int day,
                    String headline, String tall, String weight, String parents, String imgpath) {
            this.name = name;
            this.sex = sex;
            this.headline = headline;
            this.tall = tall;
            this.weight = weight;
            this.parents = parents;
            this.imgpath = imgpath;
            this.year = year;
            this.month = month;
            this.day = day;
        }

    }

    public class growthlog {

        public String name, weight, tall, headline, fever, date, caldate, parents;;

        public growthlog() {
        }

        public growthlog(String name, String weight, String tall, String headline, String fever
                , String date, String caldate, String parents) {
            this.name = name; this.weight = weight; this.tall = tall;
            this.headline = headline; this.fever = fever; this.date = date;
            this.caldate = caldate; this.parents = parents;
        }

    }

    public class recode {

        public String name, date, time, title, subt, contents1, contents2, parents;

        public recode() {

        }

        public recode(String name, String date, String time, String title, String subt
                , String contents1, String contents2, String parents) {
            this.name = name;this.date = date;this.time = time;
            this.title = title;this.subt = subt;this.contents1 = contents1;
            this.contents2 = contents2;this.parents = parents;
        }

    }

    public class diary {

        public String name,title , date , memo ,parents ;

        public diary() {
        }

        public diary(String name, String title, String date, String memo, String parents) {
            this.name = name; this.title = title;
            this.date = date; this.memo = memo;
            this.parents = parents;
        }

    }


}