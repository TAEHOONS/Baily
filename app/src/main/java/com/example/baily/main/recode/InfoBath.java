package com.example.baily.main.recode;

import android.app.TimePickerDialog;
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

import androidx.appcompat.app.AppCompatActivity;

import com.example.baily.DBlink;
import com.example.baily.R;

import java.util.Calendar;

public class InfoBath extends AppCompatActivity {

    String dbName = "user.db", mId, mBabyname;
    int dbVersion = 3, infoId, INFO_NULL = 999999999;
    private DBlink helper;
    private SQLiteDatabase db;

    String tthou, ttmin, memo, time;
    String test = null;
    Button tagAdd;
    ImageView back, end;
    private SeekBar mSeekBar;
    private int mSeekBarVal = 0;
    String stt = null;
    Calendar myCalender = Calendar.getInstance();
    int hour = myCalender.get(Calendar.HOUR_OF_DAY);
    int minute = myCalender.get(Calendar.MINUTE);

    private LinearLayout horizontalLayout;
    EditText edmemo;
    TextView tSum, eatpwm, startDate, endDate;
    int strt, endt;

    static int RESULT_REMOVE_EVENT = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recode_bath);

        usingDB();

        edmemo = findViewById(R.id.pwm_memo);
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
                Intent bath = new Intent();
                memo = edmemo.getText().toString();
                time = tSum.getText().toString();
                bath.putExtra("str", startDate.getText().toString());
                bath.putExtra("meme", edmemo.getText().toString());
                setResult(RESULT_OK, bath);
                finish();
            }
        });


        final Intent intent = getIntent();
        stt = intent.getStringExtra("str");
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
                dialog = new TimePickerDialog(InfoBath.this, new TimePickerDialog.OnTimeSetListener() {
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
                dialog = new TimePickerDialog(InfoBath.this, new TimePickerDialog.OnTimeSetListener() {
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
