package com.example.baily.main.diary;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baily.R;
import com.example.baily.caldate;
import com.example.baily.log.MainActivity;
import com.example.baily.main.home.CardItem;
import com.example.baily.main.home.MyRecyclerAdapter;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class WriteDiary extends AppCompatActivity {

    FragDiarysearch diaryAlbumFrag;
    ImageView backBtn, confirmBtn, photoBtn, diaryPhoto;//이전, 완료 버튼,사진추가버튼, 사진
    EditText diaryEdt;
    String recodeDate, diaryAdd;
    int addImg;
    TextView diaryDateTxt;
    Date now = new Date();
    SimpleDateFormat sFormat;
    private ArrayList<EventData> diaryDataList = new ArrayList<>();
    RecyclerView diaryRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_diary);
        diaryRecyclerView = (RecyclerView) findViewById(R.id.h_rView);
        //RecyclerView.LayoutManager lManager = new LinearLayoutManager(this);
        //diaryRecyclerView.setLayoutManager(lManager);

        backBtn = (ImageView) findViewById(R.id.d_backBtn); //이전버튼
        confirmBtn = (ImageView) findViewById(R.id.d_confirmBtn);//완료버튼
        photoBtn = (ImageView) findViewById(R.id.d_addPhotoBtn);//사진추가버튼
        diaryPhoto = (ImageView) findViewById(R.id.diaryAddPicture);//추가된 사진
        diaryDateTxt = (TextView) findViewById(R.id.diaryDateTxt);//일기 작성중인 오늘 날짜
        sFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        recodeDate = sFormat.format(now);//오늘 날짜
        diaryEdt = (EditText) findViewById(R.id.d_WriteEdt);
        diaryAlbumFrag = new FragDiarysearch();


        diaryDateTxt.setText(recodeDate);

        //이전버튼 눌렀을 때, '저장되지않음'경고창 띄우기
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(WriteDiary.this);

                dlg.setTitle("경고");
                dlg.setIcon(R.drawable.ic_round_report_problem_24);
                dlg.setMessage("이전으로 가면 저장되지않습니다.\n그래도 돌아가시겠습니까?");
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                dlg.setNegativeButton("취소", null);
                dlg.show();
            }
        });
        photoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diaryPhoto.setVisibility(View.VISIBLE);
            }
        });

        //확인버튼 눌렀을 때
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diaryAdd = diaryEdt.getText().toString();
                addImg = R.drawable.ic_baby;
                //일기 내용이 없을 경우
                if (diaryAdd.equals("")) {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(WriteDiary.this);

                    dlg.setTitle("알림");
                    dlg.setIcon(R.drawable.ic_baseline_report_24);
                    dlg.setMessage("내용이 없으므로 저장되지않습니다.");
                    dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    dlg.setNegativeButton("취소", null);
                    dlg.show();
                } else {
                    diaryAlbumFrag = new FragDiarysearch();

                    Bundle bundle = new Bundle();
                    bundle.putString("DiaryAdd", diaryAdd);
                    bundle.putInt("AddImg", addImg);

                    diaryAlbumFrag.setArguments(bundle);
                }
            }
        });

    }


}
