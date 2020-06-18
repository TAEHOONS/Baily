package com.example.baily.main.recode;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import com.example.baily.R;

import java.util.ArrayList;
import java.util.Calendar;

public class InfoTemp extends AppCompatActivity {
    String pwmStart, pwmEnd, pwmMemo,tthou,ttmin,memo ;
    String test = null;
    Button tagAdd ;
    ImageView back, end;
    private SeekBar mSeekBar;
    private int mSeekBarVal = (int) 36.5;

    Calendar myCalender = Calendar.getInstance();
    int hour = myCalender.get(Calendar.HOUR_OF_DAY);
    int minute = myCalender.get(Calendar.MINUTE);

    EditText  edmemo;
    TextView tSum, eatpwm , startDate, endDate;

    int strt,endt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recode_temp);

        mSeekBar=findViewById(R.id.nurs_left_bar);
        eatpwm = findViewById(R.id.left_val);
        eatpwm.setText(String.valueOf(mSeekBarVal)+"℃");
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSeekBarVal = progress;
                eatpwm.setText(String.valueOf(mSeekBarVal)+"℃");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        edmemo=findViewById(R.id.pwm_memo);
        memo= edmemo.getText().toString();



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
     String   stt = intent.getStringExtra("str");



        startDate = findViewById(R.id.rt_hospital_time);
        startDate.setText(stt);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar myCalender = Calendar.getInstance();
                int hour = myCalender.get(Calendar.HOUR_OF_DAY);
                int minute = myCalender.get(Calendar.MINUTE);
                TimePickerDialog dialog;
                dialog = new TimePickerDialog(InfoTemp.this,new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        startDate.setText(hourOfDay + ":" + minute);
                        strt = (hourOfDay*60)+minute;
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



}
