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

public class ChildFragTall  extends Fragment {
    private View view;
    private LineChart tallChart;

    String dbName = "user.db";
    int dbVersion = 3, BYear, BMonth, BDay, i = 0,count=0,dDay;
    // mId= 현재 사용 id, baby
    private String mId, mBabyname;
    private DBlink helper;
    private SQLiteDatabase db;
    String k;
    float n;

    TextView tallDateTxt;//생후 N개월 텍스트뷰
    ImageView tallBeforeBtn, tallAfterBtn;//이전 이후 버튼
    String tallDate, mToday;
    int StartDay, EndDay, sM, eM;//sM=시작개월, eM=끝개월
    LineData boyTallData, girlTallData, babyTallData;

    ArrayList<ILineDataSet> dataSets;
    ArrayList<Entry> valuesBoy, valuesGirl, valuesBaby;
    float[] standardTallBoy,standardTallGirl;
    float[] standardTallBaby = new float[1000];
    String[] mArrTall, monthDday;
    float[] monthArrTall;

    public static ChildFragTall newInstance(){
        ChildFragTall childFragTall = new ChildFragTall();
        return childFragTall;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.standard_child_frag_tall,container,false);
        //이전, 이후 버튼과 텍스트뷰
        tallDateTxt = view.findViewById(R.id.tallDateTxt);
        tallBeforeBtn = (ImageView) view.findViewById(R.id.sTallBeforeBtn);
        tallAfterBtn = (ImageView) view.findViewById(R.id.sTallAfterBtn);
        //표준머리둘레 차트
        tallChart = view.findViewById(R.id.tallLineCart);

        sM=1;
        eM=12;
        StartDay = 1;
        EndDay = 360;

        usingDB(container);

        monthDday = new String[dDay + 5];
        mArrTall = new String[380];
        monthArrTall = new float[20];
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
        setGraph(tallChart,boyTallData,girlTallData,babyTallData);

