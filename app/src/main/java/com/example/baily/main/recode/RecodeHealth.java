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

public class RecodeHealth extends AppCompatActivity {
    String pwmStart, pwmEnd, pwmMemo,tthou,ttmin,memo ;
    String test = null;
    Button tagAdd ;
    ImageView back, end;
    private SeekBar mSeekBar;
    private int mSeekBarVal = 0;

    Calendar myCalender = Calendar.getInstance();
    int hour = myCalender.get(Calendar.HOUR_OF_DAY);
    int minute = myCalender.get(Calendar.MINUTE);

    ArrayList<RecodeInfoItem> mDataList;
    RecodeInfoAdapter mAdapter;
    private LinearLayout horizontalLayout;
    EditText startDate, endDate, edmemo;
    TextView tSum, eatpwm;
    int strt,endt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recode_health);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.pwm_tag_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        horizontalLayout = (LinearLayout) findViewById(R.id.pwm_hori);

        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(RecodeHealth.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);

        recyclerView.setAdapter(mAdapter);

        edmemo=findViewById(R.id.pwm_memo);
        memo= edmemo.getText().toString();
        mDataList = new ArrayList<>();

        Intent intent = getIntent();
        final TextView tat = findViewById(R.id.pwm_tv);
        test=intent.getStringExtra("tag");
        if(test != null){
            tat.setText(test);
        }

        mAdapter = new RecodeInfoAdapter( mDataList);
        recyclerView.setAdapter(mAdapter);

        end = findViewById(R.id.rt_img_closeBtn);
        back = findViewById(R.id.rt_img_checkBtn);
        tagAdd = findViewById(R.id.rt_TV_plus);

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

        tagAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog();
            }
        });

        RecodeInfoAdapter adapter = new RecodeInfoAdapter(mDataList);
        adapter.setOnItemClickListener(new RecodeInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecodeInfoAdapter.CustomViewHolder holder, View view, int position) {
                TextView tag =  view.findViewById(R.id.rec_tag);
                String tv = tag.getText().toString();
                tat.setText(tv);
            }
        });



        startDate = findViewById(R.id.pwm_start);
        startDate.setText(pwmStart);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(RecodeHealth.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        // EditText에 출력할 형식 지정
                        startDate.setText(" " + selectedHour + " : " + selectedMinute + " " );
                        strt = (selectedHour*60)+selectedMinute;
                    }
                }, hour, minute, true); // true의 경우 24시간 형식의 TimePicker 출현
                mTimePicker.setTitle("시작 시간");
                mTimePicker.show();
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
    public void Dialog(){
        final EditText editTag = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("퀵 버튼 추가");
        builder.setMessage("자주 쓰는 단어를 퀵 버튼에 추가해 주세요.");
        builder.setView(editTag);
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 4. 사용자가 입력한 내용을 가져와서
                        String strTag = editTag.getText().toString();
                        // 5. ArrayList에 추가하고
                        RecodeInfoItem dict = new RecodeInfoItem(strTag);
                        mDataList.add(0, dict); //첫번째 줄에 삽입됨
                        //mArrayList.add(dict); //마지막 줄에 삽입됨
                        // 6. 어댑터에서 RecyclerView에 반영하도록 합니다.
                        mAdapter.notifyItemInserted(0);
                        //RecodeInfoAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }).setNegativeButton("취소", null);

        builder.show();
    }

}
