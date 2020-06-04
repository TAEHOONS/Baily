package com.example.baily.main.growth;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Binder;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputBinding;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
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

public class ChildFragDay extends Fragment {
    private View view;
    private Activity activity;
    private LineChart growDayKgCart, growDayCmCart, growDayHeadCart, growDayFeverCart;
    TextView avgKgTxt, avgCmTxt, avgHeadTxt, avgFeverTxt, dayDateTxt;
    Boolean btnCk;
    Boolean abBtn;
    float avgWeight, avgHeight, avgHead, avgFever;
    float kgSum = 0;
    float cmSum = 0;
    float headSum = 0;
    float feverSum = 0;
    ImageView beforeBtn, afterBtn;
    String dayStartDate, dayEndDate, today;
    Calendar cal, plusCal;
    Date date = new Date();
    SimpleDateFormat sFormat, simpleDate;
    int dStart, dEnd, dBeStart,dBeEnd,dAfStart,dAfEnd;

    LineData dayKgData,dayCmData,dayHeadData,dayFeverData;

    //차트에 들어가는 값,, 도저히 모르겠습니당..
    ArrayList<Entry> kgValues,cmValues,headValues,feverValues;

    ArrayList<ILineDataSet> dayKgDataSets,dayCmDataSets,dayHeadDataSets,dayFeverDataSets;

    public static ChildFragDay newInstance() {
        ChildFragDay childFragDay = new ChildFragDay();
        return childFragDay;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.growth_child_frag_day, container, false);
        growDayKgCart = view.findViewById(R.id.growthDayKgLineCart);
        growDayCmCart = view.findViewById(R.id.growthDayCmLineCart);
        growDayHeadCart = view.findViewById(R.id.growthDayHeadLineCart);
        growDayFeverCart = view.findViewById(R.id.growthDayFeverLineCart);
        avgKgTxt = view.findViewById(R.id.avgKgTxt);
        avgCmTxt = view.findViewById(R.id.avgCmTxt);
        avgHeadTxt = view.findViewById(R.id.avgHeadTxt);
        avgFeverTxt = view.findViewById(R.id.avgFeverTxt);
        dayDateTxt = view.findViewById(R.id.dayDateTxt);
        beforeBtn = (ImageView) view.findViewById(R.id.beforeBtn);
        afterBtn = (ImageView) view.findViewById(R.id.afterBtn);

        //차트 구간설정을 위한
        simpleDate = new SimpleDateFormat("dd");

        sFormat = new SimpleDateFormat("MM월 dd일");
        today = sFormat.format(date); //오늘날짜

        cal = Calendar.getInstance();
        plusCal = Calendar.getInstance();

        cal.setTime(date);
        //cal.add(Calendar.DATE, -6);
        dayStartDate = sFormat.format(cal.getTime());

        plusCal.setTime(date);
        plusCal.add(Calendar.DATE, +6);
        dayEndDate = sFormat.format(plusCal.getTime());
        dayDateTxt.setText(dayStartDate + " ~ " + dayEndDate);

        btnCk = false;
        abBtn = false;

        dStart = Integer.parseInt(simpleDate.format(cal.getTime()));
        dEnd = Integer.parseInt(simpleDate.format(plusCal.getTime()));

        kgValues = new ArrayList<>();
        cmValues = new ArrayList<>();
        headValues = new ArrayList<>();
        feverValues = new ArrayList<>();

        //이전버튼 눌렀을 때
        beforeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cal.add(Calendar.DATE, -6);
                dayStartDate = sFormat.format(cal.getTime());

                plusCal.add(Calendar.DATE, -6);
                dayEndDate = sFormat.format(plusCal.getTime());
                dayDateTxt.setText(dayStartDate + " ~ " + dayEndDate);

                dBeStart = Integer.parseInt(simpleDate.format(cal.getTime()));
                dBeEnd = Integer.parseInt(simpleDate.format(plusCal.getTime()));

