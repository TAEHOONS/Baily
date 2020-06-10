package com.example.baily.main.diary;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baily.DBlink;
import com.example.baily.R;
import com.example.baily.caldate;
import com.example.baily.main.home.CardItem;
import com.example.baily.main.home.MyRecyclerAdapter;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class FragDiaryAlbum extends Fragment {


    private DBlink helper;
    private SQLiteDatabase db;
    String dbName = "user.db", mId, mBabyname;
    int dbVersion = 3, requestCode = 0;

    private Activity activity;
    RecyclerView diaryRecyclerView;
    private View view;
    //일기작성페이지에서 데이터 가져올때 사용
    String recodeDate, diaryAdd;
    int addImg;
    //일기날짜, 내용, 사진
    TextView diaryDate, diaryContents, diaryTitle, diaryDateTxt;
    ImageView diaryPicture, beforeMonthBtn, afterMonthBtn;
    //일기 날짜
    String DiaryDate, selectMonth;
    SimpleDateFormat dFormat;
    Calendar dCal;

    Date now = new Date();
    SimpleDateFormat sFormat;
    private List<DiaryItem> diaryDataList = new ArrayList<>();
    TextView testTxt;


    public static FragDiaryAlbum newInstance() {
        FragDiaryAlbum fragDiaryAlbum = new FragDiaryAlbum();
        return fragDiaryAlbum;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_diary_album, container, false);

        // 앨범 리스트 선언
        diaryRecyclerView = (RecyclerView) view.findViewById(R.id.dAlbum_rV);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        diaryRecyclerView.setLayoutManager(layoutManager);

        usingDB(container);

        FloatingActionButton addDiary = view.findViewById(R.id.addDiary);//일기 추가 버튼
        beforeMonthBtn = (ImageView) view.findViewById(R.id.beforeDiaryBtn);//이전달로 넘어가는 버튼
        afterMonthBtn = (ImageView) view.findViewById(R.id.afterDiaryBtn); //다음달로 넘어가는 버튼
        diaryDateTxt = (TextView) view.findViewById(R.id.d_changeDate); //00년00월
        diaryTitle = (TextView) view.findViewById(R.id.diaryTitleTxt);//일기 제목
        diaryDate = (TextView) view.findViewById(R.id.diaryDate);//일기쓴 날짜 텍스트뷰
        diaryContents = (TextView) view.findViewById(R.id.diaryContents);//일기내용 텍스트뷰
        diaryPicture = (ImageView) view.findViewById(R.id.diaryPicture);//일기첨부 사진

        dFormat = new SimpleDateFormat("yy년 MM월");
        sFormat = new SimpleDateFormat("yyyy-MM");

        dCal = Calendar.getInstance();
        dCal.setTime(now);
        DiaryDate = dFormat.format(dCal.getTime());
        diaryDateTxt.setText(DiaryDate);
        selectMonth = sFormat.format(dCal.getTime());
        Log.d("비교하기위한        날짜", "  날짜 = " + selectMonth);

        OwnData();


        //날짜 이전버튼 눌렀을때
        beforeMonthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dCal.add(Calendar.MONTH, -1);
                DiaryDate = dFormat.format(dCal.getTime());
                diaryDateTxt.setText(DiaryDate);
                selectMonth = sFormat.format(dCal.getTime());
                Log.d("비교하기위한        날짜", " 이전  버튼 눌렀을 때  selectMonth = " + selectMonth);

                OwnData();
            }
        });
        afterMonthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dCal.add(Calendar.MONTH, +1);
                DiaryDate = dFormat.format(dCal.getTime());
                diaryDateTxt.setText(DiaryDate);
                selectMonth = sFormat.format(dCal.getTime());
                Log.d("비교하기위한        날짜", "  이후  버튼 눌렀을 때  selectMonth = " + selectMonth);

                OwnData();
            }
        });


        //다이어리 추가 버튼 눌렀을 때
        addDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, WriteDiary.class);
                startActivity(intent);

            }
        });

        Bundle bundle = getArguments();
        //번들 안의 텍스트 불러오기

        if (bundle != null) {
            //recodeDate = bundle.getString("RecodeDate");
            diaryAdd = bundle.getString("DiaryAdd");
            // addImg = bundle.getInt("AddImg");
        }

        return view;
    }


    // RistView 로 만들 예정이었던거 같음
    private void diaryInsert(String title, String Data, String memo) {

        DiaryRecyclerAdapter adapter = new DiaryRecyclerAdapter(diaryDataList);
        DiaryItem diaryData = new DiaryItem(title, Data, memo);

        diaryDataList.add(diaryData);
        diaryRecyclerView.setAdapter(adapter);
    }

    // 기존 데이터 on
    private void OwnData() {

        diaryDataList.clear();

        String sql="SELECT *FROM events where name='" + mBabyname + "'AND parents='" + mId + "'AND date LIKE'%"+selectMonth+"%'";
        Cursor c = db.rawQuery(sql, null);

        while (c.moveToNext()) {

            int id = c.getInt(0);
            String name = c.getString(2);
            String date = c.getString(3);
            String memo = c.getString(4);

            diaryInsert(name, date, memo);
        }
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
            requestCode = cursor.getInt(3);
            Log.d("DiaryDBset", "db받기 id = " + mId + "  현재 아기 = " + mBabyname + "  현재 코드 = " + requestCode);
        }

    }

}
