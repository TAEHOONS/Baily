package com.example.baily.main.diary;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baily.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;

public class SearchEvent extends Activity {
    private DBHelper helper;
    private Cursor cursor;
    private LinearLayout verticalLayout;
    private SQLiteDatabase db;
    private VerticalSearchAdapter verticalAdapter;
    private RecyclerView verticalView;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchevent);
        final EditText searchEdit= (EditText) findViewById(R.id.edit_search);

        helper = new DBHelper(this);
        db = helper.getWritableDatabase();


        ImageButton searchBtn = (ImageButton) findViewById(R.id.btn_eventSearch);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchStr = searchEdit.getText().toString();
                showSearchResult(searchStr);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == FragDiaryDate.SHOW_EVENT_INFO){
            if(resultCode == RESULT_OK){
                ((FragDiaryDate) FragDiaryDate.thisContext).updateEvent(data.getExtras());
                EditText editText = findViewById(R.id.edit_search);
                showSearchResult(editText.getText().toString());
            }
        }
    }

    private void showSearchResult(String searchStr){
        ArrayList<EventData> dataArrayList = new ArrayList<>();
        String[] searchArr = searchStr.split(" ");
        if(searchStr.equals("")) return;
        cursor = db.rawQuery("SELECT *FROM events",null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String date = cursor.getString(2);
            String memo = cursor.getString(3);
            boolean istarget=false;

            for(int i=0;i<searchArr.length;i++){
                if(name.toLowerCase().contains(searchArr[i].toLowerCase())){
                    istarget = true;
                    break;
                }
            }
            if(istarget){
                EventData eventData = new EventData(name,date,memo,id);
                dataArrayList.add(eventData);
                Log.d("event is ",name);
            }
        }
        dataArrayList.sort(dateComparator);

        Log.d("da", String.valueOf(dataArrayList.size()));


        verticalLayout = (LinearLayout) findViewById(R.id.search_event_layout);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(verticalLayout.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        verticalAdapter = new VerticalSearchAdapter(dataArrayList,this);

        verticalView = (RecyclerView) findViewById(R.id.search_event_view);
        verticalView.setLayoutManager(linearLayoutManager);
        verticalView.setAdapter(verticalAdapter);
    }
    public static Comparator<EventData> dateComparator = new Comparator<EventData>() {
        @Override
        public int compare(EventData o1, EventData o2) {
            return o1.getDate().compareTo(o2.getDate());
        }
    };


}
