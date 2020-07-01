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

import java.util.ArrayList;
import java.util.Calendar;

public class InfoTemp extends AppCompatActivity {

    String dbName = "user.db", mId, mBabyname;
    int dbVersion = 3, infoId, INFO_NULL = 999999999;
    private DBlink helper;
    private SQLiteDatabase db;

    String mMilkMl=null, saveTime, memo,getHour, getMinu;
    String test = null;
    Button tagAdd;
    ImageView back, end;
    private SeekBar mSeekBar;



    Calendar myCalender = Calendar.getInstance();
    int hour = myCalender.get(Calendar.HOUR_OF_DAY);
    int minute = myCalender.get(Calendar.MINUTE);

    private LinearLayout horizontalLayout;
    EditText edmemo;
    TextView tSum, eating, startDate, endDate;
    int strt, endt;

    private double mSeekBarVal = 36.5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recode_temp);

        mSeekBar = findViewById(R.id.recode_temp_bar);
        eating = findViewById(R.id.recode_temp_temperature);
        edmemo = findViewById(R.id.recode_temp_memo);
        back = findViewById(R.id.recode_temp_closeBtn);
        Button end = findViewById(R.id.recode_temp_reviseBtn);
        Button delete = findViewById(R.id.recode_temp_deleteBtn);
        startDate = findViewById(R.id.recode_temp_time);


        final Intent intent = getIntent();
        String stt = intent.getStringExtra("str");
        infoId = intent.getIntExtra("id", INFO_NULL);
        startDate.setText(stt);
        saveTime=stt;

        usingDB();

        memo = edmemo.getText().toString();


        startDate.setText(stt);


        mSeekBar.setMax(38);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    progress = 350 + progress;


                    float decimalProgress = (float) progress/10;

                double strNumber = decimalProgress;


                double mSeekBarVal =  Double.parseDouble(String.format("%.1f", strNumber));
                    eating.setText(mSeekBarVal+ "℃");
                Log.d("tempCount", " 계산전 : "+(progress-350)+"   계산후 : "+progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem();
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviseItem();
            }
        });

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar myCalender = Calendar.getInstance();
                int hour = myCalender.get(Calendar.HOUR_OF_DAY);
                int minute = myCalender.get(Calendar.MINUTE);
                TimePickerDialog dialog;
                dialog = new TimePickerDialog(InfoTemp.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        saveTime = saveChaingeTime(hourOfDay, minute);
                        startDate.setText(saveTime);
                        strt = (hourOfDay * 60) + minute;
                    }
                }, hour, minute, false);
                dialog.setTitle("시작 시간");
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

        String Revisejob = "UPDATE recode SET time='" + saveTime + "',subt='" + eating.getText().toString() + "',contents1='" + memo + "' " +
                "WHERE id='" + infoId + "' AND name='" + mBabyname + "'";
        db.execSQL(Revisejob);
        finish();
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
            Log.d("Home", "db받기 id = " + mId + "  현재 아기 = " + mBabyname);
        }

        sql = "select * from recode where id=" + infoId + " "; // 검색용
        cursor = db.rawQuery(sql, null);
        // 기본 데이터
        while (cursor.moveToNext()) {
            saveTime = cursor.getString(3);
            mMilkMl = cursor.getString(5);
            memo = cursor.getString(6);

            Log.d("tempCount", "mMilkMl" + mMilkMl);
            if(mMilkMl!=null) {
                eating.setText(mMilkMl );
                mMilkMl =mMilkMl.replace("℃","");
                mMilkMl=mMilkMl.replace(".","");
                mSeekBar.setProgress(Integer.valueOf(mMilkMl)-350);
            }
            if(memo!=null)
                edmemo.setText(memo);
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
