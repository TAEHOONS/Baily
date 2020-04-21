package com.example.baily;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class HeightAndWeight extends Dialog {

    EditText mHeightET,mWeightET;
    Button mCompleBtn;
    private CustomDialogListener customDialogListener;


    public HeightAndWeight(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);   //다이얼로그의 타이틀바를 없애주는 옵션입니다.
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  //다이얼로그의 배경을 투명으로 만듭니다.
        setContentView(R.layout.activity_height_and_weight);     //다이얼로그에서 사용할 레이아웃입니다.

        mCompleBtn=(Button)findViewById(R.id.haw_inputBtn);
        mHeightET = (EditText)findViewById(R.id.haw_heightET);
        mWeightET = (EditText)findViewById(R.id.haw_weightET);

       mCompleBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //각각의 변수에 EidtText에서 가져온 값을 저장
               String height = mHeightET.getText().toString();
               String weight = mWeightET.getText().toString();


               //인터페이스의 함수를 호출하여 변수에 저장된 값들을 Activity로 전달
               customDialogListener.onPositiveClicked(height,weight);
               dismiss();

           }
       });


    }
    //인터페이스 설정
    interface CustomDialogListener{
        void onPositiveClicked(String hei, String wei);
    }

    //호출할 리스너 초기화
    public void setDialogListener(CustomDialogListener customDialogListener){
        this.customDialogListener = customDialogListener;
    }




//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        //바깥레이어 클릭시 안닫히게
//        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public void onBackPressed() {
//        //안드로이드 백버튼 막기
//        return;
//    }
}
