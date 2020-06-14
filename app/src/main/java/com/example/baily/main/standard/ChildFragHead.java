package com.example.baily.main.standard;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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
    private LineChart headCart;

    ArrayList<Entry> valuesBoy,valuesGirl,valuesBaby;
    float[] standardHeadBoy,standardHeadGirl;

    public static ChildFragHead newInstance(){
        ChildFragHead childFragHead = new ChildFragHead();
        return childFragHead;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.standard_child_frag_head,container,false);

        headCart = view.findViewById(R.id.headLineCart);

        valuesBoy = new ArrayList<>();
        valuesGirl = new ArrayList<>();
        valuesBaby = new ArrayList<>();



        //남아 표준 그래프(머리둘레) 배열 값 삽입
        setBoyList();

        //여아 표준 그래프(머리둘레) 배열 값 삽입
        setGirlList();

        // 내 아이 임시 머리둘레 데이터
        float[] standardHeadBaby = new float[73];
        standardHeadBaby[0] = (float)40.88;
        standardHeadBaby[1] = (float)42.55;
        standardHeadBaby[2] = (float)44.25;
        standardHeadBaby[3] = (float)47.53;



        //그래프에 값 넣기
        for (int i = 0; i < 73; i++) {
            valuesBoy.add(new Entry(i,standardHeadBoy[i]));
        }
        for (int i = 0; i < 73; i++) {
            valuesGirl.add(new Entry(i,standardHeadGirl[i]));
        }
        //내애기값넣기
        for (int i = 0; i < 4; i++) {
            valuesBaby.add(new Entry(i,standardHeadBaby[i]));
        }
        LineDataSet set1;
        LineDataSet set2;
        LineDataSet set3; // 내애기

        set1 = new LineDataSet(valuesBoy, "남아 머리둘레");
        set2 = new LineDataSet(valuesGirl, "여아 머리둘레");
        set3 = new LineDataSet(valuesBaby,"내 아이 머리둘레");

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the data sets
        dataSets.add(set2);
        dataSets.add(set3);

        // create a data object with the data sets
        LineData data1 = new LineData(dataSets);
        LineData data2 = new LineData(dataSets);
        LineData data3 = new LineData(dataSets);//내애기

        // black lines and points
        set1.setColor(Color.BLUE);
        set1.setDrawCircles(false);//포인트 점(원)없애기
        set1.setDrawValues(false);//데이터 값 텍스트 없애기
        set2.setColor(Color.RED);
        set2.setDrawCircles(false);//포인트 점(원)없애기
        set2.setDrawValues(false);//데이터 값 텍스트 없애기
        set3.setColor(Color.BLACK);
        set3.setCircleColor(Color.BLACK);


        XAxis xAxis = headCart.getXAxis(); // x 축 설정
        xAxis.setPosition(XAxis.XAxisPosition.TOP); //x 축 표시에 대한 위치 설정
       // xAxis.setValueFormatter(new ChartXValueFormatter()); //X축의 데이터를 제 가공함. new ChartXValueFormatter은 Custom한 소스
        xAxis.setLabelCount(12, true); //X축의 데이터를 최대 몇개 까지 나타낼지에 대한 설정 5개 force가 true 이면 반드시 보여줌
        //xAxis.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor)); // X축 텍스트컬러설정
        //xAxis.setGridColor(ContextCompat.getColor(getContext(), R.color.textColor)); // X축 줄의 컬러 설정

        YAxis yAxisLeft = headCart.getAxisLeft(); //Y축의 왼쪽면 설정
        yAxisLeft.setDrawLabels(false);
        yAxisLeft.setDrawAxisLine(false);
        yAxisLeft.setDrawGridLines(false);
        //yAxisLeft.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor)); //Y축 텍스트 컬러 설정
        //yAxisLeft.setGridColor(ContextCompat.getColor(getContext(), R.color.textColor)); // Y축 줄의 컬러 설정

        YAxis yAxisRight = headCart.getAxisRight(); //Y축의 오른쪽면 설정

        headCart.setVisibleXRangeMinimum(60 * 60 * 24 * 1000 * 5); //라인차트에서 최대로 보여질 X축의 데이터 설정
        headCart.setDescription(null); //차트에서 Description 설정 저는 따로 안했습니다.


        // set data
        headCart.setData(data1);
        headCart.setData(data2);
        headCart.setData(data3);


        return view;
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
}
