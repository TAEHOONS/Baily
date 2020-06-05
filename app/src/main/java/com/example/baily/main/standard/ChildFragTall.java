package com.example.baily.main.standard;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class ChildFragTall extends Fragment {
    private View view;
    private LineChart tallCart;

    public static ChildFragTall newInstance(){
        ChildFragTall childFragTall = new ChildFragTall();
        return childFragTall;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.standard_child_frag_tall,container,false);

        tallCart = view.findViewById(R.id.tallLineCart);

        ArrayList<Entry> valuesBoy = new ArrayList<>();
        ArrayList<Entry> valuesGirl = new ArrayList<>();
        ArrayList<Entry> valuesBaby = new ArrayList<>();


        //남아 표준 그래프(키) 배열 값 삽입
        float[] standardTallBoy = new float[73];
        standardTallBoy[0] = (float)49.88;
        standardTallBoy[1] = (float)54.47;
        standardTallBoy[2] = (float)58.42;
        standardTallBoy[3] = (float)61.43;
        standardTallBoy[4] = (float)63.89;
        standardTallBoy[5] = (float)65.90;
        standardTallBoy[6] = (float)67.62;
        standardTallBoy[7] = (float)69.16;
        standardTallBoy[8] = (float)70.60;
        standardTallBoy[9] = (float)71.97;
        standardTallBoy[10] = (float)73.28;
        standardTallBoy[11] = (float)74.54;
        standardTallBoy[12] = (float)75.75;
        standardTallBoy[13] = (float)76.92;
        standardTallBoy[14] = (float)78.05;
        standardTallBoy[15] = (float)79.15;
        standardTallBoy[16] = (float)80.21;
        standardTallBoy[17] = (float)81.25;
        standardTallBoy[18] = (float)82.26;
        standardTallBoy[19] = (float)83.24;
        standardTallBoy[20] = (float)84.20;
        standardTallBoy[21] = (float)85.13;
        standardTallBoy[22] = (float)86.05;
        standardTallBoy[23] = (float)86.94;
        standardTallBoy[24] = (float)87.12;
        //25개월부터
        standardTallBoy[25] = (float)87.90;
        standardTallBoy[26] = (float)88.69;
        standardTallBoy[27] = (float)89.49;
        standardTallBoy[28] = (float)90.29;
        standardTallBoy[29] = (float)91.11;
        standardTallBoy[30] = (float)91.93;
        standardTallBoy[31] = (float)92.69;
        standardTallBoy[32] = (float)93.45;
        standardTallBoy[33] = (float)94.22;
        standardTallBoy[34] = (float)94.98;
        standardTallBoy[35] = (float)95.74;
        standardTallBoy[36] = (float)96.50;
        standardTallBoy[37] = (float)97.05;
        standardTallBoy[38] = (float)97.60;
        standardTallBoy[39] = (float)98.15;
        standardTallBoy[40] = (float)98.69;
        standardTallBoy[41] = (float)99.24;
        standardTallBoy[42] = (float)99.79;
        standardTallBoy[43] = (float)100.33;
        standardTallBoy[44] = (float)100.87;
        standardTallBoy[45] = (float)101.42;
        standardTallBoy[46] = (float)101.96;
        standardTallBoy[47] = (float)102.52;
        standardTallBoy[48] = (float)103.07;
        standardTallBoy[49] = (float)103.62;
        //50개월부터
        standardTallBoy[50] = (float)104.16;
        standardTallBoy[51] = (float)104.71;
        standardTallBoy[52] = (float)105.25;
        standardTallBoy[53] = (float)105.80;
        standardTallBoy[54] = (float)106.34;
        standardTallBoy[55] = (float)106.87;
        standardTallBoy[56] = (float)107.41;
        standardTallBoy[57] = (float)107.95;
        standardTallBoy[58] = (float)108.50;
        standardTallBoy[59] = (float)109.04;
        standardTallBoy[60] = (float)109.59;
        standardTallBoy[61] = (float)110.11;
        standardTallBoy[62] = (float)110.64;
        standardTallBoy[63] = (float)111.17;
        standardTallBoy[64] = (float)111.70;
        standardTallBoy[65] = (float)112.23;
        standardTallBoy[66] = (float)112.77;
        standardTallBoy[67] = (float)113.30;
        standardTallBoy[68] = (float)113.82;
        standardTallBoy[69] = (float)114.35;
        standardTallBoy[70] = (float)114.87;
        standardTallBoy[71] = (float)115.40;
        standardTallBoy[72] = (float)115.92;


        //여아 표준 그래프(키) 배열 값 삽입
        float[] standardTallGirl = new float[73];
        standardTallGirl[0] = (float)49.15;
        standardTallGirl[1] = (float)53.69;
        standardTallGirl[2] = (float)57.07;
        standardTallGirl[3] = (float)59.80;
        standardTallGirl[4] = (float)62.09;
        standardTallGirl[5] = (float)64.03;
        standardTallGirl[6] = (float)65.73;
        standardTallGirl[7] = (float)67.29;
        standardTallGirl[8] = (float)68.75;
        standardTallGirl[9] = (float)70.14;
        standardTallGirl[10] = (float)71.48;
        standardTallGirl[11] = (float)72.78;
        standardTallGirl[12] = (float)74.02;
        standardTallGirl[13] = (float)75.22;
        standardTallGirl[14] = (float)76.38;
        standardTallGirl[15] = (float)77.51;
        standardTallGirl[16] = (float)78.61;
        standardTallGirl[17] = (float)79.67;
        standardTallGirl[18] = (float)80.71;
        standardTallGirl[19] = (float)81.72;
        standardTallGirl[20] = (float)82.70;
        standardTallGirl[21] = (float)83.67;
        standardTallGirl[22] = (float)84.60;
        standardTallGirl[23] = (float)85.52;
        standardTallGirl[24] = (float)85.72;
        //25개월부터
        standardTallGirl[25] = (float)86.55;
        standardTallGirl[26] = (float)87.37;
        standardTallGirl[27] = (float)88.20;
        standardTallGirl[28] = (float)89.03;
        standardTallGirl[29] = (float)89.85;
        standardTallGirl[30] = (float)90.68;
        standardTallGirl[31] = (float)91.45;
        standardTallGirl[32] = (float)92.23;
        standardTallGirl[33] = (float)93.01;
        standardTallGirl[34] = (float)93.81;
        standardTallGirl[35] = (float)94.60;
        standardTallGirl[36] = (float)95.41;
        standardTallGirl[37] = (float)95.94;
        standardTallGirl[38] = (float)96.48;
        standardTallGirl[39] = (float)97.02;
        standardTallGirl[40] = (float)97.56;
        standardTallGirl[41] = (float)98.10;
        standardTallGirl[42] = (float)98.65;
        standardTallGirl[43] = (float)99.18;
        standardTallGirl[44] = (float)99.72;
        standardTallGirl[45] = (float)100.26;
        standardTallGirl[46] = (float)100.80;
        standardTallGirl[47] = (float)101.34;
        standardTallGirl[48] = (float)101.89;
        standardTallGirl[49] = (float)102.42;
        //50개월부터
        standardTallGirl[50] = (float)102.96;
        standardTallGirl[51] = (float)103.50;
        standardTallGirl[52] = (float)104.05;
        standardTallGirl[53] = (float)104.59;
        standardTallGirl[54] = (float)105.14;
        standardTallGirl[55] = (float)105.67;
        standardTallGirl[56] = (float)106.21;
        standardTallGirl[57] = (float)106.74;
        standardTallGirl[58] = (float)107.28;
        standardTallGirl[59] = (float)107.82;
        //60개월
        standardTallGirl[60] = (float)108.37;
        standardTallGirl[61] = (float)108.90;
        standardTallGirl[62] = (float)109.44;
        standardTallGirl[63] = (float)109.97;
        standardTallGirl[64] = (float)110.50;
        standardTallGirl[65] = (float)111.04;
        standardTallGirl[66] = (float)111.57;
        standardTallGirl[67] = (float)112.09;
        standardTallGirl[68] = (float)112.61;
        standardTallGirl[69] = (float)113.14;
        standardTallGirl[70] = (float)113.67;
        standardTallGirl[71] = (float)114.20;
        standardTallGirl[72] = (float)114.73;

        // 내 아이 임시 데이터
        float[] standardTallBaby = new float[73];
        standardTallBaby[0] = (float)55.55;
        standardTallBaby[1] = (float)56.69;
        standardTallBaby[2] = (float)59.07;
        standardTallBaby[3] = (float)63.80;

        //그래프에 값 넣기
        for (int i = 0; i < 73; i++) {
            valuesBoy.add(new Entry(i,standardTallBoy[i]));
        }
        for (int i = 0; i < 73; i++) {
            valuesGirl.add(new Entry(i,standardTallGirl[i]));
        }
        for (int i = 0; i < 4; i++) {
            valuesBaby.add(new Entry(i,standardTallBaby[i]));
        }

        LineDataSet set1;
        LineDataSet set2;
        LineDataSet set3;

        set1 = new LineDataSet(valuesBoy, "남아 신장");
        set2 = new LineDataSet(valuesGirl, "여아 신장");
        set3 = new LineDataSet(valuesBaby,"내 아이 신장");

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the data sets
        dataSets.add(set2);
        dataSets.add(set3);

        // create a data object with the data sets
        LineData data1 = new LineData(dataSets);
        LineData data2 = new LineData(dataSets);
        LineData data3 = new LineData(dataSets);

        // black lines and points
        set1.setColor(Color.BLUE);
        set1.setCircleColor(Color.BLUE);
        set2.setColor(Color.RED);
        set2.setCircleColor(Color.RED);
        set3.setColor(Color.BLACK);
        set3.setCircleColor(Color.BLACK);


        XAxis xAxis = tallCart.getXAxis(); // x 축 설정
        xAxis.setPosition(XAxis.XAxisPosition.TOP); //x 축 표시에 대한 위치 설정
        // xAxis.setValueFormatter(new ChartXValueFormatter()); //X축의 데이터를 제 가공함. new ChartXValueFormatter은 Custom한 소스
        xAxis.setLabelCount(12, true); //X축의 데이터를 최대 몇개 까지 나타낼지에 대한 설정 5개 force가 true 이면 반드시 보여줌
        //xAxis.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor)); // X축 텍스트컬러설정
        //xAxis.setGridColor(ContextCompat.getColor(getContext(), R.color.textColor)); // X축 줄의 컬러 설정

        YAxis yAxisLeft = tallCart.getAxisLeft(); //Y축의 왼쪽면 설정
        yAxisLeft.setDrawLabels(false);
        yAxisLeft.setDrawAxisLine(false);
        yAxisLeft.setDrawGridLines(false);
        //yAxisLeft.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor)); //Y축 텍스트 컬러 설정
        //yAxisLeft.setGridColor(ContextCompat.getColor(getContext(), R.color.textColor)); // Y축 줄의 컬러 설정

        YAxis yAxisRight = tallCart.getAxisRight(); //Y축의 오른쪽면 설정

        tallCart.setVisibleXRangeMinimum(60 * 60 * 24 * 1000 * 5); //라인차트에서 최대로 보여질 X축의 데이터 설정
        tallCart.setDescription(null); //차트에서 Description 설정 저는 따로 안했습니다.

        // set data
        tallCart.setData(data1);
        tallCart.setData(data2);
        tallCart.setData(data3);

        return view;
    }
}
