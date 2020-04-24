package com.example.baily;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class SecondPage extends AppCompatActivity {

    TextView mHWATV,mBerthTV;

    public static final int REQUEST_CODE_PUTHAW = 7458;
    public static final int REQUEST_CODE_GETHAW = 8778;

    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_page);
        mHWATV=(TextView)findViewById(R.id.sp_tallTV);
        mBerthTV=(TextView)findViewById(R.id.sp_berthTV);
        activity = this;

    }


    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.sp_creatBtn: {
                ThirdScreen();
                break;
            }
            case R.id.sp_tallBtn: {
                HAWPickerScreen();
                break;
            }
            case R.id.sp_berthBtn: {
                BerthPickerScreen();
                break;
            }

        }

    }

    private void ThirdScreen() {
        Intent intent = new Intent(SecondPage.this, ThirdPage.class);
        startActivity(intent);

    }

    private void HAWPickerScreen() {

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
        int width = dm.widthPixels; //디바이스 화면 너비
        int height = dm.heightPixels; //디바이스 화면 높이


        HeightAndWeight cd = new HeightAndWeight(this);
        WindowManager.LayoutParams wm = cd.getWindow().getAttributes();  //다이얼로그의 높이 너비 설정하기위해
        wm.copyFrom(cd.getWindow().getAttributes());  //여기서 설정한값을 그대로 다이얼로그에 넣겠다는의미
        wm.width = (width / 3) *2;  //화면 너비의 절반
        wm.height = (height / 3)*2;  //화면 높이의 절반

        cd.setDialogListener(new HeightAndWeight.CustomDialogListener(){
            @Override
            public void onPositiveClicked(String hei, String wei) {
                mHWATV.setText(wei+"Kg  "+hei+"cm");
            }
        });

        cd.show();

    }
    private void BerthPickerScreen() {

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
        int width = dm.widthPixels; //디바이스 화면 너비
        int height = dm.heightPixels; //디바이스 화면 높이


        BirthdayPicker cd = new BirthdayPicker(this);
        WindowManager.LayoutParams wm = cd.getWindow().getAttributes();  //다이얼로그의 높이 너비 설정하기위해
        wm.copyFrom(cd.getWindow().getAttributes());  //여기서 설정한값을 그대로 다이얼로그에 넣겠다는의미
        wm.width = (width / 3) *2;  //화면 너비의 절반
        wm.height = (height / 3)*2;  //화면 높이의 절반

        cd.setDialogListener(new BirthdayPicker.CustomDialogListener(){
            @Override
            public void onPositiveClicked(int year, int month,int day) {
                mHWATV.setText(year+"년  "+month+"월  "+day+"일");
            }
        });

        cd.show();

    }



}
