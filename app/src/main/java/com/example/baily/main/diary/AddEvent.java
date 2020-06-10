package com.example.baily.main.diary;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.baily.DBlink;
import com.example.baily.R;

public class AddEvent extends Activity {
    String strDate ;

    int selectedYear;
    int selectedMonth;
    int selectedDay;

    private DBlink helper;
    private SQLiteDatabase db;
    String dbName = "user.db", mId, mBabyname;
    int dbVersion = 3;


    Fragment fragDiaryDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_addevent);
        usingDB();
        Intent intent = getIntent();
        strDate = intent.getExtras().getString("selectedDate"); // yyyy-mm-dd
        String[] arrDate =  strDate.split("-");
        selectedYear = Integer.parseInt(arrDate[0]);
        selectedMonth = Integer.parseInt(arrDate[1]);
        selectedDay = Integer.parseInt(arrDate[2]);

        //fragment 생성
        fragDiaryDate = new FragDiaryDate();

        //번들객체 생성



        EditText eventDate = findViewById(R.id.edit_eventDate);
        eventDate.setText(strDate);
        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddEvent.this,dateSetListener,selectedYear,selectedMonth-1,selectedDay).show();
            }
        });

        Button confirmBtn = findViewById(R.id.btn_eventAddConfirm);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText eventName = findViewById(R.id.edit_eventName);
                if(eventName.getText().toString().equals("")){
                    Toast noname = Toast.makeText(getApplicationContext(),"Write Event Name!", Toast.LENGTH_SHORT);
                    noname.show();
                    return;
                }
                Bundle bundle = new Bundle();
                Intent eventIntent = new Intent(AddEvent.this, FragDiaryDate.class);
                // add eventName
                eventIntent.putExtra("eventName",eventName.getText().toString());
               bundle.putString("eventName",eventName.getText().toString());
                Log.d("name",eventName.getText().toString());
                //add eventDate
                eventIntent.putExtra("eventDate",strDate);
                bundle.putString("eventDate",strDate);
                //add eventMemo
                EditText eventMemo = findViewById(R.id.edit_eventMemo);
                eventIntent.putExtra("eventMemo",eventMemo.getText().toString());
                bundle.putString("eventMemo",eventMemo.getText().toString());
                insertData(eventName.getText().toString(),strDate,eventMemo.getText().toString());

                // add 지금 입력한거 플레그-DiaryDate 보내는중
                fragDiaryDate.setArguments(bundle);
                Log.d("DiaryTable", "Fragment 에 보냄");
                setResult(RESULT_OK,eventIntent);
                finish();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            strDate = String.format("%d-%d-%d",year,month+1,dayOfMonth);
            EditText eventDate = (EditText)findViewById(R.id.edit_eventDate);
            eventDate.setText(strDate);
        }
    };

    private void insertData(String title,String Date,String Memo){
        // insert to DB
        ContentValues values = new ContentValues();

        values.put("name", mBabyname);
        values.put("title", title);
        values.put("date", Date);
        values.put("memo", Memo);
        values.put("parents", mId);

        // 테이블 이름 + 이제까지 입력한것을 저장한 변수(values)
        Log.d("DiaryTable", "insert");
        Log.d("DiaryTable", "mId = "+mId+"   , eventName = "+title+"   ,eventDate = "+
                Date+"   ,eventMemo = "+Memo+"   , mBabyname = "+mBabyname);
        db.insert("events", null, values);
    }


    // db 적용
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
