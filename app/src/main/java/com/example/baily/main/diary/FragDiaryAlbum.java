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
import android.widget.LinearLayout;
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
import com.example.baily.main.recode.RecodeAdapter;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;


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
    //검색입력
    EditText searchEdit;
    //검색어
    String searchStr;
    Date now = new Date();
    SimpleDateFormat sFormat;
    private List<DiaryItem> diaryDataList = new ArrayList<>();

    View dialogView;


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

        final FloatingActionButton searchBtn = view.findViewById(R.id.searchBtn);//일기 검색 버튼
        ImageView homeBtn = view.findViewById(R.id.d_returnHome);//이번달로 돌아올수 있는 버튼
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
                diaryDataList.clear();
                diaryRecyclerView.setAdapter(null);

                dCal.add(Calendar.MONTH, -1);
                DiaryDate = dFormat.format(dCal.getTime());
                diaryDateTxt.setText(DiaryDate);
                selectMonth = sFormat.format(dCal.getTime());
                Log.d("비교하기위한        날짜", " 이전  버튼 눌렀을 때  selectMonth = " + selectMonth);
                Toast.makeText(activity,DiaryDate+" 일기",Toast.LENGTH_SHORT).show();
                OwnData();
            }
        });
        afterMonthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diaryDataList.clear();
                diaryRecyclerView.setAdapter(null);


                dCal.add(Calendar.MONTH, +1);
                DiaryDate = dFormat.format(dCal.getTime());
                diaryDateTxt.setText(DiaryDate);
                selectMonth = sFormat.format(dCal.getTime());
                Log.d("비교하기위한        날짜", "  이후  버튼 눌렀을 때  selectMonth = " + selectMonth);
                Toast.makeText(activity,DiaryDate+" 일기",Toast.LENGTH_SHORT).show();

                OwnData();
            }
        });


        //다이어리 검색 버튼 눌렀을 때
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogView = (View) View.inflate(activity, R.layout.search_dialog, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(activity);

                dlg.setTitle("   ");
                dlg.setView(dialogView);
                dlg.setPositiveButton("검색",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        diaryDataList.clear();
                        searchEdit= (EditText) dialogView.findViewById(R.id.edit_search);//검색어입력
                        searchStr = searchEdit.getText().toString();
                        Log.d("searchDia", "  get TExt = " + searchStr);
                        showSearchResult(searchStr);
                        diaryDateTxt.setText("검색어 // "+searchStr);
                        beforeMonthBtn.setVisibility(View.GONE);//이전버튼 숨기기
                        afterMonthBtn.setVisibility(View.GONE);//이후버튼 숨기기
                    }
                });
                dlg.setNegativeButton("취소", null);
                dlg.setCancelable(false);
                dlg.show();
            }
        });

        //이번달 일기 목록으로 돌아오기
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beforeMonthBtn.setVisibility(View.VISIBLE);
                afterMonthBtn.setVisibility(View.VISIBLE);
                Date nowDate = new Date();
                dCal.setTime(nowDate);
                DiaryDate = dFormat.format(dCal.getTime());
                diaryDateTxt.setText(dFormat.format(nowDate));
                selectMonth = sFormat.format(dCal.getTime());
                OwnData();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == FragDiaryDate.SHOW_EVENT_INFO){
            if(resultCode == RESULT_OK){
                searchEdit = dialogView.findViewById(R.id.edit_search);
                showSearchResult(searchEdit.getText().toString());
            }
        }
    }

    private void showSearchResult(String searchStr){
        diaryDataList.clear();
        diaryRecyclerView.setAdapter(null);

        String[] searchArr = searchStr.split(" ");
        if(searchStr.equals("")) return;
        Cursor c = db.rawQuery("SELECT * FROM events ",null);
        while (c.moveToNext()){

            int id = c.getInt(0);
            String name = c.getString(2);
            String date = c.getString(3);
            String memo = c.getString(4);
            boolean istarget = false;

            for(int i=0;i<searchArr.length;i++){
                if(name.toLowerCase().contains(searchArr[i].toLowerCase())||memo.toLowerCase().contains(searchArr[i].toLowerCase())){
                    istarget = true;
                    Log.d("searchDia", "  istarget = " + istarget);
                    break;
                }
            }
            if(istarget){
                Log.d("searchDia", "  Start istarget title="+name+" , memo = "+memo);

                diaryInsert(name,date,memo);

                Log.d("event is ",name);
            }
        }
        diaryDataList.sort(dateComparator);//날짜순으로 정렬
    }
    //날짜 순으로
    public static Comparator<DiaryItem> dateComparator = new Comparator<DiaryItem>() {
        @Override
        public int compare(DiaryItem o1, DiaryItem o2) {
            return o1.getRecodeDay().compareTo(o2.getRecodeDay());
        }
    };


    //데이터 추가
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
        diaryDataList.sort(dateComparator);//날짜순으로 정렬
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
            Log.d("DiaryDBset", "db받기 id = " + mId + "  현재 아기 = " + mBabyname);
        }

    }

}
