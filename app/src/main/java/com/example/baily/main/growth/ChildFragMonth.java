package com.example.baily.main.growth;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChildFragMonth extends Fragment {
    private View view;
    private LineChart growMonthKgCart, growMonthCmCart, growMonthHeadCart, growMonthFeverCart;
    TextView monthAvgKgTxt, monthAvgCmTxt, monthAvgHeadTxt, monthAvgFeverTxt, monthDateTxt;
    float monthAvgWeight,monthAvgHeight,monthAvgHead,monthAvgFever;
    float mKgSum = 0;
    float mCmSum = 0;
    float mHeadSum = 0;
    float mFeverSum = 0;
    ImageView monthBeforeBtn,monthAfterBtn;
    String monthStartDate, monthEndDate;
    Calendar mCal, mPlusCal;
    Date date = new Date();
    SimpleDateFormat sFormat;

    public static ChildFragMonth newInstance(){
        ChildFragMonth childFragMonth = new ChildFragMonth();
        return childFragMonth;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.growth_child_frag_month,container,false);
        growMonthKgCart = view.findViewById(R.id.growthMonthKgLineCart);
        growMonthCmCart = view.findViewById(R.id.growthMonthCmLineCart);
        growMonthHeadCart = view.findViewById(R.id.growthMonthHeadLineCart);
        growMonthFeverCart = view.findViewById(R.id.growthMonthFeverLineCart);
        monthAvgKgTxt = view.findViewById(R.id.monthAvgKgTxt);
        monthAvgCmTxt = view.findViewById(R.id.monthAvgCmTxt);
        monthAvgHeadTxt = view.findViewById(R.id.monthAvgHeadTxt);
        monthAvgFeverTxt = view.findViewById(R.id.monthAvgFeverTxt);
        monthDateTxt = view.findViewById(R.id.monthDateTxt);
        monthBeforeBtn = (ImageView)view.findViewById(R.id.monthBeforeBtn);
        monthAfterBtn = (ImageView)view.findViewById(R.id.monthAfterBtn);

        sFormat = new SimpleDateFormat("yyyy년");
        mCal = Calendar.getInstance();
        mPlusCal = Calendar.getInstance();

        mCal.setTime(date);
        monthStartDate =sFormat.format(mCal.getTime());
        monthDateTxt.setText(monthStartDate);
        /*
        mPlusCal.setTime(date);
        mPlusCal.add(Calendar.DATE, +7);
        monthEndDate = sFormat.format(mPlusCal.getTime());
        monthDateTxt.setText(monthStartDate+" ~ "+monthEndDate);
         */

        monthBeforeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCal.add(Calendar.YEAR, -1);
                monthStartDate =sFormat.format(mCal.getTime());
                monthDateTxt.setText(monthStartDate);
                /*
                mPlusCal.add(Calendar.YEAR, -1);
                monthEndDate = sFormat.format(mPlusCal.getTime());
                monthDateTxt.setText(monthStartDate+" ~ "+monthEndDate);
                 */
            }
        });
        monthAfterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCal.add(Calendar.YEAR, +1);
                monthStartDate =sFormat.format(mCal.getTime());
                monthDateTxt.setText(monthStartDate);
                /*
                mPlusCal.add(Calendar.YEAR, +1);
                monthEndDate = sFormat.format(mPlusCal.getTime());
                monthDateTxt.setText(monthStartDate+" ~ "+monthEndDate);
                 */
            }
        });

        ArrayList<Entry> kgValues = new ArrayList<>();
        ArrayList<Entry> cmValues = new ArrayList<>();
        ArrayList<Entry> headValues = new ArrayList<>();
        ArrayList<Entry> feverValues = new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            float val = (float) (Math.random() * 10);
            mKgSum = mKgSum + val;
            kgValues.add(new Entry(i, val));
            monthAvgWeight = mKgSum/i;
        }
        for (int i = 1; i < 13; i++) {
            float val = (float) (Math.random() * 10);
            mCmSum = mCmSum + val;
            cmValues.add(new Entry(i, val));
            monthAvgHeight = mCmSum/i;
        }
        for (int i = 1; i < 13; i++) {
            float val = (float) (Math.random() * 10);
            mHeadSum = mHeadSum + val;
            headValues.add(new Entry(i, val));
            monthAvgHead = mHeadSum/i;
        }
        for (int i = 1; i < 13; i++) {
            float val = (float) 36.5;
            mFeverSum = mFeverSum + val;
            feverValues.add(new Entry(i, val));
            monthAvgFever = mFeverSum/i;
        }
        LineDataSet dayKg,dayCm,dayHead,dayFever;
        dayKg = new LineDataSet(kgValues, "몸무게");
        dayCm = new LineDataSet(cmValues, "신장");
        dayHead = new LineDataSet(headValues, "머리둘레");
        dayFever = new LineDataSet(feverValues, "체온");


        ArrayList<ILineDataSet> dayKgDataSets = new ArrayList<>();
        ArrayList<ILineDataSet> dayCmDataSets = new ArrayList<>();
        ArrayList<ILineDataSet> dayHeadDataSets = new ArrayList<>();
        ArrayList<ILineDataSet> dayFeverDataSets = new ArrayList<>();

        dayKgDataSets.add(dayKg); // add the data sets
        dayCmDataSets.add(dayCm);
        dayHeadDataSets.add(dayHead);
        dayFeverDataSets.add(dayFever);

        // create a data object with the data sets
        LineData dayKgData = new LineData(dayKgDataSets);
        LineData dayCmData = new LineData(dayCmDataSets);
        LineData dayHeadData = new LineData(dayHeadDataSets);
        LineData dayFeverData = new LineData(dayFeverDataSets);


        // black lines and points
        dayKg.setColor(Color.BLACK);
        dayKg.setCircleColor(Color.BLACK);

        dayCm.setColor(Color.RED);
        dayCm.setCircleColor(Color.RED);

        dayHead.setColor(Color.BLUE);
        dayHead.setCircleColor(Color.BLUE);

        dayFever.setColor(Color.GREEN);
        dayFever.setCircleColor(Color.GREEN);

        //몸무게 차트 속성
        XAxis kgXAxis = growMonthKgCart.getXAxis(); // x 축 설정
        kgXAxis.setPosition(XAxis.XAxisPosition.TOP); //x 축 표시에 대한 위치 설정
        kgXAxis.setLabelCount(12, true); //X축의 데이터를 최대 몇개 까지 나타낼지에 대한 설정 5개 force가 true 이면 반드시 보여줌

        YAxis kgYAxisLeft = growMonthKgCart.getAxisLeft(); //Y축의 왼쪽면 설정
        kgYAxisLeft.setDrawLabels(false);
        kgYAxisLeft.setDrawAxisLine(false);
        kgYAxisLeft.setDrawGridLines(false);
        YAxis kgYAxisRight = growMonthKgCart.getAxisRight(); //Y축의 오른쪽면 설정
        kgYAxisRight.setLabelCount(4, true);

        // set data
        growMonthKgCart.setDescription(null);
        growMonthKgCart.setData(dayKgData);
        monthAvgKgTxt.setText(monthAvgWeight+" kg");

        //신장 차트 속성
        XAxis cmXAxis = growMonthCmCart.getXAxis(); // x 축 설정
        cmXAxis.setPosition(XAxis.XAxisPosition.TOP); //x 축 표시에 대한 위치 설정
        cmXAxis.setLabelCount(12, true); //X축의 데이터를 최대 몇개 까지 나타낼지에 대한 설정 5개 force가 true 이면 반드시 보여줌

        YAxis cmYAxisLeft = growMonthCmCart.getAxisLeft(); //Y축의 왼쪽면 설정
        cmYAxisLeft.setDrawLabels(false);
        cmYAxisLeft.setDrawAxisLine(false);
        cmYAxisLeft.setDrawGridLines(false);
        YAxis cmYAxisRight = growMonthCmCart.getAxisRight(); //Y축의 오른쪽면 설정
        cmYAxisRight.setLabelCount(4, true);

        growMonthCmCart.setDescription(null);
        growMonthCmCart.setData(dayCmData);
        monthAvgCmTxt.setText(monthAvgHeight+" cm");


        //머리둘레 차트 속성
        XAxis headXAxis = growMonthHeadCart.getXAxis(); // x 축 설정
        headXAxis.setPosition(XAxis.XAxisPosition.TOP); //x 축 표시에 대한 위치 설정
        headXAxis.setLabelCount(12, true); //X축의 데이터를 최대 몇개 까지 나타낼지에 대한 설정 5개 force가 true 이면 반드시 보여줌

        YAxis headYAxisLeft = growMonthHeadCart.getAxisLeft(); //Y축의 왼쪽면 설정
        headYAxisLeft.setDrawLabels(false);
        headYAxisLeft.setDrawAxisLine(false);
        headYAxisLeft.setDrawGridLines(false);
        YAxis headYAxisRight = growMonthHeadCart.getAxisRight(); //Y축의 오른쪽면 설정
        headYAxisRight.setLabelCount(4, true);

        growMonthHeadCart.setDescription(null);
        growMonthHeadCart.setData(dayHeadData);
        monthAvgHeadTxt.setText(monthAvgHead+" cm");


        //체온 차트 속성
        XAxis feverXAxis = growMonthFeverCart.getXAxis(); // x 축 설정
        feverXAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x 축 표시에 대한 위치 설정
        feverXAxis.setLabelCount(12, true); //X축의 데이터를 최대 몇개 까지 나타낼지에 대한 설정 5개 force가 true 이면 반드시 보여줌

        YAxis feverYAxisLeft = growMonthFeverCart.getAxisLeft(); //Y축의 왼쪽면 설정
        feverYAxisLeft.setDrawLabels(false);
        feverYAxisLeft.setDrawAxisLine(false);
        feverYAxisLeft.setDrawGridLines(false);
        YAxis feverYAxisRight = growMonthFeverCart.getAxisRight(); //Y축의 오른쪽면 설정
        feverYAxisRight.setLabelCount(4, true);


        growMonthFeverCart.setDescription(null);
        growMonthFeverCart.setData(dayFeverData);
        monthAvgFeverTxt.setText(monthAvgFever+" °C");
        return view;
    }
}