        //이전버튼눌렀을 때
        tallBeforeBtn.setOnClickListener(new View.OnClickListener() {
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
                    tallDate = "~ 생후 "+eM+"개월";
                    tallDateTxt.setText(tallDate);

                    //남아 표준 그래프(몸무게) 배열 값 삽입
                    setBoyList();
                    //여아 표준 그래프(몸무게) 배열 값 삽입
                    setGirlList();
                    monthString();
                    SetGraphData();
                   
                    //  중간 업데이트
                    MidDataSet();
                    // 차트 속성
                    setGraph(tallChart,boyTallData,girlTallData,babyTallData);
                    ChartChange(tallChart);
                }
            }
        });
        //이후버튼 눌렀을때
        tallAfterBtn.setOnClickListener(new View.OnClickListener() {
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
                    tallDate = "~ 생후 " + eM + "개월";
                    tallDateTxt.setText(tallDate);

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
                    setGraph(tallChart,boyTallData,girlTallData,babyTallData);
                    ChartChange(tallChart);
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
        boyTallData = new LineData(dataSets);
        girlTallData = new LineData(dataSets);
        babyTallData = new LineData(dataSets);//내아기

        // 그래프 색 넣기
        GraphLineColor2(boy, Color.BLUE);
        GraphLineColor2(girl, Color.RED);

        GraphLineColor(baby, Color.BLACK);
    }

    // 그래프에 데이터 적용 셋팅
    private void setGraph(LineChart tallChart, LineData data1,LineData data2,LineData data3) {

        XAxis xAxis = tallChart.getXAxis(); // x 축 설정
        xAxis.setPosition(XAxis.XAxisPosition.TOP); //x 축 표시에 대한 위치 설정
        xAxis.setLabelCount(12, true); //X축의 데이터를 최대 몇개 까지 나타낼지에 대한 설정 5개 force가 true 이면 반드시 보여줌

        YAxis yAxisLeft = tallChart.getAxisLeft(); //Y축의 왼쪽면 설정
        yAxisLeft.setDrawLabels(false);
        yAxisLeft.setDrawAxisLine(true);

        YAxis yAxisRight = tallChart.getAxisRight(); //Y축의 오른쪽면 설정
        yAxisRight.setDrawGridLines(false);

        tallChart.setDescription(null); //차트에서 Description 설정 저는 따로 안했습니다.

        tallChart.setData(data1);
        tallChart.setData(data2);
        tallChart.setData(data3); //내아기
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

        dataStack(sM,eM,valuesBoy, standardTallBoy);
        dataStack(sM,eM,valuesGirl, standardTallGirl);
        insertdataStack(sM,eM,valuesBaby, monthArrTall);
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
        mArrTall = new String[380];
        int count = 0;

        for (int i = StartDay; i <= EndDay; i++) {

            String sql = "select * from growlog where name='" + mBabyname + "'AND caldate=" + i + ""; // 검색용
            Cursor c = db.rawQuery(sql, null);
            while (c.moveToNext()) {
                mArrTall[count] = c.getString(3);
                Log.d("avgViewday", count + " mArrTall[i] = " + mArrTall[count]);

            }
            count += 1;
            c.close();
        }


        int monthCount = 0, avgCount = 0;
        float sumMonth = 0;
        for (int i = 1; i <= 360; i++) {

            if (mArrTall[i] != null) {
                try {

                    sumMonth += Float.parseFloat(mArrTall[i].trim());

                    avgCount += 1;
                } catch (Exception e) {
                }
            }
            if (i % 30 == 0) {

                monthArrTall[monthCount] = sumMonth / avgCount;
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
    private void setBoyList() {
        standardTallBoy = new float[73];
        standardTallBoy[0] = (float) 49.88;
        standardTallBoy[1] = (float) 54.47;
        standardTallBoy[2] = (float) 58.42;
        standardTallBoy[3] = (float) 61.43;
        standardTallBoy[4] = (float) 63.89;
        standardTallBoy[5] = (float) 65.90;
        standardTallBoy[6] = (float) 67.62;
        standardTallBoy[7] = (float) 69.16;
        standardTallBoy[8] = (float) 70.60;
        standardTallBoy[9] = (float) 71.97;
        standardTallBoy[10] = (float) 73.28;
        standardTallBoy[11] = (float) 74.54;
        standardTallBoy[12] = (float) 75.75;
        standardTallBoy[13] = (float) 76.92;
        standardTallBoy[14] = (float) 78.05;
        standardTallBoy[15] = (float) 79.15;
        standardTallBoy[16] = (float) 80.21;
        standardTallBoy[17] = (float) 81.25;
        standardTallBoy[18] = (float) 82.26;
        standardTallBoy[19] = (float) 83.24;
        standardTallBoy[20] = (float) 84.20;
        standardTallBoy[21] = (float) 85.13;
        standardTallBoy[22] = (float) 86.05;
        standardTallBoy[23] = (float) 86.94;
        standardTallBoy[24] = (float) 87.12;
        //25개월부터
        standardTallBoy[25] = (float) 87.90;
        standardTallBoy[26] = (float) 88.69;
        standardTallBoy[27] = (float) 89.49;
        standardTallBoy[28] = (float) 90.29;
        standardTallBoy[29] = (float) 91.11;
        standardTallBoy[30] = (float) 91.93;
        standardTallBoy[31] = (float) 92.69;
        standardTallBoy[32] = (float) 93.45;
        standardTallBoy[33] = (float) 94.22;
        standardTallBoy[34] = (float) 94.98;
        standardTallBoy[35] = (float) 95.74;
        standardTallBoy[36] = (float) 96.50;
        standardTallBoy[37] = (float) 97.05;
        standardTallBoy[38] = (float) 97.60;
        standardTallBoy[39] = (float) 98.15;
        standardTallBoy[40] = (float) 98.69;
        standardTallBoy[41] = (float) 99.24;
        standardTallBoy[42] = (float) 99.79;
        standardTallBoy[43] = (float) 100.33;
        standardTallBoy[44] = (float) 100.87;
        standardTallBoy[45] = (float) 101.42;
        standardTallBoy[46] = (float) 101.96;
        standardTallBoy[47] = (float) 102.52;
        standardTallBoy[48] = (float) 103.07;
        standardTallBoy[49] = (float) 103.62;
        //50개월부터
        standardTallBoy[50] = (float) 104.16;
        standardTallBoy[51] = (float) 104.71;
        standardTallBoy[52] = (float) 105.25;
        standardTallBoy[53] = (float) 105.80;
        standardTallBoy[54] = (float) 106.34;
        standardTallBoy[55] = (float) 106.87;
        standardTallBoy[56] = (float) 107.41;
        standardTallBoy[57] = (float) 107.95;
        standardTallBoy[58] = (float) 108.50;
        standardTallBoy[59] = (float) 109.04;
        standardTallBoy[60] = (float) 109.59;
        standardTallBoy[61] = (float) 110.11;
        standardTallBoy[62] = (float) 110.64;
        standardTallBoy[63] = (float) 111.17;
        standardTallBoy[64] = (float) 111.70;
        standardTallBoy[65] = (float) 112.23;
        standardTallBoy[66] = (float) 112.77;
        standardTallBoy[67] = (float) 113.30;
        standardTallBoy[68] = (float) 113.82;
        standardTallBoy[69] = (float) 114.35;
        standardTallBoy[70] = (float) 114.87;
        standardTallBoy[71] = (float) 115.40;
        standardTallBoy[72] = (float) 115.92;

    }

    private void setGirlList() {
        standardTallGirl = new float[73];
        standardTallGirl[0] = (float) 49.15;
        standardTallGirl[1] = (float) 53.69;
        standardTallGirl[2] = (float) 57.07;
        standardTallGirl[3] = (float) 59.80;
        standardTallGirl[4] = (float) 62.09;
        standardTallGirl[5] = (float) 64.03;
        standardTallGirl[6] = (float) 65.73;
        standardTallGirl[7] = (float) 67.29;
        standardTallGirl[8] = (float) 68.75;
        standardTallGirl[9] = (float) 70.14;
        standardTallGirl[10] = (float) 71.48;
        standardTallGirl[11] = (float) 72.78;
        standardTallGirl[12] = (float) 74.02;
        standardTallGirl[13] = (float) 75.22;
        standardTallGirl[14] = (float) 76.38;
        standardTallGirl[15] = (float) 77.51;
        standardTallGirl[16] = (float) 78.61;
        standardTallGirl[17] = (float) 79.67;
        standardTallGirl[18] = (float) 80.71;
        standardTallGirl[19] = (float) 81.72;
        standardTallGirl[20] = (float) 82.70;
        standardTallGirl[21] = (float) 83.67;
        standardTallGirl[22] = (float) 84.60;
        standardTallGirl[23] = (float) 85.52;
        standardTallGirl[24] = (float) 85.72;
        //25개월부터
        standardTallGirl[25] = (float) 86.55;
        standardTallGirl[26] = (float) 87.37;
        standardTallGirl[27] = (float) 88.20;
        standardTallGirl[28] = (float) 89.03;
        standardTallGirl[29] = (float) 89.85;
        standardTallGirl[30] = (float) 90.68;
        standardTallGirl[31] = (float) 91.45;
        standardTallGirl[32] = (float) 92.23;
        standardTallGirl[33] = (float) 93.01;
        standardTallGirl[34] = (float) 93.81;
        standardTallGirl[35] = (float) 94.60;
        standardTallGirl[36] = (float) 95.41;
        standardTallGirl[37] = (float) 95.94;
        standardTallGirl[38] = (float) 96.48;
        standardTallGirl[39] = (float) 97.02;
        standardTallGirl[40] = (float) 97.56;
        standardTallGirl[41] = (float) 98.10;
        standardTallGirl[42] = (float) 98.65;
        standardTallGirl[43] = (float) 99.18;
        standardTallGirl[44] = (float) 99.72;
        standardTallGirl[45] = (float) 100.26;
        standardTallGirl[46] = (float) 100.80;
        standardTallGirl[47] = (float) 101.34;
        standardTallGirl[48] = (float) 101.89;
        standardTallGirl[49] = (float) 102.42;
        //50개월부터
        standardTallGirl[50] = (float) 102.96;
        standardTallGirl[51] = (float) 103.50;
        standardTallGirl[52] = (float) 104.05;
        standardTallGirl[53] = (float) 104.59;
        standardTallGirl[54] = (float) 105.14;
        standardTallGirl[55] = (float) 105.67;
        standardTallGirl[56] = (float) 106.21;
        standardTallGirl[57] = (float) 106.74;
        standardTallGirl[58] = (float) 107.28;
        standardTallGirl[59] = (float) 107.82;
        //60개월
        standardTallGirl[60] = (float) 108.37;
        standardTallGirl[61] = (float) 108.90;
        standardTallGirl[62] = (float) 109.44;
        standardTallGirl[63] = (float) 109.97;
        standardTallGirl[64] = (float) 110.50;
        standardTallGirl[65] = (float) 111.04;
        standardTallGirl[66] = (float) 111.57;
        standardTallGirl[67] = (float) 112.09;
        standardTallGirl[68] = (float) 112.61;
        standardTallGirl[69] = (float) 113.14;
        standardTallGirl[70] = (float) 113.67;
        standardTallGirl[71] = (float) 114.20;
        standardTallGirl[72] = (float) 114.73;
    }

    private void setBabyList(int i, float n) {
        standardTallBaby[i] = n;
    }


}


