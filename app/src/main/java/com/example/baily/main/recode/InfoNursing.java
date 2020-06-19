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

public class InfoNursing extends AppCompatActivity {

    String dbName = "user.db", mId, mBabyname;
    int dbVersion = 3, infoId, INFO_NULL = 999999999;
    private DBlink helper;
    private SQLiteDatabase db;

    String pwmStart, pwmEnd, pwmMemo, tthou, ttmin, memo;
    String test = null;
    Button tagAdd;
    ImageView back, end;
    private SeekBar mSeekBar;
    private int mSeekBarVal = 0;

    Calendar myCalender = Calendar.getInstance();
    int hour = myCalender.get(Calendar.HOUR_OF_DAY);
    int minute = myCalender.get(Calendar.MINUTE);


    EditText edmemo;
    TextView tSum, eatpwm, startDate, endDate,
            eatleft, eatright;
    int strt, endt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recode_nursing);


        edmemo = findViewById(R.id.pwm_memo);
        memo = edmemo.getText().toString();

        usingDB();

        back = findViewById(R.id.rt_img_closeBtn);
        Button end = findViewById(R.id.button2);
        Button delete = findViewById(R.id.button3);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tSum = findViewById(R.id.pwm_sum);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Intent intent = getIntent();
        String stt = intent.getStringExtra("str");
        infoId = intent.getIntExtra("id", INFO_NULL);

        int idx = stt.indexOf(":");
        String stt1 = stt.substring(0, idx);
        String stt2 = stt.substring(idx + 1);
        int sa = Integer.parseInt(stt1);
        int sb = Integer.parseInt(stt2);
        strt = (sa * 60) + sb;


        startDate = findViewById(R.id.pwm_start);
        startDate.setText(stt);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar myCalender = Calendar.getInstance();
                int hour = myCalender.get(Calendar.HOUR_OF_DAY);
                int minute = myCalender.get(Calendar.MINUTE);
                TimePickerDialog dialog;
                dialog = new TimePickerDialog(InfoNursing.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        startDate.setText(hourOfDay + ":" + minute);
                        strt = (hourOfDay * 60) + minute;
                    }
                }, hour, minute, false);
                dialog.setTitle("시작 시간");
                dialog.show();
            }
        });
        endDate = findViewById(R.id.pwm_end);
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar myCalender = Calendar.getInstance();
                int hour = myCalender.get(Calendar.HOUR_OF_DAY);
                int minute = myCalender.get(Calendar.MINUTE);
                TimePickerDialog dialog;
                dialog = new TimePickerDialog(InfoNursing.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        endDate.setText(hourOfDay + ":" + minute);
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
    }

}
