package com.example.baily.main.growth;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.baily.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChildFragDay extends Fragment{
    private View view;
    private LineChart growDayKgCart, growDayCmCart, growDayHeadCart, growDayFeverCart;
    TextView avgKgTxt, avgCmTxt, avgHeadTxt, avgFeverTxt, dayDateTxt;
    float avgWeight,avgHeight,avgHead,avgFever;
    float kgSum = 0;
    float cmSum = 0;
    float headSum = 0;
    float feverSum = 0;
    ImageView beforeBtn,afterBtn;
    String dayStartDate, dayEndDate, today;
    Calendar cal, plusCal;
    Date date = new Date();
    SimpleDateFormat sFormat,simpleDate;
    int dStart, dEnd;

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

        //차트 구간설정을 위한
        simpleDate = new SimpleDateFormat("dd");

        sFormat = new SimpleDateFormat("MM월 dd일");
        today =sFormat.format(date); //오늘날짜
        cal = Calendar.getInstance();
        plusCal = Calendar.getInstance();


        cal.setTime(date);
        cal.add(Calendar.DATE, -6);
        dayStartDate =sFormat.format(cal.getTime());

        plusCal.setTime(date);
        dayEndDate = sFormat.format(plusCal.getTime());
        dayDateTxt.setText(dayStartDate+" ~ "+dayEndDate);

        beforeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.DATE, -6);
                dayStartDate =sFormat.format(cal.getTime());

                plusCal.add(Calendar.DATE, -6);
                dayEndDate = sFormat.format(plusCal.getTime());
                dayDateTxt.setText(dayStartDate+" ~ "+dayEndDate);

            }
        });
        afterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //2000.00.00~마지막 날짜, 마지막날짜가 오늘날짜이면 더이상 넘어갈수 없게
                if(today.equals(dayEndDate)){
                    Toast.makeText(getActivity(), "오늘이후 기록이 없습니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    cal.add(Calendar.DATE, +6);
                    dayStartDate =sFormat.format(cal.getTime());

                    plusCal.add(Calendar.DATE, +6);
                    dayEndDate = sFormat.format(plusCal.getTime());
                    dayDateTxt.setText(dayStartDate+" ~ "+dayEndDate);
                }
            }
        });

        //차트에 들어가는 값
        ArrayList<Entry> kgValues = new ArrayList<>();
        ArrayList<Entry> cmValues = new ArrayList<>();
        ArrayList<Entry> headValues = new ArrayList<>();
        ArrayList<Entry> feverValues = new ArrayList<>();


        dStart =Integer.parseInt(simpleDate.format(cal.getTime()));
        dEnd =Integer.parseInt(simpleDate.format(plusCal.getTime()));

        for (int i = 1; i <= 7; i++) {
            float val = (float) (Math.random() * 10);
            kgSum = kgSum + val;
            kgValues.add(new Entry(i, val));
            avgWeight = kgSum/i;
        }

        for (int i = dStart; i <= dEnd; i++) {
            float val = (float) (Math.random() * 10);
            cmSum = cmSum + val;
            cmValues.add(new Entry(i, val));
            avgHeight = cmSum/i;
        }
        for (int i = dStart; i <= dEnd; i++) {
            float val = (float) (Math.random() * 10);
            headSum = headSum + val;
            headValues.add(new Entry(i, val));
            avgHead = headSum/i;
        }
        for (int i = dStart; i <= dEnd; i++) {
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

        //몸무게 차트 속성
        XAxis kgXAxis = growDayKgCart.getXAxis(); // x 축 설정
        kgXAxis.setPosition(XAxis.XAxisPosition.TOP); //x 축 표시에 대한 위치 설정
        kgXAxis.setLabelCount(7, true); //X축의 데이터를 최대 몇개 까지 나타낼지에 대한 설정 5개 force가 true 이면 반드시 보여줌

        YAxis kgYAxisLeft = growDayKgCart.getAxisLeft(); //Y축의 왼쪽면 설정
        kgYAxisLeft.setDrawLabels(false);
        kgYAxisLeft.setDrawAxisLine(false);
        kgYAxisLeft.setDrawGridLines(false);
        YAxis kgYAxisRight = growDayKgCart.getAxisRight(); //Y축의 오른쪽면 설정
        kgYAxisRight.setLabelCount(4, true);

        // set data
        growDayKgCart.setDescription(null);
        growDayKgCart.setData(dayKgData);
        avgKgTxt.setText(avgWeight+" kg");

        //신장 차트 속성
        XAxis cmXAxis = growDayCmCart.getXAxis(); // x 축 설정
        cmXAxis.setPosition(XAxis.XAxisPosition.TOP); //x 축 표시에 대한 위치 설정
        cmXAxis.setLabelCount(7, true); //X축의 데이터를 최대 몇개 까지 나타낼지에 대한 설정 5개 force가 true 이면 반드시 보여줌

        YAxis cmYAxisLeft = growDayCmCart.getAxisLeft(); //Y축의 왼쪽면 설정
        cmYAxisLeft.setDrawLabels(false);
        cmYAxisLeft.setDrawAxisLine(false);
        cmYAxisLeft.setDrawGridLines(false);
        YAxis cmYAxisRight = growDayCmCart.getAxisRight(); //Y축의 오른쪽면 설정
        cmYAxisRight.setLabelCount(4, true);

        growDayCmCart.setDescription(null);
        growDayCmCart.setData(dayCmData);
        avgCmTxt.setText(avgHeight+" cm");

        //머리둘레 차트 속성
        XAxis headXAxis = growDayHeadCart.getXAxis(); // x 축 설정
        headXAxis.setPosition(XAxis.XAxisPosition.TOP); //x 축 표시에 대한 위치 설정
        headXAxis.setLabelCount(7, true); //X축의 데이터를 최대 몇개 까지 나타낼지에 대한 설정 5개 force가 true 이면 반드시 보여줌

        YAxis headYAxisLeft = growDayHeadCart.getAxisLeft(); //Y축의 왼쪽면 설정
        headYAxisLeft.setDrawLabels(false);
        headYAxisLeft.setDrawAxisLine(false);
        headYAxisLeft.setDrawGridLines(false);
        YAxis headYAxisRight = growDayHeadCart.getAxisRight(); //Y축의 오른쪽면 설정
        headYAxisRight.setLabelCount(4, true);

        growDayHeadCart.setDescription(null);
        growDayHeadCart.setData(dayHeadData);
        avgHeadTxt.setText(avgHead+" cm");

        //체온 차트 속성
        XAxis feverXAxis = growDayFeverCart.getXAxis(); // x 축 설정
        feverXAxis.setPosition(XAxis.XAxisPosition.TOP); //x 축 표시에 대한 위치 설정
        feverXAxis.setLabelCount(7, true); //X축의 데이터를 최대 몇개 까지 나타낼지에 대한 설정 5개 force가 true 이면 반드시 보여줌

        YAxis feverYAxisLeft = growDayFeverCart.getAxisLeft(); //Y축의 왼쪽면 설정
        feverYAxisLeft.setDrawLabels(false);
        feverYAxisLeft.setDrawAxisLine(false);
        feverYAxisLeft.setDrawGridLines(false);
        YAxis feverYAxisRight = growDayFeverCart.getAxisRight(); //Y축의 오른쪽면 설정
        feverYAxisRight.setLabelCount(4, true);


        growDayFeverCart.setDescription(null);
        growDayFeverCart.setData(dayFeverData);
        avgFeverTxt.setText(avgFever+" °C");

        return view;
    }

}
