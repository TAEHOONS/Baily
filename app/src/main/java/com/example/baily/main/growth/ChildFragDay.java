package com.example.baily.main.growth;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import androidx.fragment.app.FragmentTransaction;

import com.example.baily.DBlink;
import com.example.baily.R;
import com.example.baily.caldate;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class ChildFragDay extends Fragment {

    private DBlink helper;
    private SQLiteDatabase db;
    private View view;
    private Activity activity;
    private LineChart growDayKgCart, growDayCmCart, growDayHeadCart, growDayFeverCart;
    TextView avgKgTxt, avgCmTxt, avgHeadTxt, avgFeverTxt, dayDateTxt;
    Boolean btnCk;
    Boolean abBtn;
    float avgWeight, avgHeight, avgHead, avgFever, kgSum = 0, cmSum = 0, headSum = 0, feverSum = 0;

    ImageView beforeBtn, afterBtn;
    String dayStartDate, dayEndDate, today;
    Calendar cal, plusCal;
    Date date = new Date();
    SimpleDateFormat sFormat, simpleDate, readChartD;

    int dStart, dEnd, dBeStart, dBeEnd, dAfStart, dAfEnd, dbVersion = 3;
    String dbName = "user.db", dayStart, dayEnd, mId, mBabyname;
    String[] SearchDay, mArrKg, mArrCm, mArrHead, mArrFever;

    LineData dayKgData, dayCmData, dayHeadData, dayFeverData, AvgLineData;

    //차트에 들어가는 값,, 도저히 모르겠습니당..
    ArrayList<Entry> kgValues, cmValues, headValues, feverValues;
    ArrayList<String> XariDay;
    ArrayList<ILineDataSet> dayKgDataSets, dayCmDataSets, dayHeadDataSets, dayFeverDataSets, AvgDataSets;

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
        readChartD = new SimpleDateFormat("yyyy년 MM월 dd일");
        sFormat = new SimpleDateFormat("MM월 dd일");
        today = sFormat.format(date); //오늘날짜

        cal = Calendar.getInstance();
        plusCal = Calendar.getInstance();
        usingDB(container);

        SearchDay = new String[7];
        mArrKg = new String[7];
        mArrCm = new String[7];
        mArrHead = new String[7];
        mArrFever = new String[7];

        cal.setTime(date);
        cal.add(Calendar.DATE, -6);
        dayStartDate = sFormat.format(cal.getTime());

        plusCal.setTime(date);
        //plusCal.add(Calendar.DATE, +6);
        dayEndDate = sFormat.format(plusCal.getTime());
        dayDateTxt.setText(dayStartDate + " ~ " + dayEndDate);

        btnCk = false;
        abBtn = false;

        dStart = Integer.parseInt(simpleDate.format(cal.getTime()));
        dEnd = Integer.parseInt(simpleDate.format(plusCal.getTime()));
        dayStart = sFormat.format(cal.getTime());
        dayEnd = sFormat.format(plusCal.getTime());

        kgValues = new ArrayList<>();
        cmValues = new ArrayList<>();
        headValues = new ArrayList<>();
        feverValues = new ArrayList<>();


        // 값 셋팅하기
        SetGraphData();

        // 그래프 평균값 글자넣기
        avgKgTxt.setText(String.format("%.2f", avgWeight) + " kg");
        avgCmTxt.setText(String.format("%.2f", avgHeight) + " cm");
        avgHeadTxt.setText(String.format("%.2f", avgHead) + " cm");
        avgFeverTxt.setText(String.format("%.2f", avgFever) + " °C");


        //  중간 업데이트
        MidDataSet();

        //몸무게 차트 속성
        setGraph(growDayKgCart, dayKgData);

        //신장 차트 속성
        setGraph(growDayCmCart, dayCmData);

        //머리둘레 차트 속성
        setGraph(growDayHeadCart, dayHeadData);

        //체온 차트 속성
        setGraph(growDayFeverCart, dayFeverData);

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
                dayStart = sFormat.format(cal.getTime());
                dayEnd = sFormat.format(plusCal.getTime());


                btnCk = true;
                abBtn = false;
                SetGraphData();
                //  중간 업데이트
                MidDataSet();

                // 차트 속성
                setGraph(growDayKgCart, dayKgData);
                setGraph(growDayCmCart, dayCmData);
                setGraph(growDayHeadCart, dayHeadData);
                setGraph(growDayFeverCart, dayFeverData);

                // 바뀐 차트 적용
                ChartChange(growDayKgCart);
                ChartChange(growDayCmCart);
                ChartChange(growDayHeadCart);
                ChartChange(growDayFeverCart);
            }
        });
        //이후버튼 눌렀을 때
        afterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //2000.00.00~마지막 날짜, 마지막날짜가 오늘날짜이면 더이상 넘어갈수 없게
                Log.d("todayset", "today: " + today);
                Log.d("todayset", "dayEndDate: " + dayEndDate);
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
                    dayStart = sFormat.format(cal.getTime());
                    dayEnd = sFormat.format(plusCal.getTime());

                    btnCk = true;
                    abBtn = true;


                    SetGraphData();
                    //  중간 업데이트
                    MidDataSet();

                    // 차트 속성
                    setGraph(growDayKgCart, dayKgData);
                    setGraph(growDayCmCart, dayCmData);
                    setGraph(growDayHeadCart, dayHeadData);
                    setGraph(growDayFeverCart, dayFeverData);

                    // 바뀐 차트 적용
                    ChartChange(growDayKgCart);
                    ChartChange(growDayCmCart);
                    ChartChange(growDayHeadCart);
                    ChartChange(growDayFeverCart);

                }
            }
        });

        return view;
    }

    private void MidDataSet() {

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

        // day"++"DataSets에 linedata 받은거 추가하기 + 평균줄 추가
        dayKgDataSets.add(dayKg);
        dayKgDataSets.add(AvgData(avgWeight));
        dayCmDataSets.add(dayCm);
        dayCmDataSets.add(AvgData(avgHeight));
        dayHeadDataSets.add(dayHead);
        dayHeadDataSets.add(AvgData(avgHead));
        dayFeverDataSets.add(dayFever);
        dayFeverDataSets.add(AvgData(avgFever));

        // 실질적 라인인 day"++"Data에 새로 값넣기
        dayKgData = new LineData(dayKgDataSets);
        dayCmData = new LineData(dayCmDataSets);
        dayHeadData = new LineData(dayHeadDataSets);
        dayFeverData = new LineData(dayFeverDataSets);


        // 그래프 색 넣기
        GraphLineColor(dayKg, Color.BLACK);
        GraphLineColor(dayCm, Color.RED);
        GraphLineColor(dayHead, Color.BLUE);
        GraphLineColor(dayFever, Color.GREEN);
    }


    // 그래프에 데이터 적용 셋팅
    private void setGraph(LineChart growDayCart, LineData dayData) {


        XAxis headXAxis = growDayCart.getXAxis(); // x 축 설정
        headXAxis.setPosition(XAxis.XAxisPosition.TOP); //x 축 표시에 대한 위치 설정
        headXAxis.setLabelCount(7, true); //X축의 데이터를 최대 몇개 까지 나타낼지에 대한 설정 5개 force가 true 이면 반드시 보여줌
        //headXAxis.setDrawAxisLine(false);
        //headXAxis.setDrawGridLines(false);

        // 차트 x 에 라벨 넣기
        headXAxis.setValueFormatter(new IndexAxisValueFormatter(XariDay));

        YAxis headYAxisLeft = growDayCart.getAxisLeft(); //Y축의 왼쪽면 설정

        headYAxisLeft.setDrawLabels(false);     // 왼쪽에 표현되는 글자나 선 들 지우기
        //headYAxisLeft.setDrawAxisLine(false);
        headYAxisLeft.setDrawGridLines(false);


        YAxis headYAxisRight = growDayCart.getAxisRight(); //Y축의 오른쪽면 설정

        headYAxisRight.setLabelCount(4, true);
        growDayCart.setDescription(null);

        growDayCart.setData(dayData);
    }

    // 그래프 컬러 적용
    private void GraphLineColor(LineDataSet line, int color) {
        line.setColor(color);
        line.setCircleColor(color);

    }

    // 그래프 데이터 넣기용
    private void SetGraphData() {
        // 그래프 평균값 글자넣기
        avgKgTxt.setText(String.format("%.2f", avgWeight) + " kg");
        avgCmTxt.setText(String.format("%.2f", avgHeight) + " cm");
        avgHeadTxt.setText(String.format("%.2f", avgHead) + " cm");
        avgFeverTxt.setText(String.format("%.2f", avgFever) + " °C");

        kgValues.clear();
        cmValues.clear();
        headValues.clear();
        feverValues.clear();

        XariDay = getDate();
        if (btnCk == true) {
            if (abBtn == true) {
                avgWeight = dataStack(dAfStart, mArrKg, kgSum, kgValues, avgWeight);
                avgHeight = dataStack(dAfStart, mArrCm, cmSum, cmValues, avgHeight);
                avgHead = dataStack(dAfStart, mArrHead, headSum, headValues, avgHead);
                avgFever = dataStack(dAfStart, mArrFever, feverSum, feverValues, avgFever);

            } else {
                avgWeight = dataStack(dBeStart, mArrKg, kgSum, kgValues, avgWeight);
                avgHeight = dataStack(dBeStart, mArrCm, cmSum, cmValues, avgHeight);
                avgHead = dataStack(dBeStart, mArrHead, headSum, headValues, avgHead);
                avgFever = dataStack(dBeStart, mArrFever, feverSum, feverValues, avgFever);
            }
        } else {
            avgWeight = dataStack(dStart, mArrKg, kgSum, kgValues, avgWeight);
            avgHeight = dataStack(dStart, mArrCm, cmSum, cmValues, avgHeight);
            avgHead = dataStack(dStart, mArrHead, headSum, headValues, avgHead);
            avgFever = dataStack(dStart, mArrFever, feverSum, feverValues, avgFever);
        }
    }

    private float dataStack(int start, String[] end, float sum, ArrayList<Entry> values, float avg) {
        int count = 0;
        float val;
        for (int i = 0; i <= 6; i++) {
            if (end[i]!=null) {
                try {
                    val = Float.parseFloat(end[i].trim());
                    sum = sum + val;
                    values.add(new Entry(i, val));
                    count += 1;
                }catch (Exception e){
                }
            }

        }
        avg = 0;


        avg = sum / count;
        return avg;
    }

    // chart X좌표 글자 넣기
    public ArrayList<String> getDate() {
        String read;
        ArrayList<String> label = new ArrayList<>();

        read = sFormat.format((cal.getTime()));
        SearchDay[0] = readChartD.format((cal.getTime()));
        Log.d("readdate", "read: " + read);
        label.add(read.substring(4, 7));
        for (int i = 0; i <= 5; i++) {
            cal.add(Calendar.DATE, +1);
            read = sFormat.format((cal.getTime()));
            SearchDay[i + 1] = readChartD.format((cal.getTime()));
            Log.d("readdate", "read: " + read);
            label.add(read.substring(4, 7));

        }
        cal.add(Calendar.DATE, -6);

        getDBdata();

        //for (int i = 0; i <=6; i++)
        //    label.add(yourList.get(i).getDateValue());
        return label;
    }

    // 차트 변경 적용
    private void ChartChange(LineChart chart) {
        chart.notifyDataSetChanged();
        chart.invalidate();
    }

    // DB 연결
    private void usingDB(ViewGroup container) {
        helper = new DBlink(container.getContext(), dbName, null, dbVersion);
        db = helper.getWritableDatabase();

        String sql = "select * from thisusing where _id=1"; // 검색용
        Cursor cursor = db.rawQuery(sql, null);

        // 기본 데이터
        while (cursor.moveToNext()) {
            mId = cursor.getString(1);
            mBabyname = cursor.getString(2);
            Log.d("Home", "db받기 id = " + mId + "  현재 아기 = " + mBabyname);
        }

    }

    // 현재값 받기
    private void getDBdata() {
        Log.d("searchDay", "start ");
        Arrays.fill(mArrKg,null);
        Arrays.fill(mArrCm,null);
        Arrays.fill(mArrHead,null);
        Arrays.fill(mArrFever,null);

        // 현재 사용 아기데이터
        for (int i = 0; i <= 6; i++) {
            String sql = "select * from growlog where name='" + mBabyname + "'AND date='" + SearchDay[i] + "'"; // 검색용
            Cursor c = db.rawQuery(sql, null);
            while (c.moveToNext()) {
                //SearchDay[i] = c.getString(3);
                mArrKg[i] = c.getString(2);
                mArrCm[i] = c.getString(3);
                mArrHead[i] = c.getString(4);
                mArrFever[i] = c.getString(5);


                Log.d("searchDay", "SearchDay = " + SearchDay[i] + " ,mArrKg = "
                        + mArrKg[i] + "   ,mArrCm = " + mArrCm[i] + "   ,mArrHead = " + mArrHead[i] + "   ,mArrFever = " + mArrFever[i]);
            }
        }

    }

    private LineDataSet AvgData(float avgData) {
        LineDataSet avgDataSet;
        ArrayList<Entry> avgValues = new ArrayList<>();

        for (int i = 0; i <= 6; i++) {
            avgValues.add(new Entry(i, avgData));
        }

        avgDataSet = new LineDataSet(avgValues, null);

        avgDataSet.setDrawValues(false);//데이터 값 없애기
        avgDataSet.setDrawCircles(false);//포인트 원 없애기
        // 그래프 색 넣기
        GraphLineColor(avgDataSet, Color.argb(0, 0, 0, 0));

        return avgDataSet;
    }

}
