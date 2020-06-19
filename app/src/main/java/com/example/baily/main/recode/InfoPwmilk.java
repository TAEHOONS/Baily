package com.example.baily.main.recode;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baily.DBlink;
import com.example.baily.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class InfoPwmilk extends AppCompatActivity {

    String dbName = "user.db", mId, mBabyname;
    int dbVersion = 3, infoId, INFO_NULL = 999999999;
    private DBlink helper;
    private SQLiteDatabase db;


    String tthou, ttmin, memo, getHour, getMinu, saveTime, lastTime;
    String test = null;
    Button tagAdd;
    ImageView back, end;
    private SeekBar mSeekBar;
    private int mSeekBarVal = 0;

    Calendar myCalender = Calendar.getInstance();
    int hour = myCalender.get(Calendar.HOUR_OF_DAY);
    int minute = myCalender.get(Calendar.MINUTE);


    EditText edmemo;
    TextView tSum, eatpwm, startDate, endDate;

    int strt, endt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recode_pwmilk);

        edmemo = findViewById(R.id.recode_pwmilk_memo);
        back = findViewById(R.id.recode_pwmilk_closeBtn);
        Button revise = findViewById(R.id.recode_pwmilk_reviseBtn);
        Button delete = findViewById(R.id.recode_pwmilk_deleteBtn);
        tSum = findViewById(R.id.recode_pwmilk_time);
        startDate = findViewById(R.id.recode_pwmilk_startTime);
        endDate = findViewById(R.id.recode_pwmilk_finishTime);


        final Intent intent = getIntent();
        String stt = intent.getStringExtra("str");
        infoId = intent.getIntExtra("id", INFO_NULL);

        usingDB();


        Log.d("recodeTest", "get recodeId = " + infoId);
        int idx = stt.indexOf(":");
        String stt1 = stt.substring(0, idx);
        String stt2 = stt.substring(idx + 1);
        int sa = Integer.parseInt(stt1);
        int sb = Integer.parseInt(stt2);
        strt = (sa * 60) + sb;


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem();
            }
        });

        revise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviseItem();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar myCalender = Calendar.getInstance();
                int hour = myCalender.get(Calendar.HOUR_OF_DAY);
                int minute = myCalender.get(Calendar.MINUTE);
                TimePickerDialog dialog;
                dialog = new TimePickerDialog(InfoPwmilk.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        startDate.setText(saveChaingeTime(hourOfDay, minute));
                        saveTime = saveChaingeTime(hourOfDay, minute);

                        strt = (hourOfDay * 60) + minute;
                    }
                }, hour, minute, false);
                dialog.setTitle("시작 시간");
                dialog.show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar myCalender = Calendar.getInstance();
                int hour = myCalender.get(Calendar.HOUR_OF_DAY);
                int minute = myCalender.get(Calendar.MINUTE);
                TimePickerDialog dialog;
                dialog = new TimePickerDialog(InfoPwmilk.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        endDate.setText(saveChaingeTime(hourOfDay, minute));
                        lastTime = saveChaingeTime(hourOfDay, minute);
                        endt = (hourOfDay * 60) + minute;
                        tthou = Integer.toString((endt - strt) / 60);
                        ttmin = Integer.toString((endt - strt) % 60);
                        if ((endt - strt) >= 60) {
                            tSum.setText(tthou + "시간" + ttmin + "분");
                        } else {
                            tSum.setText(ttmin + "분");
                        }
                    }
                }, hour, minute, false);
                dialog.setTitle("종료 시간");
                dialog.show();
            }
        });

    }

    TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            if (view.isShown()) {
                myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalender.set(Calendar.MINUTE, minute);

            }
        }
    };


    private void reviseItem() {
        memo = edmemo.getText().toString();
        Log.d("recodePlay", "memo: " + memo + "    ," + saveTime + "  <=> " + lastTime);
        if (lastTime.equals(saveTime))
            lastTime = (lastTime + " ");
        else
            lastTime = lastTime + " 까지 놀았습니다.";

        Log.d("recodePlay", "memo: " + memo + "    ," + saveTime + "  <=> " + lastTime);
        String Revisejob = "UPDATE recode SET time='" + saveTime + "',subt='" + lastTime + "',contents1='" + memo + "' " +
                "WHERE id='" + infoId + "' AND name='" + mBabyname + "'";
        db.execSQL(Revisejob);
    }

    private void deleteItem() {
        String deletejob = "DELETE FROM recode where id=" + infoId + " AND name='" + mBabyname + "'";
        db.execSQL(deletejob);
        finish();
    }

    private void usingDB() {
        helper = new DBlink(this, dbName, null, dbVersion);
        db = helper.getWritableDatabase();

        String sql = "select * from thisusing where _id=1"; // 검색용
        Cursor cursor = db.rawQuery(sql, null);

        // 기본 데이터
        while (cursor.moveToNext()) {
            mId = cursor.getString(1);
            mBabyname = cursor.getString(2);
        }

        sql = "select * from recode where id=" + infoId + " "; // 검색용
        cursor = db.rawQuery(sql, null);

        // 기본 데이터
        while (cursor.moveToNext()) {
            saveTime = cursor.getString(3);
            lastTime = cursor.getString(5);
            memo = cursor.getString(6);



            if (lastTime != null)
                lastTime = lastTime.substring(0, 5);


            startDate.setText(saveTime);
            endDate.setText(lastTime);
            if (lastTime == null){
                lastTime = saveTime;
                endDate.setText(saveTime);
            }

            edmemo.setText(memo);


            SimpleDateFormat f = new SimpleDateFormat("HH:mm", Locale.KOREA);
            Date d1 = null,d2=null;
            try {
                d1 = f.parse(saveTime);
                d2 = f.parse(lastTime);
            } catch (ParseException e) { }


            tthou = Integer.toString(((int)d2.getTime() - (int)d1.getTime()) / 3600000);
            ttmin = Integer.toString((((int)d2.getTime() - (int)d1.getTime()) % 3600000)/60000);
            Log.d("recodePlayTime", "시간: "+((int)d2.getTime() - (int)d1.getTime()));

            if (((int)d2.getTime() - (int)d1.getTime()) >= 3600000) {
                tSum.setText(tthou + "시간" + ttmin + "분");
            } else {
                tSum.setText(ttmin + "분");
            }

        }


    }

    private String saveChaingeTime(int hour, int min) {

        getHour = Integer.toString(hour);
        if (hour / 10 == 0)
            getHour = ("0" + Integer.toString(hour));
        getMinu = Integer.toString(min);
        if (min / 10 == 0)
            getMinu = ("0" + Integer.toString(min));


        return getHour + ":" + getMinu;
    }
}
