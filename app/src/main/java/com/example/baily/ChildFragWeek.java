package com.example.baily;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChildFragWeek extends Fragment {
    private View view;
    private LineChart growWeekKgCart, growWeekCmCart, growWeekHeadCart, growWeekFeverCart;
    TextView weekAvgKgTxt, weekAvgCmTxt, weekAvgHeadTxt, weekAvgFeverTxt, weekDateTxt;
    float weekAvgWeight,weekAvgHeight,weekAvgHead,weekAvgFever;
    float wKgSum = 0;
    float wCmSum = 0;
    float wHeadSum = 0;
    float wFeverSum = 0;
    ImageView weekBeforeBtn,weekAfterBtn;
    String weekStartDate, weekEndDate;
    Calendar wCal, wPlusCal;
    Date date = new Date();
    SimpleDateFormat sFormat;

    public static ChildFragWeek newInstance(){
        ChildFragWeek childFragWeek = new ChildFragWeek();
        return childFragWeek;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.growth_child_frag_week,container,false);
        growWeekKgCart = view.findViewById(R.id.growthWeekKgLineCart);
        growWeekCmCart = view.findViewById(R.id.growthWeekCmLineCart);
        growWeekHeadCart = view.findViewById(R.id.growthWeekHeadLineCart);
        growWeekFeverCart = view.findViewById(R.id.growthWeekFeverLineCart);
        weekAvgKgTxt = view.findViewById(R.id.weekAvgKgTxt);
        weekAvgCmTxt = view.findViewById(R.id.weekAvgCmTxt);
        weekAvgHeadTxt = view.findViewById(R.id.weekAvgHeadTxt);
        weekAvgFeverTxt = view.findViewById(R.id.weekAvgFeverTxt);
        weekDateTxt = view.findViewById(R.id.weekDateTxt);
        weekBeforeBtn = (ImageView)view.findViewById(R.id.weekBeforeBtn);
        weekAfterBtn = (ImageView)view.findViewById(R.id.weekAfterBtn);

        sFormat = new SimpleDateFormat("yy년 MM월");
        wCal = Calendar.getInstance();
        wPlusCal = Calendar.getInstance();

        wCal.setTime(date);
        weekStartDate =sFormat.format(wCal.getTime());
        weekDateTxt.setText(weekStartDate);
        /*
        wPlusCal.setTime(date);
        wPlusCal.add(Calendar.MONTH, +1);
        weekEndDate = sFormat.format(wPlusCal.getTime());
        */

        weekBeforeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wCal.add(Calendar.MONTH, -1);
                weekStartDate =sFormat.format(wCal.getTime());
                weekDateTxt.setText(weekStartDate);
                /*
                wPlusCal.add(Calendar.MONTH, -1);
                weekEndDate = sFormat.format(wPlusCal.getTime());
                weekDateTxt.setText(weekStartDate+" ~ "+weekEndDate);
                */
            }
        });
        weekAfterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wCal.add(Calendar.MONTH, +1);
                weekStartDate =sFormat.format(wCal.getTime());
                weekDateTxt.setText(weekStartDate);


                /*
                wPlusCal.add(Calendar.MONTH, +1);
                weekEndDate = sFormat.format(wPlusCal.getTime());
                weekDateTxt.setText(weekStartDate+" ~ "+weekEndDate);
                */
            }
        });

        ArrayList<Entry> kgValues = new ArrayList<>();
        ArrayList<Entry> cmValues = new ArrayList<>();
        ArrayList<Entry> headValues = new ArrayList<>();
        ArrayList<Entry> feverValues = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            float val = (float) (Math.random() * 10);
            wKgSum = wKgSum + val;
            kgValues.add(new Entry(i, val));
            weekAvgWeight = wKgSum/i;
        }
        for (int i = 0; i < 10; i++) {
            float val = (float) (Math.random() * 10);
            wCmSum = wCmSum + val;
            cmValues.add(new Entry(i, val));
            weekAvgHeight = wCmSum/i;
        }
        for (int i = 0; i < 10; i++) {
            float val = (float) (Math.random() * 10);
            wHeadSum = wHeadSum + val;
            headValues.add(new Entry(i, val));
            weekAvgHead = wHeadSum/i;
        }
        for (int i = 0; i < 10; i++) {
            float val = (float) 36.5;
            wFeverSum = wFeverSum + val;
            feverValues.add(new Entry(i, val));
            weekAvgFever = wFeverSum/i;
        }
        LineDataSet dayKg,dayCm,dayHead,dayFever;
        dayKg = new LineDataSet(kgValues, "몸무게");
        dayCm = new LineDataSet(cmValues, "신장");
        dayHead = new LineDataSet(headValues, "머리둘레");
        dayFever = new LineDataSet(feverValues, "체온");


        ArrayList<ILineDataSet> dayKgDataSets = new ArrayList<>();
        ArrayList<ILineDataSet> dayCmDataSets = new ArrayList<>();
        ArrayList<ILineDataSet> dayHeadDataSets = new ArrayList<>();
        ArrayList<ILineDataSet> dayFeverDataSets = new ArrayList<>();

        dayKgDataSets.add(dayKg); // add the data sets
        dayCmDataSets.add(dayCm);
        dayHeadDataSets.add(dayHead);
        dayFeverDataSets.add(dayFever);

        // create a data object with the data sets
        LineData dayKgData = new LineData(dayKgDataSets);
        LineData dayCmData = new LineData(dayCmDataSets);
        LineData dayHeadData = new LineData(dayHeadDataSets);
        LineData dayFeverData = new LineData(dayFeverDataSets);


        // black lines and points
        dayKg.setColor(Color.BLACK);
        dayKg.setCircleColor(Color.BLACK);

        dayCm.setColor(Color.RED);
        dayCm.setCircleColor(Color.RED);

        dayHead.setColor(Color.BLUE);
        dayHead.setCircleColor(Color.BLUE);

        dayFever.setColor(Color.GREEN);
        dayFever.setCircleColor(Color.GREEN);

        // set data
        growWeekKgCart.setData(dayKgData);
        weekAvgKgTxt.setText(weekAvgWeight+" kg");

        growWeekCmCart.setData(dayCmData);
        weekAvgCmTxt.setText(weekAvgHeight+" cm");

        growWeekHeadCart.setData(dayHeadData);
        weekAvgHeadTxt.setText(weekAvgHead+" cm");

        growWeekFeverCart.setData(dayFeverData);
        weekAvgFeverTxt.setText(weekAvgFever+" °C");
        return view;
    }
}
