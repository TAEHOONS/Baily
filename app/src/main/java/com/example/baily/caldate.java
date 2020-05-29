package com.example.baily;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class caldate {

    public String mBirthd,result;

    public caldate(int caly, int calm, int cald) {

        String sy=String.valueOf(caly),sm=String.valueOf(calm),sd=String.valueOf(cald);

        mBirthd=sy+"-";
        Log.d("dayd", "mBirthd+sy  = " + mBirthd);
        //
        if ((calm / 10) == 0 )
            mBirthd+="0" + sm + "-";
        else
            mBirthd+=sm + "-";
        if ((cald / 10) == 0 )
            mBirthd+="0" + sd;
        else
            mBirthd+=sd;

        Log.d("dayd", "mBirthd: " + mBirthd);
        result=getProDay(mBirthd);
        Log.d("dayd", "result: " + result);

    }

    public static String getProDay(String startDate) {

        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String getTime = simpleDate.format(mDate);

        String result = "";
        String start = startDate.substring(0, 10);
        String end = getTime.substring(0, 10);
        Log.d("dayd", "end: " + end);
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date beginDate = formatter.parse(start);
            Date endDate = formatter.parse(end);

            // 시간차이를 시간,분,초를 곱한 값으로 나누면 하루 단위가 나옴
            long diff = endDate.getTime() - beginDate.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);

            result = (diffDays + 1) + "";

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;

    }
}