package com.example.baily;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class BerthdayPicker extends Dialog {

    EditText mHeightET,mWeightET;
    Button mCompleBtn;
    private CustomDialogListener customDialogListener;


    public BerthdayPicker(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);   //다이얼로그의 타이틀바를 없애주는 옵션입니다.
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  //다이얼로그의 배경을 투명으로 만듭니다.
        setContentView(R.layout.activity_berthday_picker);     //다이얼로그에서 사용할 레이아웃입니다.
    }

    interface CustomDialogListener{
        void onPositiveClicked(int year, int wei,int day);
    }

    //호출할 리스너 초기화
    public void setDialogListener(BerthdayPicker.CustomDialogListener customDialogListener){
        this.customDialogListener = customDialogListener;
    }

}
