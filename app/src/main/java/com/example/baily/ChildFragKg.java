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

public class ChildFragKg extends Fragment {
    private View view;
    private LineChart kgCart;

    public static ChildFragKg newInstance(){
        ChildFragKg childFragKg = new ChildFragKg();
        return childFragKg;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.standard_child_frag_kg,container,false);

        kgCart = view.findViewById(R.id.kgLineCart);

        ArrayList<Entry> valuesBoy = new ArrayList<>();
        ArrayList<Entry> valuesGirl = new ArrayList<>();



        //남아 표준 그래프(몸무게) 배열 값 삽입
        float[] standardKgBoy = new float[25];
        standardKgBoy[0] = (float)3.35;
        standardKgBoy[1] = (float)4.47;
        standardKgBoy[2] = (float)5.57;
        standardKgBoy[3] = (float)6.38;
        standardKgBoy[4] = (float)7.00;
        standardKgBoy[5] = (float)7.51;
        standardKgBoy[6] = (float)7.93;
        standardKgBoy[7] = (float)8.30;
        standardKgBoy[8] = (float)8.62;
        standardKgBoy[9] = (float)8.90;
        standardKgBoy[10] = (float)9.16;
        standardKgBoy[11] = (float)9.41;
        standardKgBoy[12] = (float)9.65;
        standardKgBoy[13] = (float)9.87;
        standardKgBoy[14] = (float)10.10;
        standardKgBoy[15] = (float)10.31;
        standardKgBoy[16] = (float)10.52;
        standardKgBoy[17] = (float)10.73;
        standardKgBoy[18] = (float)10.94;
        standardKgBoy[19] = (float)11.14;
        standardKgBoy[20] = (float)11.35;
        standardKgBoy[21] = (float)11.55;
        standardKgBoy[22] = (float)11.75;
        standardKgBoy[23] = (float)11.95;
        standardKgBoy[24] = (float)12.15;

        //여아 표준 그래프(몸무게) 배열 값 삽입
        float[] standardKgGirl = new float[25];
        standardKgGirl[0] = (float)3.23;
        standardKgGirl[1] = (float)4.19;
        standardKgGirl[2] = (float)5.13;
        standardKgGirl[3] = (float)5.58;
        standardKgGirl[4] = (float)6.42;
        standardKgGirl[5] = (float)6.90;
        standardKgGirl[6] = (float)7.30;
        standardKgGirl[7] = (float)7.64;
        standardKgGirl[8] = (float)7.95;
        standardKgGirl[9] = (float)8.23;
        standardKgGirl[10] = (float)8.48;
        standardKgGirl[11] = (float)8.72;
        standardKgGirl[12] = (float)8.95;
        standardKgGirl[13] = (float)9.17;
        standardKgGirl[14] = (float)9.39;
        standardKgGirl[15] = (float)9.60;
        standardKgGirl[16] = (float)9.81;
        standardKgGirl[17] = (float)10.02;
        standardKgGirl[18] = (float)10.23;
        standardKgGirl[19] = (float)10.44;
        standardKgGirl[20] = (float)10.65;
        standardKgGirl[21] = (float)10.85;
        standardKgGirl[22] = (float)11.06;
        standardKgGirl[23] = (float)11.27;
        standardKgGirl[24] = (float)11.48;

        //그래프에 값 넣기
        for (int i = 0; i < 25; i++) {
            valuesBoy.add(new Entry(i,standardKgBoy[i]));
        }
        for (int i = 0; i < 25; i++) {
            valuesGirl.add(new Entry(i,standardKgGirl[i]));
        }
        LineDataSet set1;
        LineDataSet set2;

        set1 = new LineDataSet(valuesBoy, "남아 몸무게");
        set2 = new LineDataSet(valuesGirl, "여아 몸무게");

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
        kgCart.setData(data1);
        kgCart.setData(data2);

        return view;
    }
}
