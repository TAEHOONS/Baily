package com.example.baily.main.growth;

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

import com.example.baily.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChildFragDay extends Fragment {
    private View view;
    private LineChart growDayKgCart, growDayCmCart, growDayHeadCart, growDayFeverCart;
    TextView avgKgTxt, avgCmTxt, avgHeadTxt, avgFeverTxt, dayDateTxt;
    float avgWeight,avgHeight,avgHead,avgFever;
    float kgSum = 0;
    float cmSum = 0;
    float headSum = 0;
    float feverSum = 0;
    ImageView beforeBtn,afterBtn;
    String dayStartDate, dayEndDate;
    Calendar cal, plusCal;
    Date date = new Date();
    SimpleDateFormat sFormat;


    public static ChildFragDay newInstance(){
        ChildFragDay childFragDay = new ChildFragDay();
        return childFragDay;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.growth_child_frag_day,container,false);
        growDayKgCart = view.findViewById(R.id.growthDayKgLineCart);
        growDayCmCart = view.findViewById(R.id.growthDayCmLineCart);
        growDayHeadCart = view.findViewById(R.id.growthDayHeadLineCart);
        growDayFeverCart = view.findViewById(R.id.growthDayFeverLineCart);
        avgKgTxt = view.findViewById(R.id.avgKgTxt);
        avgCmTxt = view.findViewById(R.id.avgCmTxt);
        avgHeadTxt = view.findViewById(R.id.avgHeadTxt);
        avgFeverTxt = view.findViewById(R.id.avgFeverTxt);
        dayDateTxt = view.findViewById(R.id.dayDateTxt);
        beforeBtn = (ImageView)view.findViewById(R.id.beforeBtn);
        afterBtn = (ImageView)view.findViewById(R.id.afterBtn);

        sFormat = new SimpleDateFormat("MM월 dd일");
        cal = Calendar.getInstance();
        plusCal = Calendar.getInstance();

        cal.setTime(date);
        dayStartDate =sFormat.format(cal.getTime());
        plusCal.setTime(date);
        plusCal.add(Calendar.DATE, +7);
        dayEndDate = sFormat.format(plusCal.getTime());
        dayDateTxt.setText(dayStartDate+" ~ "+dayEndDate);

        beforeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.DATE, -7);
                dayStartDate =sFormat.format(cal.getTime());

                plusCal.add(Calendar.DATE, -7);
                dayEndDate = sFormat.format(plusCal.getTime());
                dayDateTxt.setText(dayStartDate+" ~ "+dayEndDate);
            }
        });
        afterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.DATE, +7);
                dayStartDate =sFormat.format(cal.getTime());

                plusCal.add(Calendar.DATE, +7);
                dayEndDate = sFormat.format(plusCal.getTime());
                dayDateTxt.setText(dayStartDate+" ~ "+dayEndDate);
            }
        });

        ArrayList<Entry> kgValues = new ArrayList<>();
        ArrayList<Entry> cmValues = new ArrayList<>();
        ArrayList<Entry> headValues = new ArrayList<>();
        ArrayList<Entry> feverValues = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            float val = (float) (Math.random() * 10);
            kgSum = kgSum + val;
            kgValues.add(new Entry(i, val));
            avgWeight = kgSum/i;
        }
        for (int i = 0; i < 10; i++) {
            float val = (float) (Math.random() * 10);
            cmSum = cmSum + val;
            cmValues.add(new Entry(i, val));
            avgHeight = cmSum/i;
        }
        for (int i = 0; i < 10; i++) {
            float val = (float) (Math.random() * 10);
            headSum = headSum + val;
            headValues.add(new Entry(i, val));
            avgHead = headSum/i;
        }
        for (int i = 0; i < 10; i++) {
            float val = (float) 36.5;
            feverSum = feverSum + val;
            feverValues.add(new Entry(i, val));
            avgFever = feverSum/i;
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
        growDayKgCart.setData(dayKgData);
        avgKgTxt.setText(avgWeight+" kg");

        growDayCmCart.setData(dayCmData);
        avgCmTxt.setText(avgHeight+" cm");

        growDayHeadCart.setData(dayHeadData);
        avgHeadTxt.setText(avgHead+" cm");

        growDayFeverCart.setData(dayFeverData);
        avgFeverTxt.setText(avgFever+" 도");

        return view;
    }


}
