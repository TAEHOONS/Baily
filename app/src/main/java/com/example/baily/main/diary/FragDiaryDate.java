package com.example.baily.main.diary;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baily.DBlink;
import com.example.baily.R;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragDiaryDate extends Fragment {
    public static int ADD_EVENT = 0;
    public static int SHOW_EVENT_INFO = 1;
    public static int RESULT_REMOVE_EVENT = 101;
    public static FragDiaryDate thisContext;
    private TextView ymdate;
    private Date selectedDate;
    private SimpleDateFormat simpleMonthFormat = new SimpleDateFormat("yyyy 년 MM 월", Locale.KOREA);
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
    private CompactCalendarView compactCalendarView;
    private LinearLayout horizontalLayout;
    private RecyclerView horizontalView;
    private HorizontalAdapter horizontalAdapter;

    private DBlink helper;
    private SQLiteDatabase db;
    String dbName = "user.db", mId, mBabyname;
    int dbVersion = 3, requestCode = 0;
    ViewGroup container;
    private Cursor cursor;
    Context contx;
    View v;

    public FragDiaryDate() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("Eventsetting", "onCreateView Start");
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.frag_diary_date, container, false);
        setHasOptionsMenu(true);
        this.container=container;

        contx = container.getContext();
        // Constructing
        selectedDate = new Date();

        Toolbar toolbar = v.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowCustomEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeButtonEnabled(false);
        activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home_36pt);
        activity.getSupportActionBar().setTitle("");

        ymdate = v.findViewById(R.id.ymdate);

        thisContext = this;

        compactCalendarView = (CompactCalendarView) v.findViewById(R.id.customcalendar_view);
        compactCalendarView.setLocale(TimeZone.getDefault(), Locale.KOREA);
        compactCalendarView.setFirstDayOfWeek(1);
        compactCalendarView.setUseThreeLetterAbbreviation(true);
        compactCalendarView.shouldDrawIndicatorsBelowSelectedDays(true);
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {

            @Override
            public void onDayClick(Date dateClicked) {
                ymdate.setText(simpleMonthFormat.format(dateClicked));
                selectedDate = dateClicked;
                Log.d("dateclick", dateClicked + "");
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                ymdate.setText(simpleMonthFormat.format(firstDayOfNewMonth));
                showRecyclerEvents(firstDayOfNewMonth);
            }
        });

        //DATABASE data.add
        usingDB(container);


        cursor = db.rawQuery("SELECT *FROM events where name='" + mBabyname + "'AND parents='" + mId + "'", null);
        int dblength = 0;
        while (cursor.moveToNext()) {

            int id = cursor.getInt(0);
            String name = cursor.getString(2);
            String date = cursor.getString(3);
            String memo = cursor.getString(4);

            try {
                Date addDate = simpleDateFormat.parse(date);

                Bundle bundle = new Bundle();

                bundle.putString("eventName", name);
                bundle.putString("eventDate", date);
                bundle.putString("eventMemo", memo);

                bundle.putInt("eventId", id);


                Event event = new Event(Color.RED, addDate.getTime(), bundle);
                compactCalendarView.addEvent(event);


            } catch (ParseException e) {
                e.printStackTrace();
            }

            dblength++;
        }
        Log.d("dblength", String.valueOf(dblength));

        showRecyclerEvents(compactCalendarView.getFirstDayOfCurrentMonth());

        // createEvent();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Eventsetting", "onResume Start");
        ymdate.setText(simpleMonthFormat.format(compactCalendarView.getFirstDayOfCurrentMonth()));


        usingDB(container);
        compactCalendarView = (CompactCalendarView) v.findViewById(R.id.customcalendar_view);
        compactCalendarView.setLocale(TimeZone.getDefault(), Locale.KOREA);
        compactCalendarView.setFirstDayOfWeek(1);
        compactCalendarView.setUseThreeLetterAbbreviation(true);
        compactCalendarView.shouldDrawIndicatorsBelowSelectedDays(true);
        cursor = db.rawQuery("SELECT *FROM events where name='" + mBabyname + "'AND parents='" + mId + "'", null);
        int dblength = 0;
        while (cursor.moveToNext()) {

            int id = cursor.getInt(0);
            String name = cursor.getString(2);
            String date = cursor.getString(3);
            String memo = cursor.getString(4);

            try {
                Date addDate = simpleDateFormat.parse(date);

                Bundle bundle = new Bundle();
                bundle.putString("eventName", name);
                bundle.putString("eventDate", date);
                bundle.putString("eventMemo", memo);

                bundle.putInt("eventId", id);


                Event event = new Event(Color.RED, addDate.getTime(), bundle);
                compactCalendarView.addEvent(event);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            dblength++;
        }
        Log.d("dblength", String.valueOf(dblength));
        showRecyclerEvents(compactCalendarView.getFirstDayOfCurrentMonth());


    }

    // 이거 불러오면 밑에거 실행함
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        createEvent();

