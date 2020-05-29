package com.example.baily.main.diary;

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
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.KOREA);
    private CompactCalendarView compactCalendarView;
    private LinearLayout horizontalLayout;
    private RecyclerView horizontalView;
    private HorizontalAdapter horizontalAdapter;
    private DBHelper helper;
    private Cursor cursor;
    SQLiteDatabase db;
    Context contx;
View v;
    public FragDiaryDate() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.frag_diary_date, container, false);
        setHasOptionsMenu(true);


        contx = container.getContext();
        // Constructing
        selectedDate = new Date();

        Toolbar toolbar = v.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity. getSupportActionBar().setDisplayShowCustomEnabled(true);
        activity. getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeButtonEnabled(false);
        activity. getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home_36pt);
        activity.getSupportActionBar().setTitle("");

        ymdate = v.findViewById(R.id.ymdate);

        thisContext = this;

        compactCalendarView =  (CompactCalendarView) v.findViewById(R.id.customcalendar_view);
        compactCalendarView.setLocale(TimeZone.getDefault(),Locale.KOREA);
        compactCalendarView.setFirstDayOfWeek(1);
        compactCalendarView.setUseThreeLetterAbbreviation(true);
        compactCalendarView.shouldDrawIndicatorsBelowSelectedDays(true);
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {

            @Override
            public void onDayClick(Date dateClicked) {
                ymdate.setText(simpleMonthFormat.format(dateClicked));
                selectedDate = dateClicked;
                Log.d("dateclick",dateClicked+"");
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                ymdate.setText(simpleMonthFormat.format(firstDayOfNewMonth));
                showRecyclerEvents(firstDayOfNewMonth);
            }
        });

        //DATABASE data.add
        helper = new DBHelper(contx);
        db = helper.getWritableDatabase();




        cursor = db.rawQuery("SELECT *FROM events",null);
        int dblength=0;
        while ( cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String date = cursor.getString(2);
            String memo = cursor.getString(3);



            try{
                Date addDate = simpleDateFormat.parse(date);

                Bundle bundle = new Bundle();
                bundle.putString("eventName",name);
                bundle.putString("eventDate",date);
                bundle.putString("eventMemo",memo);
                bundle.putInt("eventId",id);
                Event event = new Event(Color.RED,addDate.getTime(),bundle);
                compactCalendarView.addEvent(event);

            }catch (ParseException e){
                e.printStackTrace();
            }

            dblength++;
        }
        Log.d("dblength",String.valueOf(dblength));

        showRecyclerEvents(compactCalendarView.getFirstDayOfCurrentMonth());



    return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ymdate.setText(simpleMonthFormat.format(compactCalendarView.getFirstDayOfCurrentMonth()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == ADD_EVENT){
            if(resultCode == RESULT_OK){
                createEvent(data.getExtras());
            }
        }
        if(requestCode == SHOW_EVENT_INFO){
            if(resultCode == RESULT_OK){
                updateEvent(data.getExtras());
            }
            else if(resultCode == RESULT_REMOVE_EVENT){
                deleteEvent(data.getExtras());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {

        menuInflater.inflate(R.menu.diarymenu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            Date nowDate = new Date();
            compactCalendarView.setCurrentDate(nowDate);
            ymdate.setText(simpleMonthFormat.format(nowDate));
            showRecyclerEvents(nowDate);
        }
        if (id == R.id.action_addEvent){
            Intent intent = new Intent(getActivity(), AddEvent.class);

            String dateString = simpleDateFormat.format(selectedDate);
            intent.putExtra("selectedDate",dateString);
            getActivity().startActivityForResult(intent,ADD_EVENT);
        }
        else if(id == R.id.action_search){
            Intent intent = new Intent(getActivity(), SearchEvent.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void createEvent(Bundle extras){
        Bundle bundle = getArguments();

        String eventName = bundle.getString("eventName");
        String eventDate = bundle.getString("eventDate");
        String eventMemo = bundle.getString("eventMemo");
        int eventId= -1;

        try {
            Date newdate = simpleDateFormat.parse(eventDate);
            // insert to DB
            db.execSQL("INSERT INTO events VALUES (null, '"+ eventName+"','"+eventDate+"','"+eventMemo+"');");
            cursor = db.rawQuery("SELECT LAST_INSERT_ROWID();",null);
            if(cursor.moveToFirst())
                eventId = cursor.getInt(0);

            extras.putInt("eventId",eventId);
            Log.d("eventID is",String.valueOf(eventId));

            Event newEvent = new Event(Color.RED,newdate.getTime(),extras);
            compactCalendarView.addEvent(newEvent);

            showRecyclerEvents(compactCalendarView.getFirstDayOfCurrentMonth());
        }catch (ParseException e){
            e.printStackTrace();

        }

        Log.d("eventmsg",eventName+","+eventDate+","+eventMemo);

    }
    public void updateEvent(Bundle extras){

        int eventId = extras.getInt("eventId");
        String eventName = extras.getString("eventName");
        String eventDate = extras.getString("eventDate");
        String eventMemo = extras.getString("eventMemo");
        String oldDate = extras.getString("oldDate");
        try {
            Date newdate = simpleDateFormat.parse(eventDate);
            Date oldParseDate = simpleDateFormat.parse(oldDate);
            db.execSQL("UPDATE events SET name = '"+eventName+"', date = '"+eventDate+
                    "', memo ='"+eventMemo+"' WHERE id ='"+eventId+"';");
            for ( Event event : compactCalendarView.getEvents(oldParseDate)){
                Bundle eventData = (Bundle) event.getData();
                if(eventData.getInt("eventId") == eventId){
                    compactCalendarView.removeEvent(event);
                    extras.putString("eventName",eventName);
                    extras.putString("eventDate",eventDate);
                    extras.putString("eventMemo",eventMemo);
                    Event newEvent = new Event(Color.RED,newdate.getTime(),extras);
                    compactCalendarView.addEvent(newEvent);
                    showRecyclerEvents(compactCalendarView.getFirstDayOfCurrentMonth());

                    Log.d("updateEvent","is Success, id: "+eventId);
                    break;
                }
            }
        } catch (ParseException e){
            e.printStackTrace();
        }
    }
    public void deleteEvent(Bundle extras){
        int eventId = extras.getInt("eventId");
        String eventDate = extras.getString("eventDate");
        try {
            Date newdate = simpleDateFormat.parse(eventDate);
            db.execSQL("DELETE FROM events WHERE id=" + eventId + " ;");
            for (Event event : compactCalendarView.getEvents(newdate)) {
                Bundle eventData = (Bundle) event.getData();
                if(eventData.getInt("eventId") == eventId){
                    compactCalendarView.removeEvent(event);
                    showRecyclerEvents(compactCalendarView.getFirstDayOfCurrentMonth());
                    Log.d("delEvent","is Success, id: "+eventId);
                    break;
                }
            }
        } catch (ParseException e){
            e.printStackTrace();
        }
    }
    public void showRecyclerEvents(Date date){
        List<Event> events =compactCalendarView.getEventsForMonth(date);
        Bundle bundle = getArguments();

        ArrayList<EventData> dataArrayList = new ArrayList<>();
        for(Event event: events){
            Bundle extras = (Bundle) event.getData();
            String sname = bundle.getString("eventName");
            String sdate = bundle.getString("eventDate");
            String smemo = bundle.getString("eventMemo");
            int sid = extras.getInt("eventId");

            EventData eventData = new EventData(sname,sdate,smemo,sid);
            dataArrayList.add(eventData);
        }
        horizontalLayout = (LinearLayout) v.findViewById(R.id.horizontal_event_layout);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(horizontalLayout.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        horizontalAdapter = new HorizontalAdapter(dataArrayList,this);

        horizontalView = (RecyclerView) v.findViewById(R.id.horizontal_event_view);
        horizontalView.setLayoutManager(linearLayoutManager);
        horizontalView.setAdapter(horizontalAdapter);

    }

    }






