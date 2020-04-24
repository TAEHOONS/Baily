package com.example.baily;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.util.Calendar;

public class BirthdayPicker extends Dialog {


    Calendar calendar = Calendar.getInstance();
    int cYear = calendar.get(Calendar.YEAR);
    int cMonth = calendar.get(Calendar.MONTH);
    int cDay = calendar.get(Calendar.DAY_OF_MONTH);

    int i;

    NumberPicker mYear, mMonth, mDay;
    Button mCompleBtn;
    private CustomDialogListener customDialogListener;


    public BirthdayPicker(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);   //다이얼로그의 타이틀바를 없애주는 옵션입니다.
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  //다이얼로그의 배경을 투명으로 만듭니다.
        setContentView(R.layout.activity_berthday_picker);     //다이얼로그에서 사용할 레이아웃입니다.

        mCompleBtn = (Button) findViewById(R.id.bp_inputBtn);
        mYear = (NumberPicker) findViewById(R.id.bp_NPyear);
        mMonth = (NumberPicker) findViewById(R.id.bp_NPmonth);
        mDay = (NumberPicker) findViewById(R.id.bp_NPday);
        getPickerdata();


        mCompleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int putYear = mYear.getValue();
                int putmonth = mMonth.getValue();
                int putDay = mDay.getValue()+1;

                customDialogListener.onPositiveClicked(putYear, putmonth, putDay);
                dismiss();
            }
        });
    }

    public void getPickerdata() {
        mYear.setMaxValue(cYear);
        mYear.setMinValue(cYear - 12);
        mYear.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); // 데이터 선택 시 editText 방지
        mYear.setWrapSelectorWheel(false);
        mYear.setValue(cYear);

        mMonth.setMaxValue(12);
        mMonth.setMinValue(1);
        mMonth.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mMonth.setWrapSelectorWheel(true);
        mMonth.setValue(cMonth + 1);

        String[] stringDate = new String[31];
        for (i = 0; i < 31; i++) {
            stringDate[i] = String.valueOf(i + 1);
        }
        mDay.setDisplayedValues(stringDate);
        mDay.setMaxValue(30);
        mDay.setMinValue(0);
        mDay.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mDay.setWrapSelectorWheel(true);
        mDay.setValue(cDay-1);

    }

    interface CustomDialogListener {
        void onPositiveClicked(int year, int month, int day);
    }

    //호출할 리스너 초기화
    public void setDialogListener(BirthdayPicker.CustomDialogListener customDialogListener) {
        this.customDialogListener = customDialogListener;
    }


}
