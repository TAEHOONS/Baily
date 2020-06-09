package com.example.baily.main.diary;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baily.DBlink;
import com.example.baily.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;

public class SearchEvent extends Activity {

    private DBlink helper;
    private SQLiteDatabase db;
    String dbName = "user.db", mId, mBabyname;
    int dbVersion = 3;


    private LinearLayout verticalLayout;
    private VerticalSearchAdapter verticalAdapter;
    private RecyclerView verticalView;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchevent);
        final EditText searchEdit= (EditText) findViewById(R.id.edit_search);

        usingDB();


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
        Cursor c = db.rawQuery("SELECT *FROM events ",null);
        while (c.moveToNext()){

            int id = c.getInt(0);
            String name = c.getString(2);
            String date = c.getString(3);
            String memo = c.getString(4);
            boolean istarget=false;

            for(int i=0;i<searchArr.length;i++){
                if(name.toLowerCase().contains(searchArr[i].toLowerCase())){
                    istarget = true;
                    break;
                }
            }
            if(istarget){
                EventData eventData = new EventData(mBabyname,name,date,memo,mId,id);
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

    // DB 연결
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
