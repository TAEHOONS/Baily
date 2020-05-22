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
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class ChildFragHead  extends Fragment {
    private View view;
    private LineChart headCart;

    public static ChildFragHead newInstance(){
        ChildFragHead childFragHead = new ChildFragHead();
        return childFragHead;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.standard_child_frag_head,container,false);

        headCart = view.findViewById(R.id.headLineCart);

        ArrayList<Entry> valuesBoy = new ArrayList<>();
        ArrayList<Entry> valuesGirl = new ArrayList<>();



        //남아 표준 그래프(머리둘레) 배열 값 삽입
        float[] standardHeadBoy = new float[25];
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

        //여아 표준 그래프(머리둘레) 배열 값 삽입
        float[] standardHeadGirl = new float[25];
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

        //그래프에 값 넣기
        for (int i = 0; i < 25; i++) {
            valuesBoy.add(new Entry(i,standardHeadBoy[i]));
        }
        for (int i = 0; i < 25; i++) {
            valuesGirl.add(new Entry(i,standardHeadGirl[i]));
        }
        LineDataSet set1;
        LineDataSet set2;

        set1 = new LineDataSet(valuesBoy, "남아 머리둘레");
        set2 = new LineDataSet(valuesGirl, "여아 머리둘레");

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the data sets
        dataSets.add(set2);

        // create a data object with the data sets
        LineData data1 = new LineData(dataSets);
        LineData data2 = new LineData(dataSets);

        // black lines and points
        set1.setColor(Color.BLUE);
        set1.setCircleColor(Color.BLUE);
        set2.setColor(Color.RED);
        set2.setCircleColor(Color.RED);
        // set data
        headCart.setData(data1);
        headCart.setData(data2);


        return view;
    }
}
