package com.example.baily.main.diary;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baily.R;
import com.example.baily.caldate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FragDiaryAlbum extends Fragment {
    private Activity activity;
    RecyclerView diaryRecyclerView;
    private View view;
    //일기작성페이지에서 데이터 가져올때 사용
    String recodeDate, diaryAdd;
    int addImg;
    //일기날짜, 내용, 사진
    TextView diaryDate, diaryContents;
    ImageView diaryPicture;
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
        /*
        diaryRecyclerView = (RecyclerView) view.findViewById(R.id.h_rView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        diaryRecyclerView.setLayoutManager(layoutManager);

         */

        FloatingActionButton addDiary = view.findViewById(R.id.addDiary);
        diaryDate = (TextView) view.findViewById(R.id.diaryDate);//일기쓴 날짜 텍스트뷰
        diaryContents = (TextView) view.findViewById(R.id.diaryContents);//일기내용 텍스트뷰
        diaryPicture = (ImageView) view.findViewById(R.id.diaryPicture);//일기첨부 사진
        testTxt = view.findViewById(R.id.testTxt);
        //다이어리 추가 버튼 눌렀을 때

        addDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,WriteDiary.class);
                startActivity(intent);

            }
        });

        Bundle bundle = getArguments();
        //번들 안의 텍스트 불러오기
        if (bundle != null) {
            //recodeDate = bundle.getString("RecodeDate");
            diaryAdd = bundle.getString("DiaryAdd");
            // addImg = bundle.getInt("AddImg");

            testTxt.setText(diaryAdd);
        }

        return view;
    }

    // RistView 로 만들 예정이었던거 같음
//    private void diaryInsert() {
//
//        DiaryRecyclerAdapter adapter = new DiaryRecyclerAdapter(diaryDataList);
//        DiaryItem diaryData = new DiaryItem("날짜","하이",0);
//
//        diaryDataList.add(diaryData);
//        diaryRecyclerView.setAdapter(adapter);
//
//    }
}
