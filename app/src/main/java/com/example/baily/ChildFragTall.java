package com.example.baily;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
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



        //남아 표준 그래프(키) 배열 값 삽입
        float[] standardTallBoy = new float[25];
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

        //여아 표준 그래프(키) 배열 값 삽입
        float[] standardTallGirl = new float[25];
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

        //그래프에 값 넣기
        for (int i = 0; i < 25; i++) {
            valuesBoy.add(new Entry(i,standardTallBoy[i]));
        }
        for (int i = 0; i < 25; i++) {
            valuesGirl.add(new Entry(i,standardTallGirl[i]));
        }
        LineDataSet set1;
        LineDataSet set2;

        set1 = new LineDataSet(valuesBoy, "남아 신장");
        set2 = new LineDataSet(valuesGirl, "여아 신장");

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the data sets
        dataSets.add(set2);

        // create a data object with the data sets
        LineData data1 = new LineData(dataSets);
        LineData data2 = new LineData(dataSets);

        // black lines and points
        set1.setColor(Color.RED);
        set1.setCircleColor(Color.RED);
        set2.setColor(Color.BLUE);
        set2.setCircleColor(Color.BLUE);
        // set data
        tallCart.setData(data1);
        tallCart.setData(data2);

        return view;
    }
}