                btnCk = true;
                abBtn = false;

            }
        });
        //이후버튼 눌렀을 때
        afterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //2000.00.00~마지막 날짜, 마지막날짜가 오늘날짜이면 더이상 넘어갈수 없게
                if (today.equals(dayEndDate)) {
                    Toast.makeText(getActivity(), "오늘이후 기록이 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    cal.add(Calendar.DATE, +6);
                    dayStartDate = sFormat.format(cal.getTime());

                    plusCal.add(Calendar.DATE, +6);
                    dayEndDate = sFormat.format(plusCal.getTime());
                    dayDateTxt.setText(dayStartDate + " ~ " + dayEndDate);

                    dAfStart = Integer.parseInt(simpleDate.format(cal.getTime()));
                    dAfEnd = Integer.parseInt(simpleDate.format(plusCal.getTime()));

                    btnCk = true;
                    abBtn = true;
                }
            }
        });



        // 값 셋팅하기
        SetGraphData();

        // 그래프 평균값 글자넣기
        avgKgTxt.setText(avgWeight + " kg");
        avgCmTxt.setText(avgHeight + " cm");
        avgHeadTxt.setText(avgHead + " cm");
        avgFeverTxt.setText(avgFever + " °C");

        LineDataSet dayKg, dayCm, dayHead, dayFever;

        dayKg = new LineDataSet(kgValues, "몸무게");
        dayCm = new LineDataSet(cmValues, "신장");
        dayHead = new LineDataSet(headValues, "머리둘레");
        dayFever = new LineDataSet(feverValues, "체온");

        // 기초 라인들 만들기
        dayKgDataSets = new ArrayList<>();
        dayCmDataSets = new ArrayList<>();
        dayHeadDataSets = new ArrayList<>();
        dayFeverDataSets = new ArrayList<>();

            // day"++"DataSets에 linedata 받은거 추가하기
        dayKgDataSets.add(dayKg);
        dayCmDataSets.add(dayCm);
        dayHeadDataSets.add(dayHead);
        dayFeverDataSets.add(dayFever);

             // 실질적 라인인 day"++"Data에 새로 값넣기
        dayKgData = new LineData(dayKgDataSets);
        dayCmData = new LineData(dayCmDataSets);
        dayHeadData = new LineData(dayHeadDataSets);
        dayFeverData = new LineData(dayFeverDataSets);


        // 그래프 색 넣기
        GraphLineColor(dayKg,Color.BLACK);
        GraphLineColor(dayCm,Color.RED);
        GraphLineColor(dayHead,Color.BLUE);
        GraphLineColor(dayFever,Color.GREEN);


        //몸무게 차트 속성
        setGraph(growDayKgCart,dayKgData);

        //신장 차트 속성
        setGraph(growDayCmCart,dayCmData);

        //머리둘레 차트 속성
        setGraph(growDayHeadCart,dayHeadData);

        //체온 차트 속성
        setGraph(growDayFeverCart,dayFeverData);

        return view;
    }


    // 그래프에 데이터 적용 셋팅
    private void setGraph(LineChart growDayCart,LineData dayData){
        XAxis headXAxis = growDayCart.getXAxis(); // x 축 설정
        headXAxis.setPosition(XAxis.XAxisPosition.TOP); //x 축 표시에 대한 위치 설정
        headXAxis.setLabelCount(7, true); //X축의 데이터를 최대 몇개 까지 나타낼지에 대한 설정 5개 force가 true 이면 반드시 보여줌

        YAxis headYAxisLeft = growDayCart.getAxisLeft(); //Y축의 왼쪽면 설정
        headYAxisLeft.setDrawLabels(false);
        headYAxisLeft.setDrawAxisLine(false);
        headYAxisLeft.setDrawGridLines(false);
        YAxis headYAxisRight = growDayCart.getAxisRight(); //Y축의 오른쪽면 설정
        headYAxisRight.setLabelCount(4, true);

        growDayCart.setDescription(null);
        growDayCart.setData(dayData);

    }

    // 그래프 컬러 적용
    private void GraphLineColor(LineDataSet line,int color){
        line.setColor(color);
        line.setCircleColor(color);
    }

    // 그래프 데이터 넣기용
    private void SetGraphData(){
        if(btnCk==true){
            if(abBtn==true){


                for (int i = dAfStart; i <= dAfEnd; i++) {
                    float val = (float) (Math.random() * 10);
                    kgSum = kgSum + val;
                    kgValues.add(new Entry(i, val));
                    avgWeight = kgSum / i;
                }

                for (int i = dAfStart; i <= dAfEnd; i++) {
                    float val = (float) (Math.random() * 10);
                    cmSum = cmSum + val;
                    cmValues.add(new Entry(i, val));
                    avgHeight = cmSum / i;
                }
                for (int i = dAfStart; i <= dAfEnd; i++) {
                    float val = (float) (Math.random() * 10);
                    headSum = headSum + val;
                    headValues.add(new Entry(i, val));
                    avgHead = headSum / i;
                }
                for (int i = dAfStart; i <= dAfEnd; i++) {
                    float val = (float) (Math.random() * 10);
                    float sVal = (float) 36.5;
                    feverSum = feverSum + sVal;
                    feverValues.add(new Entry(i, sVal));
                    avgFever = feverSum / i;
                }

            }else{

                for (int i = dBeStart; i <= dBeEnd; i++) {
                    float val = (float) (Math.random() * 10);
                    kgSum = kgSum + val;
                    kgValues.add(new Entry(i, val));
                    avgWeight = kgSum / i;
                }

                for (int i = dBeStart; i <= dBeEnd; i++) {
                    float val = (float) (Math.random() * 10);
                    cmSum = cmSum + val;
                    cmValues.add(new Entry(i, val));
                    avgHeight = cmSum / i;
                }
                for (int i = dBeStart; i <= dBeEnd; i++) {
                    float val = (float) (Math.random() * 10);
                    headSum = headSum + val;
                    headValues.add(new Entry(i, val));
                    avgHead = headSum / i;
                }
                for (int i = dBeStart; i <= dBeEnd; i++) {
                    float val = (float) (Math.random() * 10);
                    float sVal = (float) 36.5;
                    feverSum = feverSum + sVal;
                    feverValues.add(new Entry(i, sVal));
                    avgFever = feverSum / i;
                }
            }
        }else{

            for (int i = dStart; i <= dEnd; i++) {
                float val = (float) (Math.random() * 10);
                kgSum = kgSum + val;
                kgValues.add(new Entry(i, val));
                avgWeight = kgSum / i;
            }

            for (int i = dStart; i <= dEnd; i++) {
                float val = (float) (Math.random() * 10);
                cmSum = cmSum + val;
                cmValues.add(new Entry(i, val));
                avgHeight = cmSum / i;
            }
            for (int i = dStart; i <= dEnd; i++) {
                float val = (float) (Math.random() * 10);
                headSum = headSum + val;
                headValues.add(new Entry(i, val));
                avgHead = headSum / i;
            }
            for (int i = dStart; i <= dEnd; i++) {
                float val = (float) (Math.random() * 10);
                float sVal = (float) 36.5;
                feverSum = feverSum + sVal;
                feverValues.add(new Entry(i, sVal));
                avgFever = feverSum / i;
            }
        }
    }


}
