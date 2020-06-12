package com.example.baily.main.standard;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class ChildFragKg extends Fragment {
    private View view;
    private LineChart kgCart;

    String dbName = "user.db";
    int dbVersion = 3, BYear, BMonth, BDay;
    // mId= 현재 사용 id, baby
    private String mId, mBabyname;
    private DBlink helper;
    private SQLiteDatabase db;
    String k;

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
        ArrayList<Entry> valuesBaby = new ArrayList<>();//내애기



        //남아 표준 그래프(몸무게) 배열 값 삽입
        float[] standardKgBoy = new float[73];
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
        standardKgBoy[25] = (float)12.33;
        standardKgBoy[26] = (float)12.52;
        standardKgBoy[27] = (float)12.71;
        standardKgBoy[28] = (float)12.91;
        standardKgBoy[29] = (float)13.10;
        //30개월
        standardKgBoy[30] = (float)13.30;
        standardKgBoy[31] = (float)13.53;
        standardKgBoy[32] = (float)13.76;
        standardKgBoy[33] = (float)14.00;
        standardKgBoy[34] = (float)14.24;
        standardKgBoy[35] = (float)14.49;
        standardKgBoy[36] = (float)14.74;
        standardKgBoy[37] = (float)14.91;
        standardKgBoy[38] = (float)15.09;
        standardKgBoy[39] = (float)15.26;
        standardKgBoy[40] = (float)15.43;
        standardKgBoy[41] = (float)15.61;
        standardKgBoy[42] = (float)15.78;
        standardKgBoy[43] = (float)15.95;
        standardKgBoy[44] = (float)16.12;
        standardKgBoy[45] = (float)16.30;
        standardKgBoy[46] = (float)16.47;
        standardKgBoy[47] = (float)16.65;
        standardKgBoy[48] = (float)16.83;
        standardKgBoy[49] = (float)17.00;
        standardKgBoy[50] = (float)17.18;
        standardKgBoy[51] = (float)17.35;
        standardKgBoy[52] = (float)17.53;
        standardKgBoy[53] = (float)17.71;
        standardKgBoy[54] = (float)17.89;
        standardKgBoy[55] = (float)18.07;
        standardKgBoy[56] = (float)18.25;
        standardKgBoy[57] = (float)18.43;
        standardKgBoy[58] = (float)18.60;
        standardKgBoy[59] = (float)18.78;
        //60개월
        standardKgBoy[60] = (float)18.96;
        standardKgBoy[61] = (float)19.15;
        standardKgBoy[62] = (float)19.33;
        standardKgBoy[63] = (float)19.52;
        standardKgBoy[64] = (float)19.71;
        standardKgBoy[65] = (float)19.89;
        standardKgBoy[66] = (float)20.08;
        standardKgBoy[67] = (float)20.28;
        standardKgBoy[68] = (float)20.49;
        standardKgBoy[69] = (float)20.70;
        standardKgBoy[70] = (float)20.91;
        standardKgBoy[71] = (float)21.12;
        standardKgBoy[72] = (float)21.34;



        //여아 표준 그래프(몸무게) 배열 값 삽입
        float[] standardKgGirl = new float[73];
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
        standardKgGirl[25] = (float)11.68;
        standardKgGirl[26] = (float)11.88;
        standardKgGirl[27] = (float)12.08;
        standardKgGirl[28] = (float)12.29;
        standardKgGirl[29] = (float)12.50;
        //30개월부터
        standardKgGirl[30] = (float)12.71;
        standardKgGirl[31] = (float)12.96;
        standardKgGirl[32] = (float)13.21;
        standardKgGirl[33] = (float)13.46;
        standardKgGirl[34] = (float)13.70;
        standardKgGirl[35] = (float)13.95;
        standardKgGirl[36] = (float)14.20;
        standardKgGirl[37] = (float)14.37;
        standardKgGirl[38] = (float)14.53;
        standardKgGirl[39] = (float)14.70;
        standardKgGirl[40] = (float)14.87;
        standardKgGirl[41] = (float)15.05;
        standardKgGirl[42] = (float)15.22;
        standardKgGirl[43] = (float)15.39;
        standardKgGirl[44] = (float)15.57;
        standardKgGirl[45] = (float)15.74;
        standardKgGirl[46] = (float)15.91;
        standardKgGirl[47] = (float)16.09;
        standardKgGirl[48] = (float)16.26;
        standardKgGirl[49] = (float)16.43;
        standardKgGirl[50] = (float)16.60;
        standardKgGirl[51] = (float)16.70;
        standardKgGirl[52] = (float)16.95;
        standardKgGirl[53] = (float)17.12;
        standardKgGirl[54] = (float)17.30;
        standardKgGirl[55] = (float)17.47;
        standardKgGirl[56] = (float)17.65;
        standardKgGirl[57] = (float)17.82;
        standardKgGirl[58] = (float)18.00;
        standardKgGirl[59] = (float)18.36;
        //60개월
        standardKgGirl[60] = (float)18.54;
        standardKgGirl[61] = (float)18.72;
        standardKgGirl[62] = (float)18.90;
        standardKgGirl[63] = (float)19.08;
        standardKgGirl[64] = (float)19.26;
        standardKgGirl[65] = (float)19.45;
        standardKgGirl[66] = (float)19.65;
        standardKgGirl[67] = (float)19.85;
        standardKgGirl[68] = (float)20.06;
        standardKgGirl[69] = (float)20.26;
        standardKgGirl[70] = (float)20.46;
        standardKgGirl[71] = (float)20.66;
        standardKgGirl[72] = (float)20.89;

        // 내애기 몸무게 임시데이터
        float[] standardKgBaby = new float[73];
        standardKgBaby[0] = (float)7.35;
        standardKgBaby[1] = (float)8.47;
        standardKgBaby[2] = (float)9.57;
        standardKgBaby[3] = (float)11.38;



        //그래프에 값 넣기
        for (int i = 0; i < 73; i++) {
            valuesBoy.add(new Entry(i,standardKgBoy[i]));
        }
        for (int i = 0; i < 73; i++) {
            valuesGirl.add(new Entry(i,standardKgGirl[i]));
        }
        //내 애기 그래프값넣기
        for (int i = 0; i < 4; i++) {
            valuesBaby.add(new Entry(i,standardKgBaby[i]));
        }


        LineDataSet set1;
        LineDataSet set2;
        LineDataSet set3; //내애기

        set1 = new LineDataSet(valuesBoy, "남아 몸무게");
        set2 = new LineDataSet(valuesGirl, "여아 몸무게");
        set3 = new LineDataSet(valuesBaby,"내 아이 몸무게");

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the data sets
        dataSets.add(set2);
        dataSets.add(set3);//내 아기

        // create a data object with the data sets
        LineData data1 = new LineData(dataSets);
        LineData data2 = new LineData(dataSets);
        LineData data3 = new LineData(dataSets);//내아기

        // black lines and points
        set1.setColor(Color.BLUE);
        set1.setCircleColor(Color.BLUE);
        set2.setColor(Color.RED);
        set2.setCircleColor(Color.RED);
        set3.setColor(Color.BLACK);
        set3.setCircleColor(Color.BLACK);


        XAxis xAxis = kgCart.getXAxis(); // x 축 설정
        xAxis.setPosition(XAxis.XAxisPosition.TOP); //x 축 표시에 대한 위치 설정
        // xAxis.setValueFormatter(new ChartXValueFormatter()); //X축의 데이터를 제 가공함. new ChartXValueFormatter은 Custom한 소스
        xAxis.setLabelCount(12, true); //X축의 데이터를 최대 몇개 까지 나타낼지에 대한 설정 5개 force가 true 이면 반드시 보여줌
        //xAxis.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor)); // X축 텍스트컬러설정
        //xAxis.setGridColor(ContextCompat.getColor(getContext(), R.color.textColor)); // X축 줄의 컬러 설정

        YAxis yAxisLeft = kgCart.getAxisLeft(); //Y축의 왼쪽면 설정
        yAxisLeft.setDrawLabels(false);
        yAxisLeft.setDrawAxisLine(false);
        yAxisLeft.setDrawGridLines(false);
        //yAxisLeft.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor)); //Y축 텍스트 컬러 설정
        //yAxisLeft.setGridColor(ContextCompat.getColor(getContext(), R.color.textColor)); // Y축 줄의 컬러 설정

        YAxis yAxisRight = kgCart.getAxisRight(); //Y축의 오른쪽면 설정

        kgCart.setVisibleXRangeMinimum(60 * 60 * 24 * 1000 * 5); //라인차트에서 최대로 보여질 X축의 데이터 설정
        kgCart.setDescription(null); //차트에서 Description 설정 저는 따로 안했습니다.

        // set data
        kgCart.setData(data1);
        kgCart.setData(data2);
        kgCart.setData(data3); //내아기

        return view;
    }
    //새로운시도
    // DB 연결
    private void usingDB(ViewGroup container) {
        helper = new DBlink(container.getContext(), dbName, null, dbVersion);
        db = helper.getWritableDatabase();

    }
    // 저장된 growlog DB 에 있는걸 불러와서 그래프에 넣기
    private void loadgrowLog() {

        String sql = "select * from growlog where name='" + mBabyname + "'"; // 검색용

        Cursor c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            String sqlcm, h, f, r, d;
            k = c.getString(2);
            Log.d("k값 쌓이는거", "loadgrowLog 와일 내부: "+k);
        }
        Log.d("k값 쌓이는거", "loadgrowLog 와일 끝: "+k);
    }

}
