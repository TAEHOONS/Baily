package com.example.baily.main.standard;

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

import com.example.baily.DBlink;
import com.example.baily.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class ChildFragHead  extends Fragment {
    private View view;
    private LineChart headChart;

    String dbName = "user.db";
    int dbVersion = 3, BYear, BMonth, BDay, i = 0,count=0,dDay;
    // mId= 현재 사용 id, baby
    private String mId, mBabyname;
    private DBlink helper;
    private SQLiteDatabase db;
    String k;
    float n;

    TextView headDateTxt;//생후 N개월 텍스트뷰
    ImageView headBeforeBtn, headAfterBtn;//이전 이후 버튼
    String headDate, mToday;
    int StartDay, EndDay,sM, eM;//sM=시작개월, eM=끝개월
    LineData boyHeadData, girlHeadData, babyHeadData;

    ArrayList<ILineDataSet> dataSets;
    ArrayList<Entry> valuesBoy, valuesGirl, valuesBaby;
    float[] standardHeadBoy,standardHeadGirl;
    float[] standardHeadBaby=new float[1000];
    String[] mArrHead, monthDday;
    float[] monthArrHead;

    public static ChildFragHead newInstance(){
        ChildFragHead childFragHead = new ChildFragHead();
        return childFragHead;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.standard_child_frag_head,container,false);
        //이전, 이후 버튼과 텍스트뷰
        headDateTxt = view.findViewById(R.id.headDateTxt);
        headBeforeBtn = (ImageView) view.findViewById(R.id.sHeadBeforeBtn);
        headAfterBtn = (ImageView) view.findViewById(R.id.sHeadAfterBtn);
        //표준머리둘레 차트
        headChart = view.findViewById(R.id.headLineCart);

        sM=1;
        eM=12;
        StartDay = 1;
        EndDay = 360;

        usingDB(container);

        monthDday = new String[dDay + 5];
        mArrHead = new String[380];
        monthArrHead = new float[20];
        monthString();
        loadgrowLog();

        valuesBoy = new ArrayList<>();
        valuesGirl = new ArrayList<>();
        valuesBaby = new ArrayList<>();//내애기

        //남아 표준 그래프(몸무게) 배열 값 삽입
        setBoyList();
        //여아 표준 그래프(몸무게) 배열 값 삽입
        setGirlList();

        // 값 셋팅하기
        SetGraphData();

        //  중간 업데이트
        MidDataSet();

        // 차트 속성
        setGraph(headChart,boyHeadData,girlHeadData,babyHeadData);

        //이전버튼눌렀을 때
        headBeforeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eM==12){
                    Toast.makeText(getActivity(), "이전 기록은 없습니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    StartDay = StartDay - 360;
                    EndDay = EndDay - 360;
                    sM= sM-12;
                    eM = eM-12;
                    headDate = "~ 생후 "+eM+"개월";
                    headDateTxt.setText(headDate);

                    //남아 표준 그래프(몸무게) 배열 값 삽입
                    setBoyList();
                    //여아 표준 그래프(몸무게) 배열 값 삽입
                    setGirlList();
                    monthString();
                    SetGraphData();
                    //  중간 업데이트

                    MidDataSet();
                    // 차트 속성
                    setGraph(headChart,boyHeadData,girlHeadData,babyHeadData);
                    ChartChange(headChart);
                }
            }
        });
        //이후버튼 눌렀을때
        headAfterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eM==72){
                    Toast.makeText(getActivity(), "72개월 이후 기록은 없습니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    StartDay = StartDay + 360;
                    EndDay = EndDay + 360;
                    sM = sM + 12;
                    eM = eM + 12;
                    headDate = "~ 생후 " + eM + "개월";
                    headDateTxt.setText(headDate);

                    //남아 표준 그래프(몸무게) 배열 값 삽입
                    setBoyList();
                    //여아 표준 그래프(몸무게) 배열 값 삽입
                    setGirlList();
                    monthString();
                    loadgrowLog();
                    SetGraphData();
                    //  중간 업데이트
                    MidDataSet();
                    // 차트 속성
                    setGraph(headChart,boyHeadData,girlHeadData,babyHeadData);
                    ChartChange(headChart);
                }
            }
        });


        return view;
    }

    private void MidDataSet() {

        LineDataSet boy, girl, baby;

        boy = new LineDataSet(valuesBoy, "남아 몸무게");
        girl = new LineDataSet(valuesGirl, "여아 몸무게");
        baby = new LineDataSet(valuesBaby, "내 아이 몸무게");

        // 기초 라인들 만들기
        dataSets = new ArrayList<>();

        // day"++"DataSets에 linedata 받은거 추가하기
        dataSets.add(boy); // 남아표준
        dataSets.add(girl); // 여아표준
        dataSets.add(baby);//내 아기

        // 실질적 라인인 day"++"Data에 새로 값넣기
        boyHeadData = new LineData(dataSets);
        girlHeadData = new LineData(dataSets);
        babyHeadData = new LineData(dataSets);//내아기

        // 그래프 색 넣기
        GraphLineColor2(boy, Color.BLUE);
        GraphLineColor2(girl, Color.RED);

        GraphLineColor(baby, Color.BLACK);
    }

    // 그래프에 데이터 적용 셋팅
    private void setGraph(LineChart headChart, LineData data1,LineData data2,LineData data3) {

        XAxis xAxis = headChart.getXAxis(); // x 축 설정
        xAxis.setPosition(XAxis.XAxisPosition.TOP); //x 축 표시에 대한 위치 설정
        // xAxis.setValueFormatter(new ChartXValueFormatter()); //X축의 데이터를 제 가공함. new ChartXValueFormatter은 Custom한 소스
        xAxis.setLabelCount(12, true); //X축의 데이터를 최대 몇개 까지 나타낼지에 대한 설정 5개 force가 true 이면 반드시 보여줌

        YAxis yAxisLeft = headChart.getAxisLeft(); //Y축의 왼쪽면 설정
        yAxisLeft.setDrawLabels(false);
        yAxisLeft.setDrawAxisLine(true);

        YAxis yAxisRight = headChart.getAxisRight(); //Y축의 오른쪽면 설정
        yAxisRight.setDrawGridLines(false);

        headChart.setDescription(null); //차트에서 Description 설정 저는 따로 안했습니다.

        headChart.setData(data1);
        headChart.setData(data2);
        headChart.setData(data3); //내아기
    }

    // 표준 그래프 컬러 적용
    private void GraphLineColor2(LineDataSet line, int color) {
        line.setColor(color);
        line.setDrawCircles(false);//포인트 점(원)없애기
        line.setDrawValues(false);//데이터 값 텍스트 없애기
    }
    // 내 아기 그래프 컬러 적용
    private void GraphLineColor(LineDataSet line, int color) {
        line.setColor(color);
        line.setCircleColor(color);
    }

    // 그래프 데이터 넣기용
    private void SetGraphData() {
        valuesBoy.clear();
        valuesGirl.clear();
        valuesBaby.clear();

        dataStack(sM,eM,valuesBoy, standardHeadBoy);
        dataStack(sM,eM,valuesGirl, standardHeadGirl);
        insertdataStack(sM, eM,valuesBaby, monthArrHead);
    }

    private void dataStack(int start, int end, ArrayList<Entry> values, float[] list) {
        for (int i = sM; i <= eM; i++) {
            if (list[i - 1] != 0 && Float.isNaN(list[i - 1]) == false) {
                values.add(new Entry(start, list[i - 1]));

            }
            start+=1;
        }
    }
    private void insertdataStack(int start, int end, ArrayList<Entry> values, float[] list) {
        for (int i = 1; i <= 12; i++) {
            if (list[i - 1] != 0 && Float.isNaN(list[i - 1]) == false) {
                values.add(new Entry(start, list[i - 1]));
                //Log.d("for문123", "값: " + list[i]);
            }
            start+=1;
        }
    }

    // 차트 변경 적용
    private void ChartChange(LineChart chart) {
        chart.notifyDataSetChanged();
        chart.invalidate();
    }

    //새로운시도
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

    // 저장된 growlog DB 에 있는걸 불러와서 그래프에 넣기
    private void loadgrowLog() {

        monthAvg();

    }

    private void monthAvg() {
        mArrHead = new String[380];
        int count = 0;

        for (int i = StartDay; i <= EndDay; i++) {

            String sql = "select * from growlog where name='" + mBabyname + "'AND caldate=" + i + ""; // 검색용
            Cursor c = db.rawQuery(sql, null);
            while (c.moveToNext()) {
                mArrHead[count] = c.getString(4);
                Log.d("avgViewday", count + " mArrKg[i] = " + mArrHead[count]);

            }
            count += 1;
            c.close();
        }


        int monthCount = 0, avgCount = 0;
        float sumMonth = 0;
        for (int i = 1; i <= 360; i++) {

            if (mArrHead[i] != null) {
                try {

                    sumMonth += Float.parseFloat(mArrHead[i].trim());

                    avgCount += 1;
                } catch (Exception e) {
                }
            }
            if (i % 30 == 0) {

                monthArrHead[monthCount] = sumMonth / avgCount;
                monthCount++;
                sumMonth = 0;
                avgCount = 0;
            }

        }

    }
    public void monthString() {
        for (i = 0; i < dDay; i++) {
            String s = Integer.toString(i);
            monthDday[i] = s;
        }
    }
    private void setBoyList(){
        standardHeadBoy = new float[73];
        standardHeadBoy[0] = (float)34.46;
        standardHeadBoy[1] = (float)37.28;
        standardHeadBoy[2] = (float)39.00;
        standardHeadBoy[3] = (float)39.13;
        standardHeadBoy[4] = (float)40.51;
        standardHeadBoy[5] = (float)41.63;
        standardHeadBoy[6] = (float)42.56;
        standardHeadBoy[7] = (float)43.98;
        standardHeadBoy[8] = (float)44.53;
        standardHeadBoy[9] = (float)45.00;
        standardHeadBoy[10] = (float)45.41;
        standardHeadBoy[11] = (float)45.76;
        standardHeadBoy[12] = (float)46.07;
        standardHeadBoy[13] = (float)46.34;
        standardHeadBoy[14] = (float)46.58;
        standardHeadBoy[15] = (float)46.81;
        standardHeadBoy[16] = (float)47.01;
        standardHeadBoy[17] = (float)47.20;
        standardHeadBoy[18] = (float)47.37;
        standardHeadBoy[19] = (float)47.54;
        standardHeadBoy[20] = (float)47.69;
        standardHeadBoy[21] = (float)47.84;
        standardHeadBoy[22] = (float)47.98;
        standardHeadBoy[23] = (float)48.12;
        standardHeadBoy[24] = (float)48.25;
        standardHeadBoy[25] = (float)48.36;
        standardHeadBoy[26] = (float)48.48;
        standardHeadBoy[27] = (float)48.59;
        standardHeadBoy[28] = (float)48.71;
        standardHeadBoy[29] = (float)48.82;
        //30개월
        standardHeadBoy[30] = (float)48.86;
        standardHeadBoy[31] = (float)48.94;
        standardHeadBoy[32] = (float)49.09;
        standardHeadBoy[33] = (float)49.24;
        standardHeadBoy[34] = (float)49.39;
        standardHeadBoy[35] = (float)49.53;
        standardHeadBoy[36] = (float)49.68;
        standardHeadBoy[37] = (float)49.83;
        standardHeadBoy[38] = (float)49.96;
        standardHeadBoy[39] = (float)50.02;
        standardHeadBoy[40] = (float)50.08;
        standardHeadBoy[41] = (float)50.15;
        standardHeadBoy[42] = (float)50.21;
        standardHeadBoy[43] = (float)50.27;
        standardHeadBoy[44] = (float)50.32;
        standardHeadBoy[45] = (float)50.38;
        standardHeadBoy[46] = (float)50.43;
        standardHeadBoy[47] = (float)50.49;
        standardHeadBoy[48] = (float)50.54;
        standardHeadBoy[49] = (float)50.59;
        standardHeadBoy[50] = (float)50.64;
        standardHeadBoy[51] = (float)50.68;
        standardHeadBoy[52] = (float)50.73;
        standardHeadBoy[53] = (float)50.78;
        standardHeadBoy[54] = (float)50.83;
        standardHeadBoy[55] = (float)50.88;
        standardHeadBoy[56] = (float)50.92;
        standardHeadBoy[57] = (float)50.97;
        standardHeadBoy[58] = (float)51.02;
        standardHeadBoy[59] = (float)51.06;
        //60개월
        standardHeadBoy[60] = (float)51.11;
        standardHeadBoy[61] = (float)51.16;
        standardHeadBoy[62] = (float)51.21;
        standardHeadBoy[63] = (float)51.26;
        standardHeadBoy[64] = (float)51.30;
        standardHeadBoy[65] = (float)51.35;
        standardHeadBoy[66] = (float)51.40;
        standardHeadBoy[67] = (float)51.45;
        standardHeadBoy[68] = (float)51.49;
        standardHeadBoy[69] = (float)51.54;
        standardHeadBoy[70] = (float)51.59;
        standardHeadBoy[71] = (float)51.63;
        standardHeadBoy[72] = (float)51.68;
    }
    private void setGirlList(){
        standardHeadGirl = new float[73];
        standardHeadGirl[0] = (float)33.88;
        standardHeadGirl[1] = (float)36.55;
        standardHeadGirl[2] = (float)38.25;
        standardHeadGirl[3] = (float)39.53;
        standardHeadGirl[4] = (float)40.58;
        standardHeadGirl[5] = (float)41.46;
        standardHeadGirl[6] = (float)42.20;
        standardHeadGirl[7] = (float)42.83;
        standardHeadGirl[8] = (float)43.37;
        standardHeadGirl[9] = (float)43.83;
        standardHeadGirl[10] = (float)44.23;
        standardHeadGirl[11] = (float)44.58;
        standardHeadGirl[12] = (float)44.90;
        standardHeadGirl[13] = (float)45.18;
        standardHeadGirl[14] = (float)45.43;
        standardHeadGirl[15] = (float)45.66;
        standardHeadGirl[16] = (float)45.87;
        standardHeadGirl[17] = (float)46.06;
        standardHeadGirl[18] = (float)46.24;
        standardHeadGirl[19] = (float)46.42;
        standardHeadGirl[20] = (float)46.58;
        standardHeadGirl[21] = (float)46.74;
        standardHeadGirl[22] = (float)46.89;
        standardHeadGirl[23] = (float)47.04;
        standardHeadGirl[24] = (float)47.18;
        standardHeadGirl[25] = (float)47.31;
        standardHeadGirl[26] = (float)47.43;
        standardHeadGirl[27] = (float)47.56;
        standardHeadGirl[28] = (float)47.68;
        standardHeadGirl[29] = (float)47.81;
        //30개월
        standardHeadGirl[30] = (float)47.93;
        standardHeadGirl[31] = (float)48.08;
        standardHeadGirl[32] = (float)48.24;
        standardHeadGirl[33] = (float)48.39;
        standardHeadGirl[34] = (float)48.54;
        standardHeadGirl[35] = (float)48.70;
        standardHeadGirl[36] = (float)48.85;
        standardHeadGirl[37] = (float)48.92;
        standardHeadGirl[38] = (float)48.99;
        standardHeadGirl[39] = (float)49.05;
        standardHeadGirl[40] = (float)49.12;
        standardHeadGirl[41] = (float)49.19;
        standardHeadGirl[42] = (float)49.26;
        standardHeadGirl[43] = (float)49.32;
        standardHeadGirl[44] = (float)49.38;
        standardHeadGirl[45] = (float)49.44;
        standardHeadGirl[46] = (float)49.49;
        standardHeadGirl[47] = (float)49.55;
        standardHeadGirl[48] = (float)49.61;
        standardHeadGirl[49] = (float)49.67;
        standardHeadGirl[50] = (float)49.72;
        standardHeadGirl[51] = (float)49.78;
        standardHeadGirl[52] = (float)49.83;
        standardHeadGirl[53] = (float)49.89;
        standardHeadGirl[54] = (float)49.94;
        standardHeadGirl[55] = (float)49.99;
        standardHeadGirl[56] = (float)50.05;
        standardHeadGirl[57] = (float)50.09;
        standardHeadGirl[58] = (float)50.14;
        standardHeadGirl[59] = (float)50.19;
        //60개월
        standardHeadGirl[60] = (float)50.24;
        standardHeadGirl[61] = (float)50.29;
        standardHeadGirl[62] = (float)50.34;
        standardHeadGirl[63] = (float)50.39;
        standardHeadGirl[64] = (float)50.45;
        standardHeadGirl[65] = (float)50.50;
        standardHeadGirl[66] = (float)50.55;
        standardHeadGirl[67] = (float)50.60;
        standardHeadGirl[68] = (float)50.65;
        standardHeadGirl[69] = (float)50.71;
        standardHeadGirl[70] = (float)50.76;
        standardHeadGirl[71] = (float)50.81;
        standardHeadGirl[72] = (float)50.86;
    }
    private void setBabyList(int i, float n) {
        standardHeadBaby[i] = n;
    }


}