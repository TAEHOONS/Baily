package com.example.baily.main.recode;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baily.DBlink;
import com.example.baily.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class FragRecode extends Fragment {



    private DBlink helper;
    private SQLiteDatabase db;
    String dbName = "user.db", mId, mBabyname;
    String Timenurs="0" ,Timebowel="0" , Timesleep="0";
    int dbVersion = 3, requestCode = 0;

    private View v;
    int count = 1;
    int recode = 0;
    RecodeAdapter recodeAdapter;
    RecyclerView recyclerView;
    private String sel;
    ViewGroup container;
    Boolean nurSQL=false,bowSQL=false,sleepSQL=false;

    AppCompatImageButton  nurs, bbfood, sleep, pwmilk, bowel, dosage, tem, bath, health, play;
    TextView day, nursT, bowerT, sleepT;
    SimpleDateFormat dateSet = new SimpleDateFormat("yyyyMMdd HH:mm");
    String date = dateSet.format(new Date());

    String dateInit = null;
    private List<String> list;
    private ArrayList<String> arraylist;

    public static int INFO_NURSING = 1;
    public static int INFO_BBFOOD = 2;
    public static int INFO_BOWEL = 3;
    public static int INFO_BATH = 4;
    public static int INFO_DRUG = 5;
    public static int INFO_HOSP = 6;
    public static int INFO_PLAY = 7;
    public static int INFO_PWMILK = 8;
    public static int INFO_SLEEP = 9;
    public static int INFO_TEMP = 10;
    static int RESULT_REMOVE_EVENT = 101;
    ArrayList<RecodeData> dataArrayList;
    Context context;

    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };


    @Override
    public void onPrimaryNavigationFragmentChanged(boolean isPrimaryNavigationFragment) {
        super.onPrimaryNavigationFragmentChanged(isPrimaryNavigationFragment);
        if (isPrimaryNavigationFragment) {
            Log.d("moveScreen", "fragment is shown");
            // Your fragment is shown
        } else {
            Log.d("moveScreen", " fragment is hidden");
            // Your fragment is hidden
        }
    }

    private String getNowTime() {
        // 현재시간을 msec 으로 구한다.
        long now = System.currentTimeMillis();
        // 현재시간을 date 변수에 저장한다.
        Date date = new Date(now);
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        SimpleDateFormat sdfNow = new SimpleDateFormat("HH:mm");

        return sdfNow.format(date);
    }

    private static class TIME_MAXIMUM {
        public static final int SEC = 60;
        public static final int MIN = 60;
        public static final int HOUR = 24;
        public static final int DAY = 30;
        public static final int MONTH = 12;
    }

    public static FragRecode newInstance() {
        FragRecode fragRecode = new FragRecode();
        return fragRecode;
    }
    long nurs_start=0,sleep_start=0,bower_start=0;
    long nurs_end,sleep_end,bower_end;
    int suma,sumb,sumc;
    private int index;
    String sfName = "myFile";
    ExecutorService threadPool = Executors.newFixedThreadPool(10);
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frag_recode, container, false);




        this.container = container;

        nurs = v.findViewById(R.id.nursing_btn);
        bbfood = v.findViewById(R.id.babyfood_btn);
        sleep = v.findViewById(R.id.sleep_btn);
        pwmilk = v.findViewById(R.id.powderedmilk_btn);
        bowel = v.findViewById(R.id.bowel_btn);
        dosage = v.findViewById(R.id.dosage_btn);
        tem = v.findViewById(R.id.temperature_btn);
        bath = v.findViewById(R.id.bath_btn);
        health = v.findViewById(R.id. health_btn);
        play = v.findViewById(R.id.play_btn);
        ImageView imageView = v.findViewById(R.id.whencal);
        day = v.findViewById(R.id.whenDate);
        day.setText(" " + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "월 "
                + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "일");
        Log.d("recodeDate", "Date : " + Calendar.getInstance().get(Calendar.YEAR) + " , " + (Calendar.getInstance().get(Calendar.MONTH) + 1) + " , " + Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), myDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        usingDB(container);


        if (getArguments() != null) {
            sel = getArguments().getString("select");
            FragmentTransaction transaction = getActivity()
                    .getSupportFragmentManager().beginTransaction();
            sleepT.setText(sel);
            transaction.commit();

        }

        dataArrayList = new ArrayList<>();


        recyclerView = v.findViewById(R.id.rec_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false); //레이아웃매니저 생성
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);


        recyclerView.setLayoutManager(layoutManager);
        recodeAdapter = new RecodeAdapter(dataArrayList, this);
        recyclerView.setAdapter(recodeAdapter);

        nurs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertDB("모유");
                //excute를 통해 백그라운드 task를 실행시킨다
                //여기선 100을 매개변수로 보내는데 여기 예제에서는 이 매개변수를 doInBackGround에서 사용을 안했다.
                //여기선 100을 매개변수로 보내는데 여기 예제에서는 이 매개변수를 doInBackGround에서 사용을 안했다.
            }
        });


        bbfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertDB("이유식");

            }
        });
        sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                insertDB("잠");
                sleep_start  = System.currentTimeMillis();

                //excute를 통해 백그라운드 task를 실행시킨다
                //여기선 100을 매개변수로 보내는데 여기 예제에서는 이 매개변수를 doInBackGround에서 사용을 안했다.

            }
        });
        pwmilk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertDB("분유");
            }
        });
        bowel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertDB("기저귀");
                bower_start  = System.currentTimeMillis();

                //excute를 통해 백그라운드 task를 실행시킨다
                //여기선 100을 매개변수로 보내는데 여기 예제에서는 이 매개변수를 doInBackGround에서 사용을 안했다.

            }
        });
        dosage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertDB("약");
            }
        });
        tem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertDB("온도");
            }
        });
        bath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertDB("목욕");
            }
        });
        health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertDB("병원");
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertDB("놀이");
            }
        });



        loadRecode(false, "");


        return v;
    }



    @Override
    public void onResume() {
        super.onResume();

        usingDB(container);
        loadRecode(false, "");
    }


    private void updateLabel() {
        String myFormat = "  MM월 dd일";    // 출력형식   2018/11/28

        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        TextView et_date = v.findViewById(R.id.whenDate);
        et_date.setText(sdf.format(myCalendar.getTime()));
        Log.d("recodeDate", "Date : " + myCalendar.getTime());
        loadRecode(false, "");
    }

    // DB 연결
    private void usingDB(ViewGroup container) {
        helper = new DBlink(container.getContext(), dbName, null, dbVersion);
        db = helper.getWritableDatabase();

        String sql = "select * from thisusing where _id=1"; // 검색용
        Cursor cursor = db.rawQuery(sql, null);

        // 기본 데이터
        while (cursor.moveToNext()) {
            mId = cursor.getString(1);
            mBabyname = cursor.getString(2);
        }




    }

    private void insertDB(String title) {
        String myFormat = "YYYY년MM월dd일";    // 출력형식   2018/11/28
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        ContentValues values = new ContentValues();
        values.put("name", mBabyname);
        values.put("date", sdf.format(myCalendar.getTime()));
        values.put("time", getNowTime());
        values.put("title", title);
        values.put("parents", mId);
        db.insert("recode", null, values);


        loadRecode(false, "");
    }


    private void insertRecode(String time, String title, String subt,int recodeId) {

        recodeAdapter.addItem(new RecodeData(time, title, subt,recodeId));
        recyclerView.setAdapter(recodeAdapter);
    }


    private void loadRecode(Boolean mode, String select) {
        ArrayList<RecodeData> dataArrayList = new ArrayList<>();
        recodeAdapter = new RecodeAdapter(dataArrayList, this);
        recyclerView.setAdapter(null);
        Log.d("recodeDBget", "loadRecode Start");

        String myFormat = "YYYY년MM월dd일";    // 출력형식   2018/11/28
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);
        String sqlday = sdf.format(myCalendar.getTime());
        Log.d("recodeDBget", "sqlday =" + sqlday);
        String sql; // true 지정 검색 모드, false는 기본모드

        Log.d("recodeDBget", "mode =" + mode + " , baby = " + mBabyname);
        if (mode == true)
            sql = "select * from recode where name='" + mBabyname + "'AND date='" + sqlday + "'AND title='" + select + "' order by time"; // 검색용
        else
            sql = "select * from recode where name='" + mBabyname + "'AND date='" + sqlday + "' order by time"; // 검색용
        Cursor c = db.rawQuery(sql, null);
        // 기본 데이터
        while (c.moveToNext()) {
            int recodeId=c.getInt(0);
            String time = c.getString(3);
            String title = c.getString(4);
            String subt = c.getString(5);
            if (subt == null)
                subt = "";
            Log.d("recodeDBget", "db받기 time = " + time + "  ,title = " + title + "  , subt = " + subt);
            insertRecode(time, title, subt,recodeId);
        }
    }


}