package com.example.baily.main.diary;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.baily.DBlink;
import com.example.baily.R;

public class ShowEventInfo extends Activity {
    private String strDate;
    private int eventId;
    static int RESULT_REMOVE_EVENT = 101;

    private DBlink helper;
    private SQLiteDatabase db;
    String dbName = "user.db";
    int dbVersion = 3;

    FragDiaryDate fragDiaryDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event_info);

        Bundle extras = getIntent().getExtras();
        Log.d("Eventsetting", "show Start");
        Log.d("Eventsetting", "extras ="+extras.getString("eventName"));
        usingDB();
        final String name = extras.getString("eventName");
        final String date = extras.getString("eventDate");
        final String memo = extras.getString("eventMemo");
        eventId = extras.getInt("eventId");

        fragDiaryDate = new FragDiaryDate();

        String[] arrDate = date.split("-");
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
                new DatePickerDialog(ShowEventInfo.this, dateSetListener, targetYear, targetMonth - 1, targetDay).show();
            }
        });

        Button confirmBtn = findViewById(R.id.btn_eventConfirm);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText eventName = findViewById(R.id.edit_eventName);
                if (eventName.getText().toString().equals("")) {
                    Toast noname = Toast.makeText(getApplicationContext(), "Write Event Name!", Toast.LENGTH_SHORT);
                    noname.show();
                    return;
                }
                String title,thisdate,olddate,memo;
                int id;
                // 순서대로 1.제목, 2.오늘날, 3.바꿀 오늘날, 4. 메모, 5.id
                title= eventName.getText().toString();
                thisdate= eventDate.getText().toString();
                olddate= date;
                EditText eventMemo = findViewById(R.id.edit_eventMemo);
                memo=eventMemo.getText().toString();
                id= eventId;

                String Revisejob = "UPDATE events  SET title='" + title + "'," +
                        "date ='" + thisdate + "'," +
                        "memo ='" + memo + "'" +
                        " WHERE id ='" + id + "'";
                db.execSQL(Revisejob);

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
                                delIntent.putExtra("eventId", eventId);
                                delIntent.putExtra("eventDate", date);
                                setResult(RESULT_REMOVE_EVENT, delIntent);
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
                String deletejob = "DELETE FROM events WHERE id ='" + eventId + "'";
                db.execSQL(deletejob);
            }
        });


    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            strDate = String.format("%d-%d-%d", year, month + 1, dayOfMonth);
            EditText eventDate = (EditText) findViewById(R.id.edit_eventDate);
            eventDate.setText(strDate);
        }
    };

    private void usingDB() {
        helper = new DBlink(ShowEventInfo.this, dbName, null, dbVersion);
        db = helper.getWritableDatabase();
    }
}
