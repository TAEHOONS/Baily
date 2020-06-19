package com.example.baily.main.growth;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.baily.DBlink;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class ChildFragWeek extends Fragment {

    private DBlink helper;
    private SQLiteDatabase db;
    private View view;
    private LineChart growWeekKgCart, growWeekCmCart, growWeekHeadCart, growWeekFeverCart;
    private SwipeRefreshLayout wSwipeLayout;
    TextView weekAvgKgTxt, weekAvgCmTxt, weekAvgHeadTxt, weekAvgFeverTxt, weekDateTxt;
    float weekAvgWeight, weekAvgHeight, weekAvgHead, weekAvgFever, wKgSum = 0, wCmSum = 0, wHeadSum = 0, wFeverSum = 0;
    Boolean btnCk;//버튼을 눌렀는가 체크
    Boolean abBtn;//이전버튼, 이후버튼 구분하기위한 체크

    ImageView weekBeforeBtn, weekAfterBtn;
    String weekStartDate, wToday;
    Calendar wCal, cal;
    Date date = new Date();
    SimpleDateFormat sFormat, wSimple, readChartD;
    int maxDay, bMaxDay, aMaxDay, mMinDay, mMaxDay;//마지막일

    int dStart, dEnd, dBeStart, dBeEnd, dAfStart, dAfEnd, dbVersion = 3;
    String dbName = "user.db", dayStart, dayEnd, mId, mBabyname;
    String[] SearchDay, mArrKg, mArrCm, mArrHead, mArrFever;
    float[] weekArrKg, weekArrCm, weekArrHead, weekArrFever;
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
        wSwipeLayout = view.findViewById(R.id.wSwipeLayout);
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
        readChartD = new SimpleDateFormat("yyyy년 MM월 dd일");
        wToday = sFormat.format(date); //이번달

        wCal = Calendar.getInstance();
        cal = Calendar.getInstance();
        cal.setTime(date);
        mMaxDay = cal.getActualMaximum(Calendar.DATE);
        Log.d("주간 학습", " maxDay = " + mMaxDay);
        wCal.set(wCal.get(Calendar.YEAR), Calendar.DAY_OF_MONTH, wCal.getActualMinimum(Calendar.DATE));

        usingDB(container);

        SearchDay = new String[33];
        mArrKg = new String[33];
        mArrCm = new String[33];
        mArrHead = new String[33];
        mArrFever = new String[33];
        weekArrKg = new float[7];
        weekArrCm = new float[7];
        weekArrHead = new float[7];
        weekArrFever = new float[7];

        weekString();

        wCal.setTime(date);
        weekStartDate = sFormat.format(wCal.getTime());
        weekDateTxt.setText(weekStartDate);

        btnCk = false;
        abBtn = false;
        getDBdata();
        //해당 달의 마지막 날
        maxDay = wCal.getActualMaximum(Calendar.DATE);

        kgValues = new ArrayList<>();
        cmValues = new ArrayList<>();
        headValues = new ArrayList<>();
        feverValues = new ArrayList<>();

        // 값 셋팅하기
        SetGraphData();

        //  중간 업데이트
        MidDataSet();

        // 그래프 평균값 글자넣기
        weekAvgKgTxt.setText(String.format("%.2f", weekAvgWeight) + " kg");
        weekAvgCmTxt.setText(String.format("%.2f", weekAvgHeight) + " cm");
        weekAvgHeadTxt.setText(String.format("%.2f", weekAvgHead) + " cm");
        weekAvgFeverTxt.setText(String.format("%.2f", weekAvgFever) + " °C");

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
                ArryClear(mArrKg, mArrCm, mArrHead, mArrFever,weekArrKg, weekArrCm, weekArrHead, weekArrFever);

                wCal.add(Calendar.MONTH, -1);
                weekStartDate = sFormat.format(wCal.getTime());
                weekDateTxt.setText(weekStartDate);

                bMaxDay = wCal.getActualMaximum(Calendar.DATE); //현재 달의 마지막날
                mMaxDay = bMaxDay;


                Log.d("이전 달의 마지막날", "이번달의 마지막 날은?==============" + bMaxDay);

                weekString();
                btnCk = true;
                abBtn = false;
                getDBdata();
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
                ArryClear(mArrKg, mArrCm, mArrHead, mArrFever, weekArrKg, weekArrCm, weekArrHead, weekArrFever);

                if (wToday.equals(weekStartDate)) {
                    Toast.makeText(getActivity(), "다음 달 기록이 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    wCal.add(Calendar.MONTH, +1);
                    weekStartDate = sFormat.format(wCal.getTime());
                    weekDateTxt.setText(weekStartDate);
                    aMaxDay = wCal.getActualMaximum(Calendar.DATE); //현재 달의 마지막날
                    mMaxDay = aMaxDay;
                    Log.d("이후 달의 마지막날", "이번달의 마지막 날은?==============" + aMaxDay);


                    weekString();
                    btnCk = true;
                    abBtn = true;
                    getDBdata();
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

        //당겨서 새로고침
        wSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ArryClear(mArrKg, mArrCm, mArrHead, mArrFever,weekArrKg, weekArrCm, weekArrHead, weekArrFever);
                weekString();
                getDBdata();
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

                wSwipeLayout.setRefreshing(false);
            }
        });

        return view;
    }

    private void MidDataSet() {

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
        wKgDataSets.add(AvgData(weekAvgWeight));
        wCmDataSets.add(wCm);
        wCmDataSets.add(AvgData(weekAvgHeight));
        wHeadDataSets.add(wHead);
        wHeadDataSets.add(AvgData(weekAvgHead));
        wFeverDataSets.add(wFever);
        wFeverDataSets.add(AvgData(weekAvgFever));

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
        //wXAxis.setDrawAxisLine(false);
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

    private float dataStack(float sum, ArrayList<Entry> values,float avg ,float [] AvgArr) {
        int count = 0;
        float val=0;

        for(int i=0; i<=5; i++){
            if(AvgArr[i]!=0&&Float.isNaN(AvgArr[i])==false) {
                val += AvgArr[i];
                values.add(new Entry(i, AvgArr[i]));
                count += 1;
            }
        }

        avg = val / count;
        return avg;
    }

    // 그래프 데이터 넣기용
    private void SetGraphData() {


        kgValues.clear();
        cmValues.clear();
        headValues.clear();
        feverValues.clear();

        XarWeek = getDate();
        weekAvgWeight = dataStack(wKgSum, kgValues, weekAvgWeight,weekArrKg);
        weekAvgHeight = dataStack(wCmSum, cmValues, weekAvgHeight,weekArrCm);
        weekAvgHead = dataStack(wHeadSum, headValues, weekAvgHead,weekArrHead);
        weekAvgFever = dataStack(wFeverSum, feverValues, weekAvgFever, weekArrFever);

        // 그래프 평균값 글자넣기
        weekAvgKgTxt.setText(String.format("%.2f", weekAvgWeight) + " kg");
        weekAvgCmTxt.setText(String.format("%.2f", weekAvgHeight) + " cm");
        weekAvgHeadTxt.setText(String.format("%.2f", weekAvgHead) + " cm");
        weekAvgFeverTxt.setText(String.format("%.2f", weekAvgFever) + " °C");
    }


    // chart X좌표 글자 넣기
    public ArrayList<String> getDate() {
        String read;
        ArrayList<String> label = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            label.add(i + "주");
        }
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
        // 현재 사용 아기데이터
        for (int i = 0; i < mMaxDay; i++) {
            String sql = "select * from growlog where name='" + mBabyname + "'AND date='" + SearchDay[i] + "'"; // 검색용
            Cursor c = db.rawQuery(sql, null);
            while (c.moveToNext()) {
                //SearchDay[i] = c.getString(3);
                mArrKg[i] = c.getString(2);
                mArrCm[i] = c.getString(3);
                mArrHead[i] = c.getString(4);
                mArrFever[i] = c.getString(5);
                Log.d("searchWeek", "SearchDay = " + SearchDay[i] + " ,mArrKg = "
                        + mArrKg[i] + "   ,mArrCm = " + mArrCm[i] + "   ,mArrHead = " + mArrHead[i] + "   ,mArrFever = " + mArrFever[i]);
            }
        }
        weekAvg(mArrKg ,weekArrKg);
        weekAvg(mArrCm ,weekArrCm);
        weekAvg(mArrHead ,weekArrHead);
        weekAvg(mArrFever ,weekArrFever);
    }

    private void weekAvg(String[] MonthArr, float[] WeekArr) {
        Log.d("weekarry", "실행 mMaxDay= "+mMaxDay);
        int WeekCount=0,avgCount=0;
        for (int i = 0; i <= mMaxDay; i++) {
            if (MonthArr[i] != null) {
                try {
                    WeekArr[WeekCount] += Float.parseFloat(MonthArr[i].trim());
                    Log.d("weekarry", "i="+i+"    "+WeekCount+" - WeekArr[WeekCount]="+WeekArr[WeekCount]+" MonthArr[i]= "+MonthArr[i]);
                    avgCount+=1;
                } catch (Exception e) { }
            }

            if((i%6==0)&&i>0) {
                WeekArr[WeekCount]= WeekArr[WeekCount]/avgCount;
                WeekCount += 1;
                avgCount=0;
            }
        }

        for(int o=0; o<=5; o++)
            Log.d("weekarry", o+ "weekAvg: "+WeekArr[o]);

    }


    public void weekString() {
        wCal.set(wCal.get(Calendar.YEAR), wCal.get(Calendar.MONTH), wCal.getActualMinimum(Calendar.DATE));

        SearchDay[0] = readChartD.format((wCal.getTime()));
        for (int i = 0; i < mMaxDay - 1; i++) {
            wCal.add(Calendar.DATE, +1);
            SearchDay[i + 1] = readChartD.format((wCal.getTime()));

        }
        for (int i = 0; i < mMaxDay; i++)
            Log.d("주간 학습", " SearchDay[0] = " + SearchDay[i]);

    }

    private LineDataSet AvgData(float avgData) {
        LineDataSet avgDataSet;
        ArrayList<Entry> avgValues = new ArrayList<>();

        for (int i = 0; i <= 4; i++) {
            avgValues.add(new Entry(i, avgData));
        }

        avgDataSet = new LineDataSet(avgValues, null);

        avgDataSet.setDrawValues(false);//데이터 값 없애기
        avgDataSet.setDrawCircles(false);//포인트 원 없애기
        // 그래프 색 넣기
        GraphLineColor(avgDataSet, Color.argb(0, 255, 0, 0));

        return avgDataSet;
    }

    private void ArryClear(String []a,String []b,String []c,String []d, float [] q,float []w,float []e,float []r){
        Arrays.fill(a,null);
        Arrays.fill(b,null);
        Arrays.fill(c,null);
        Arrays.fill(d,null);
        Arrays.fill(q,0);
        Arrays.fill(w,0);
        Arrays.fill(e,0);
        Arrays.fill(r,0);
    }

}
