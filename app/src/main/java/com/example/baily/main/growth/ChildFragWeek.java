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
import androidx.fragment.app.Fragment;

import com.example.baily.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChildFragWeek extends Fragment {
    private View view;
    private LineChart growWeekKgCart, growWeekCmCart, growWeekHeadCart, growWeekFeverCart;
    TextView weekAvgKgTxt, weekAvgCmTxt, weekAvgHeadTxt, weekAvgFeverTxt, weekDateTxt;
    float weekAvgWeight, weekAvgHeight, weekAvgHead, weekAvgFever, wKgSum = 0, wCmSum = 0, wHeadSum = 0, wFeverSum = 0;
    Boolean btnCk;//버튼을 눌렀는가 체크
    Boolean abBtn;//이전버튼, 이후버튼 구분하기위한 체크

    ImageView weekBeforeBtn, weekAfterBtn;
    String weekStartDate, wToday;
    Calendar wCal;
    Date date = new Date();
    SimpleDateFormat sFormat, wSimple, readChartD;
    //마지막일, 한 주의 데이터 평균값을 구하기 위해 해당 달의 마지막 날짜를 알아야함
    int maxDay,bMaxDay,aMaxDay;

    LineData wKgData, wCmData, wHeadData, wFeverData;

    ArrayList<Entry> kgValues, cmValues, headValues, feverValues;
    ArrayList<String> XarWeek;
    ArrayList<ILineDataSet> wKgDataSets, wCmDataSets, wHeadDataSets, wFeverDataSets;

    public static ChildFragWeek newInstance() {
        ChildFragWeek childFragWeek = new ChildFragWeek();
        return childFragWeek;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.growth_child_frag_week, container, false);
        growWeekKgCart = view.findViewById(R.id.growthWeekKgLineCart);
        growWeekCmCart = view.findViewById(R.id.growthWeekCmLineCart);
        growWeekHeadCart = view.findViewById(R.id.growthWeekHeadLineCart);
        growWeekFeverCart = view.findViewById(R.id.growthWeekFeverLineCart);
        weekAvgKgTxt = view.findViewById(R.id.weekAvgKgTxt);
        weekAvgCmTxt = view.findViewById(R.id.weekAvgCmTxt);
        weekAvgHeadTxt = view.findViewById(R.id.weekAvgHeadTxt);
        weekAvgFeverTxt = view.findViewById(R.id.weekAvgFeverTxt);
        weekDateTxt = view.findViewById(R.id.weekDateTxt);
        weekBeforeBtn = (ImageView) view.findViewById(R.id.weekBeforeBtn);
        weekAfterBtn = (ImageView) view.findViewById(R.id.weekAfterBtn);

        //차트구간을 위해 , N주차
        wSimple = new SimpleDateFormat("W");
        sFormat = new SimpleDateFormat("yy년 MM월");
        wToday = sFormat.format(date); //이번달

        wCal = Calendar.getInstance();
        wCal.setTime(date);
        weekStartDate = sFormat.format(wCal.getTime());
        weekDateTxt.setText(weekStartDate);

        btnCk = false;
        abBtn = false;

        //해당 달의 마지막 날
        maxDay = wCal.getActualMaximum(Calendar.DATE);

        kgValues = new ArrayList<>();
        cmValues = new ArrayList<>();
        headValues = new ArrayList<>();
        feverValues = new ArrayList<>();

        // 값 셋팅하기
        SetGraphData();

        // 그래프 평균값 글자넣기
        weekAvgKgTxt.setText(String.format("%.2f",weekAvgWeight) + " kg");
        weekAvgCmTxt.setText(String.format("%.2f",weekAvgHeight) + " cm");
        weekAvgHeadTxt.setText(String.format("%.2f",weekAvgHead) + " cm");
        weekAvgFeverTxt.setText(String.format("%.2f",weekAvgFever) + " °C");

        //  중간 업데이트
        MidDataSet();

        //몸무게 차트 속성
        setGraph(growWeekKgCart, wKgData);

        //신장 차트 속성
        setGraph(growWeekCmCart, wCmData);

        //머리둘레 차트 속성
        setGraph(growWeekHeadCart, wHeadData);

        //체온 차트 속성
        setGraph(growWeekFeverCart, wFeverData);

        //이전 버튼 눌렀을 때
        weekBeforeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wCal.add(Calendar.MONTH, -1);
                weekStartDate = sFormat.format(wCal.getTime());
                weekDateTxt.setText(weekStartDate);

                bMaxDay = wCal.getActualMaximum(Calendar.DATE); //현재 달의 마지막날
                Log.d("이전 달의 마지막날", "이번달의 마지막 날은?==============" + bMaxDay);

                btnCk = true;
                abBtn = false;

                SetGraphData();
                //  중간 업데이트
                MidDataSet();

                // 차트 속성
                setGraph(growWeekKgCart, wKgData);
                setGraph(growWeekCmCart, wCmData);
                setGraph(growWeekHeadCart, wHeadData);
                setGraph(growWeekFeverCart, wFeverData);

                // 바뀐 차트 적용
                ChartChange(growWeekKgCart);
                ChartChange(growWeekCmCart);
                ChartChange(growWeekHeadCart);
                ChartChange(growWeekFeverCart);

            }
        });
        //이후 버튼 눌렀을 때
        weekAfterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //보여지는 달이 이번달이면 다음 달로 넘어갈수 없게
                if (wToday.equals(weekStartDate)) {
                    Toast.makeText(getActivity(), "다음 달 기록이 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    wCal.add(Calendar.MONTH, +1);
                    weekStartDate = sFormat.format(wCal.getTime());
                    weekDateTxt.setText(weekStartDate);
                    aMaxDay = wCal.getActualMaximum(Calendar.DATE); //현재 달의 마지막날
                    Log.d("이후 달의 마지막날", "이번달의 마지막 날은?==============" + aMaxDay);

                    btnCk = true;
                    abBtn = true;

                    SetGraphData();
                    //  중간 업데이트
                    MidDataSet();

                    // 차트 속성
                    setGraph(growWeekKgCart, wKgData);
                    setGraph(growWeekCmCart, wCmData);
                    setGraph(growWeekHeadCart, wHeadData);
                    setGraph(growWeekFeverCart, wFeverData);

                    // 바뀐 차트 적용
                    ChartChange(growWeekKgCart);
                    ChartChange(growWeekCmCart);
                    ChartChange(growWeekHeadCart);
                    ChartChange(growWeekFeverCart);
                }
            }
        });

        return view;
    }

    private void MidDataSet(){

        LineDataSet wKg, wCm, wHead, wFever;

        wKg = new LineDataSet(kgValues, "몸무게");
        wCm = new LineDataSet(cmValues, "신장");
        wHead = new LineDataSet(headValues, "머리둘레");
        wFever = new LineDataSet(feverValues, "체온");

        // 기초 라인들 만들기
        wKgDataSets = new ArrayList<>();
        wCmDataSets = new ArrayList<>();
        wHeadDataSets = new ArrayList<>();
        wFeverDataSets = new ArrayList<>();

        // day"++"DataSets에 linedata 받은거 추가하기
        wKgDataSets.add(wKg);
        wCmDataSets.add(wCm);
        wHeadDataSets.add(wHead);
        wFeverDataSets.add(wFever);

        // 실질적 라인인 day"++"Data에 새로 값넣기
        wKgData = new LineData(wKgDataSets);
        wCmData = new LineData(wCmDataSets);
        wHeadData = new LineData(wHeadDataSets);
        wFeverData = new LineData(wFeverDataSets);


        // 그래프 색 넣기
        GraphLineColor(wKg, Color.BLACK);
        GraphLineColor(wCm, Color.RED);
        GraphLineColor(wHead, Color.BLUE);
        GraphLineColor(wFever, Color.GREEN);
    }

    // 그래프에 데이터 적용 셋팅
    private void setGraph(LineChart growWeekCart, LineData weekData) {

        XAxis wXAxis = growWeekCart.getXAxis(); // x 축 설정
        wXAxis.setPosition(XAxis.XAxisPosition.TOP); //x 축 표시에 대한 위치 설정
        wXAxis.setLabelCount(5, true); //X축의 데이터를 최대 몇개 까지 나타낼지에 대한 설정 5개 force가 true 이면 반드시 보여줌
        wXAxis.setDrawAxisLine(false);
        // 차트 x 에 라벨 넣기
        wXAxis.setValueFormatter(new IndexAxisValueFormatter(XarWeek));

        YAxis wYAxisLeft = growWeekCart.getAxisLeft(); //Y축의 왼쪽면 설정

        wYAxisLeft.setDrawLabels(false);
        wYAxisLeft.setDrawAxisLine(false);
        wYAxisLeft.setDrawGridLines(false);

        YAxis wYAxisRight = growWeekCart.getAxisRight(); //Y축의 오른쪽면 설정

        wYAxisRight.setLabelCount(4, true);

        growWeekCart.setDescription(null);
        growWeekCart.setData(weekData);

    }

    // 그래프 컬러 적용
    private void GraphLineColor(LineDataSet line, int color) {
        line.setColor(color);
        line.setCircleColor(color);
    }

    // 그래프 데이터 넣기용
    private void SetGraphData() {
        // 그래프 평균값 글자넣기
        weekAvgKgTxt.setText(String.format("%.2f",weekAvgWeight) + " kg");
        weekAvgCmTxt.setText(String.format("%.2f",weekAvgHeight) + " cm");
        weekAvgHeadTxt.setText(String.format("%.2f",weekAvgHead) + " cm");
        weekAvgFeverTxt.setText(String.format("%.2f",weekAvgFever) + " °C");

        kgValues.clear();
        cmValues.clear();
        headValues.clear();
        feverValues.clear();

        XarWeek=getDate();
        weekAvgWeight = dataStack(wKgSum, kgValues, weekAvgWeight);
        weekAvgHeight = dataStack(wCmSum, cmValues, weekAvgHeight);
        weekAvgHead = dataStack(wHeadSum, headValues, weekAvgHead);
        weekAvgFever = dataStack(wFeverSum, feverValues, weekAvgFever);
    }


    private float dataStack(float sum, ArrayList<Entry> values, float avg) {
        for (int i = 1; i <= 5; i++) {
            float val = (float) (Math.random() * 10);
            sum = sum + val;
            values.add(new Entry(i, val));
            avg = sum / i;
        }
        return avg;
    }

    // chart X좌표 글자 넣기
    public ArrayList<String> getDate() {
        String read;
        ArrayList<String> label = new ArrayList<>();
        for(int i=0;i<=5; i++){
            label.add(i+"주");
        }
        return label;
    }


    // 차트 변경 적용
    private void ChartChange(LineChart chart){
        chart.notifyDataSetChanged();
        chart.invalidate();
    }
}
