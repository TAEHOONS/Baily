package com.example.baily.main.diary;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.baily.R;

public class ShowEventInfo extends Activity {
    private String strDate;
    private int eventId;
    static int RESULT_REMOVE_EVENT = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event_info);

        Bundle extras = getIntent().getExtras();
        final String name = extras.getString("eventName");
        final String date = extras.getString("eventDate");
        final String memo = extras.getString("eventMemo");
        eventId = extras.getInt("eventId");

        String[] arrDate =  date.split("-");
        final int targetYear = Integer.parseInt(arrDate[0]);
        final int targetMonth = Integer.parseInt(arrDate[1]);
        final int targetDay = Integer.parseInt(arrDate[2]);



        EditText eventName = findViewById(R.id.edit_eventName);
        eventName.setText(name);
        final EditText eventDate = findViewById(R.id.edit_eventDate);
        eventDate.setText(date);
        EditText eventMemo = findViewById(R.id.edit_eventMemo);
        eventMemo.setText(memo);

        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ShowEventInfo.this,dateSetListener,targetYear,targetMonth-1, targetDay).show();
            }
        });

        Button confirmBtn = findViewById(R.id.btn_eventConfirm);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText eventName = findViewById(R.id.edit_eventName);
                if(eventName.getText().toString().equals("")){
                    Toast noname = Toast.makeText(getApplicationContext(),"Write Event Name!", Toast.LENGTH_SHORT);
                    noname.show();
                    return;
                }
                Intent eventIntent = new Intent(ShowEventInfo.this, FragDiary.class);
                // add eventName
                eventIntent.putExtra("eventName",eventName.getText().toString());
                Log.d("name",eventName.getText().toString());
                //add eventDate
                eventIntent.putExtra("eventDate",eventDate.getText().toString());
                eventIntent.putExtra("oldDate",date);
                //add eventMemo
                EditText eventMemo = findViewById(R.id.edit_eventMemo);
                eventIntent.putExtra("eventMemo",eventMemo.getText().toString());

                eventIntent.putExtra("eventId",eventId);

                setResult(RESULT_OK,eventIntent);
                finish();
            }
        });

        Button delBtn = findViewById(R.id.btn_eventDelete);
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder delConfirm = new AlertDialog.Builder(ShowEventInfo.this)
                        .setTitle("check delete Event")
                        .setMessage("Are you sure you want to delete?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent delIntent = new Intent(ShowEventInfo.this, FragDiary.class);
                                delIntent.putExtra("eventId",eventId);
                                delIntent.putExtra("eventDate",date);
                                setResult(RESULT_REMOVE_EVENT,delIntent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                delConfirm.show();
            }
        });





    }
    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            strDate = String.format("%d-%d-%d",year,month+1,dayOfMonth);
            EditText eventDate = (EditText)findViewById(R.id.edit_eventDate);
            eventDate.setText(strDate);
        }
    };
}