//        if (requestCode == SHOW_EVENT_INFO) {
//            if (resultCode == RESULT_OK) {
//                updateEvent(data.getExtras());
//            } else if (resultCode == RESULT_REMOVE_EVENT) {
//                deleteEvent(data.getExtras());
//            }
//        }

    }

    // 옵션 메뉴 만들기
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {

        menuInflater.inflate(R.menu.diarymenu, menu);

    }

    // 옵션 메뉴 선택시
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Date nowDate = new Date();
            compactCalendarView.setCurrentDate(nowDate);
            ymdate.setText(simpleMonthFormat.format(nowDate));
            showRecyclerEvents(nowDate);
        }
        if (id == R.id.action_addEvent) {
            Intent intent = new Intent(getActivity(), AddEvent.class);

            String dateString = simpleDateFormat.format(selectedDate);
            intent.putExtra("selectedDate", dateString);
            getActivity().startActivityForResult(intent, ADD_EVENT);
        } else if (id == R.id.action_search) {
            Intent intent = new Intent(getActivity(), SearchEvent.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    // 달력-캘린더 날짜 색칠하기
    public void createEvent() {
        Bundle bundle = getArguments();

        String eventName = bundle.getString("eventName");
        String eventDate = bundle.getString("eventDate");
        String eventMemo = bundle.getString("eventMemo");
        Log.d("DiaryTable", "Frag가 받음" + eventName + "," + eventDate + "," + eventMemo);
        int eventId = -1;

        try {
            Date newdate = simpleDateFormat.parse("2020-06-29");
            // insert to DB

            cursor = db.rawQuery("SELECT * from events where id=LAST_INSERT_ROWID();", null);
            if (cursor.moveToFirst())
                eventId = cursor.getInt(0);

            bundle.putInt("eventId", 1);
            Log.d("DiaryTable", "eventID is" + String.valueOf(eventId));

            Event newEvent = new Event(Color.RED, newdate.getTime(), bundle);
            compactCalendarView.addEvent(newEvent);

            showRecyclerEvents(compactCalendarView.getFirstDayOfCurrentMonth());
        } catch (ParseException e) {
            e.printStackTrace();

        }


    }


    public void showRecyclerEvents(Date date) {
        Log.d("Eventsetting", "showRecyclerEvents Start");
        List<Event> events = compactCalendarView.getEventsForMonth(date);


        ArrayList<EventData> dataArrayList = new ArrayList<>();
        for (Event event : events) {
            Bundle extras = (Bundle) event.getData();
            Log.d("Eventsetting", "Bundle extras = "+extras);
            String sname = ((Bundle) event.getData()).getString("eventName");
            String sdate = ((Bundle) event.getData()).getString("eventDate");
            String smemo = ((Bundle) event.getData()).getString("eventMemo");
            int sid = extras.getInt("eventId");

            EventData eventData = new EventData(mBabyname, sname, sdate, smemo, mId, sid);
            dataArrayList.add(eventData);
        }
        horizontalLayout = (LinearLayout) v.findViewById(R.id.horizontal_event_layout);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(horizontalLayout.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        horizontalAdapter = new HorizontalAdapter(dataArrayList, this);

        horizontalView = (RecyclerView) v.findViewById(R.id.horizontal_event_view);
        horizontalView.setLayoutManager(linearLayoutManager);
        horizontalView.setAdapter(horizontalAdapter);

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
            requestCode=cursor.getInt(3);
            Log.d("DiaryDBset", "db받기 id = " + mId + "  현재 아기 = " + mBabyname+ "  현재 코드 = " + requestCode);
        }

    }



}






