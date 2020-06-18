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

public class infoSleep extends AppCompatActivity {
    String pwmStart, pwmEnd, pwmMemo,tthou,ttmin,memo ;
    String test = null;
    Button tagAdd ;
    ImageView back, end;
    private SeekBar mSeekBar;
    private int mSeekBarVal = 0;

    Calendar myCalender = Calendar.getInstance();
    int hour = myCalender.get(Calendar.HOUR_OF_DAY);
    int minute = myCalender.get(Calendar.MINUTE);


    EditText  edmemo;
    TextView tSum, eatpwm , startDate, endDate;

    int strt,endt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recode_sleep);



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
    String    stt = intent.getStringExtra("str");
        int idx = stt.indexOf(":");
        String stt1 = stt.substring(0,idx);
        String stt2 = stt.substring(idx+1);
        int sa = Integer.parseInt(stt1);
        int sb = Integer.parseInt(stt2);
        strt = (sa*60)+sb;



        startDate = findViewById(R.id.pwm_start);
        startDate.setText(stt);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar myCalender = Calendar.getInstance();
                int hour = myCalender.get(Calendar.HOUR_OF_DAY);
                int minute = myCalender.get(Calendar.MINUTE);
                TimePickerDialog dialog;
                dialog = new TimePickerDialog(infoSleep.this,new TimePickerDialog.OnTimeSetListener(){
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
        endDate = findViewById(R.id.pwm_end);
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar myCalender = Calendar.getInstance();
                int hour = myCalender.get(Calendar.HOUR_OF_DAY);
                int minute = myCalender.get(Calendar.MINUTE);
                TimePickerDialog dialog;
                dialog = new TimePickerDialog(infoSleep.this,new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        endDate.setText(hourOfDay + ":" + minute );
                        endt = (hourOfDay*60)+minute;
                        tthou=Integer.toString((endt-strt)/60);
                        ttmin=Integer.toString((endt-strt)%60);
                        if((endt-strt)>=60){
                            tSum.setText(tthou+ "시간" + ttmin +"분");
                        }else {
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


}
