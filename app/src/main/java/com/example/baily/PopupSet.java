package com.example.baily;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baily.babyPlus.FirstPage;
import com.example.baily.babyPlus.HeightAndWeight;
import com.example.baily.main.setting;

import java.util.Locale;

public class PopupSet extends Dialog {

    String dbName = "user.db";
    int dbVersion = 3;
    private DBlink helper;
    private SQLiteDatabase db;

    TextView mPopUpText;
    Button mCinfirmBtn, mCancleBtn;
    int setting = 0;
    private CustomDialogListener customDialogListener;


    public PopupSet(@NonNull final Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);   //다이얼로그의 타이틀바를 없애주는 옵션입니다.
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  //다이얼로그의 배경을 투명으로 만듭니다.
        setContentView(R.layout.popup_set);     //다이얼로그에서 사용할 레이아웃입니다.

        mCinfirmBtn = (Button) findViewById(R.id.popup_Confirm);
        mCancleBtn = (Button) findViewById(R.id.popup_Cancle);
        mPopUpText = (TextView) findViewById(R.id.popup_TextView);


        mCinfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //각각의 변수에 EidtText에서 가져온 값을 저장
                Toast.makeText(context, "확인", Toast.LENGTH_SHORT).show();
                //인터페이스의 함수를 호출하여 변수에 저장된 값들을 Activity로 전달
                customDialogListener.onPositiveClicked(true);
                dismiss();

            }
        });
        mCancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 그냥 취소
                Toast.makeText(context, "취소", Toast.LENGTH_SHORT).show();
                customDialogListener.onPositiveClicked(false);
                dismiss();

            }
        });

    }


    //인터페이스 설정
    public interface CustomDialogListener {
        void onPositiveClicked(Boolean exit);
    }


    //호출할 리스너 초기화
    public void setDialogListener(CustomDialogListener customDialogListener, int textset) {
        this.customDialogListener = customDialogListener;
        SettingText(textset);
    }

    // 호출 Text 설정
    private void SettingText(int textnum) {
        switch (textnum) {
            case 1:
                mPopUpText.setText("계정을 삭제 \n 하시겠습니까?");
                setting = 1;
                break;
            case 2:
                mPopUpText.setText("아기를 수정 \n 하시겠습니까?");
                setting = 2;
                break;
            case 3:
                mPopUpText.setText("아기를 삭제 \n 하시겠습니까?");
                setting = 3;
                break;
        }


    }



}
