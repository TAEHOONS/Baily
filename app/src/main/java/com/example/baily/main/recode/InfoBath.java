package com.example.baily.main.recode;

import android.app.TimePickerDialog;
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

import androidx.appcompat.app.AppCompatActivity;

import com.example.baily.R;

import java.util.Calendar;

public class InfoBath extends AppCompatActivity {
    String tthou,ttmin,memo , time  ;
    String test = null;
    Button tagAdd ;
    ImageView back, end;
    private SeekBar mSeekBar;
    private int mSeekBarVal = 0;
 String stt = null;
    Calendar myCalender = Calendar.getInstance();
    int hour = myCalender.get(Calendar.HOUR_OF_DAY);
    int minute = myCalender.get(Calendar.MINUTE);

    private LinearLayout horizontalLayout;
    EditText  edmemo;
    TextView tSum, eatpwm , startDate, endDate;
    int strt,endt;

    static int RESULT_REMOVE_EVENT = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recode_bath);



        edmemo=findViewById(R.id.pwm_memo);






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
                memo= edmemo.getText().toString();
                time = tSum.getText().toString();
                bath.putExtra("str",startDate.getText().toString());
                bath.putExtra("meme",edmemo.getText().toString());
                setResult(RESULT_OK,bath);
                finish();
            }
        });


        final Intent intent = getIntent();
        stt = intent.getStringExtra("str");
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
                dialog = new TimePickerDialog(InfoBath.this,new TimePickerDialog.OnTimeSetListener(){
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
                dialog = new TimePickerDialog(InfoBath.this,new TimePickerDialog.OnTimeSetListener(){
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
