package com.example.baily.main.growth;

import android.graphics.Color;
import android.os.Bundle;
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
import java.util.Calendar;
import java.util.Date;

import static com.google.common.base.Ascii.FF;

public class ChildFragMonth extends Fragment {
    private View view;
    private LineChart growMonthKgCart, growMonthCmCart, growMonthHeadCart, growMonthFeverCart;
    private SwipeRefreshLayout mSwipeLayout;
    TextView monthAvgKgTxt, monthAvgCmTxt, monthAvgHeadTxt, monthAvgFeverTxt, monthDateTxt;
    float monthAvgWeight, monthAvgHeight, monthAvgHead, monthAvgFever, mKgSum = 0, mCmSum = 0, mHeadSum = 0, mFeverSum = 0;

    ImageView monthBeforeBtn, monthAfterBtn;//이전 이후 버튼
    String monthStartDate, monthEndDate, mToday;
    Calendar mCal;
    Date date = new Date();
    SimpleDateFormat sFormat, mSimple;

    LineData mKgData, mCmData, mHeadData, mFeverData;

    ArrayList<Entry> kgValues, cmValues, headValues, feverValues;
    ArrayList<String> XarMonth;
    ArrayList<ILineDataSet> mKgDataSets, mCmDataSets, mHeadDataSets, mFeverDataSets;


    public static ChildFragMonth newInstance() {
        ChildFragMonth childFragMonth = new ChildFragMonth();
        return childFragMonth;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.growth_child_frag_month, container, false);
        mSwipeLayout = view.findViewById(R.id.mSwipeLayout);
        growMonthKgCart = view.findViewById(R.id.growthMonthKgLineCart);
        growMonthCmCart = view.findViewById(R.id.growthMonthCmLineCart);
        growMonthHeadCart = view.findViewById(R.id.growthMonthHeadLineCart);
        growMonthFeverCart = view.findViewById(R.id.growthMonthFeverLineCart);
        monthAvgKgTxt = view.findViewById(R.id.monthAvgKgTxt);
        monthAvgCmTxt = view.findViewById(R.id.monthAvgCmTxt);
        monthAvgHeadTxt = view.findViewById(R.id.monthAvgHeadTxt);
        monthAvgFeverTxt = view.findViewById(R.id.monthAvgFeverTxt);
        monthDateTxt = view.findViewById(R.id.monthDateTxt);
        monthBeforeBtn = (ImageView) view.findViewById(R.id.monthBeforeBtn);
        monthAfterBtn = (ImageView) view.findViewById(R.id.monthAfterBtn);

        mSimple = new SimpleDateFormat("MM");

        sFormat = new SimpleDateFormat("yyyy년");
        mToday = sFormat.format(date); //이번년도

        mCal = Calendar.getInstance();

        mCal.setTime(date);
        monthStartDate = sFormat.format(mCal.getTime());
        monthDateTxt.setText(monthStartDate);

        kgValues = new ArrayList<>();
        cmValues = new ArrayList<>();
        headValues = new ArrayList<>();
        feverValues = new ArrayList<>();

        // 값 셋팅하기
        SetGraphData();

        // 그래프 평균값 글자넣기
        monthAvgKgTxt.setText(String.format("%.2f", monthAvgWeight) + " kg");
        monthAvgCmTxt.setText(String.format("%.2f", monthAvgHeight) + " cm");
        monthAvgHeadTxt.setText(String.format("%.2f", monthAvgHead) + " cm");
        monthAvgFeverTxt.setText(String.format("%.2f", monthAvgFever) + " °C");

        //  중간 업데이트
        MidDataSet();

        //몸무게 차트 속성
        setGraph(growMonthKgCart, mKgData);

        //신장 차트 속성
        setGraph(growMonthCmCart, mCmData);

        //머리둘레 차트 속성
        setGraph(growMonthHeadCart, mHeadData);

        //체온 차트 속성
        setGraph(growMonthFeverCart, mFeverData);

        //이전버튼 눌렀을 때
        monthBeforeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (monthStartDate.equals("2015년")) {//아이의 출생년도랑 비교해야함
                    Toast.makeText(getActivity(), "2015년 이전의 기록은 확인불가합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    mCal.add(Calendar.YEAR, -1);
                    monthStartDate = sFormat.format(mCal.getTime());
                    monthDateTxt.setText(monthStartDate);

                    SetGraphData();
                    //  중간 업데이트
                    MidDataSet();

                    //차트 속성
                    setGraph(growMonthKgCart, mKgData);
                    setGraph(growMonthCmCart, mCmData);
                    setGraph(growMonthHeadCart, mHeadData);
                    setGraph(growMonthFeverCart, mFeverData);


                    // 바뀐 차트 적용
                    ChartChange(growMonthKgCart);
                    ChartChange(growMonthCmCart);
                    ChartChange(growMonthHeadCart);
                    ChartChange(growMonthFeverCart);
                }
            }
        });
        //이후버튼 눌렀을 때
        monthAfterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mToday.equals(monthStartDate)) {
                    Toast.makeText(getActivity(), "다음 년도 기록이 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    mCal.add(Calendar.YEAR, +1);
                    monthStartDate = sFormat.format(mCal.getTime());
                    monthDateTxt.setText(monthStartDate);

                    SetGraphData();
                    //  중간 업데이트
                    MidDataSet();

                    //차트 속성
                    setGraph(growMonthKgCart, mKgData);
                    setGraph(growMonthCmCart, mCmData);
                    setGraph(growMonthHeadCart, mHeadData);
                    setGraph(growMonthFeverCart, mFeverData);


                    // 바뀐 차트 적용
                    ChartChange(growMonthKgCart);
                    ChartChange(growMonthCmCart);
                    ChartChange(growMonthHeadCart);
                    ChartChange(growMonthFeverCart);
                }
            }
        });

        //당겨서 새로고침
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SetGraphData();
                //  중간 업데이트
                MidDataSet();

                //차트 속성
                setGraph(growMonthKgCart, mKgData);
                setGraph(growMonthCmCart, mCmData);
                setGraph(growMonthHeadCart, mHeadData);
                setGraph(growMonthFeverCart, mFeverData);


                // 바뀐 차트 적용
                ChartChange(growMonthKgCart);
                ChartChange(growMonthCmCart);
                ChartChange(growMonthHeadCart);
                ChartChange(growMonthFeverCart);

                mSwipeLayout.setRefreshing(false);
            }
        });

        return view;
    }

    private void MidDataSet() {

        LineDataSet mKg, mCm, mHead, mFever;

        mKg = new LineDataSet(kgValues, "몸무게");
        mCm = new LineDataSet(cmValues, "신장");
        mHead = new LineDataSet(headValues, "머리둘레");
        mFever = new LineDataSet(feverValues, "체온");

        // 기초 라인들 만들기
        mKgDataSets = new ArrayList<>();
        mCmDataSets = new ArrayList<>();
        mHeadDataSets = new ArrayList<>();
        mFeverDataSets = new ArrayList<>();

        // day"++"DataSets에 linedata 받은거 추가하기
        mKgDataSets.add(mKg);
        mCmDataSets.add(mCm);
        mHeadDataSets.add(mHead);
        mFeverDataSets.add(mFever);

        // 실질적 라인인 day"++"Data에 새로 값넣기
        mKgData = new LineData(mKgDataSets);
        mCmData = new LineData(mCmDataSets);
        mHeadData = new LineData(mHeadDataSets);
        mFeverData = new LineData(mFeverDataSets);

        // 그래프 색 넣기
        GraphLineColor(mKg, Color.BLACK);
        GraphLineColor(mCm, Color.RED);
        GraphLineColor(mHead, Color.BLUE);
        GraphLineColor(mFever, Color.GREEN);
    }

    // 그래프에 데이터 적용 셋팅
    private void setGraph(LineChart growMonthCart, LineData monthData) {


        XAxis wXAxis = growMonthCart.getXAxis(); // x 축 설정
        wXAxis.setPosition(XAxis.XAxisPosition.TOP); //x 축 표시에 대한 위치 설정
        wXAxis.setLabelCount(12, true); //X축의 데이터를 최대 몇개 까지 나타낼지에 대한 설정 5개 force가 true 이면 반드시 보여줌

        wXAxis.setDrawAxisLine(false);
        //wXAxis.setDrawGridLines(false);

        // 차트 x 에 라벨 넣기
        wXAxis.setValueFormatter(new IndexAxisValueFormatter(XarMonth));

        YAxis wYAxisLeft = growMonthCart.getAxisLeft(); //Y축의 왼쪽면 설정

        wYAxisLeft.setDrawLabels(false);
        //wYAxisLeft.setDrawAxisLine(false);
        wYAxisLeft.setDrawGridLines(false);

        YAxis wYAxisRight = growMonthCart.getAxisRight(); //Y축의 오른쪽면 설정

        wYAxisRight.setLabelCount(4, true);

        growMonthCart.setDescription(null);
        growMonthCart.setData(monthData);

    }

    // 그래프 컬러 적용
    private void GraphLineColor(LineDataSet line, int color) {
        line.setColor(color);
        line.setCircleColor(color);
    }

    // 그래프 데이터 넣기용
    private void SetGraphData() {
        // 그래프 평균값 글자넣기
        monthAvgKgTxt.setText(String.format("%.2f", monthAvgWeight) + " kg");
        monthAvgCmTxt.setText(String.format("%.2f", monthAvgHeight) + " cm");
        monthAvgHeadTxt.setText(String.format("%.2f", monthAvgHead) + " cm");
        monthAvgFeverTxt.setText(String.format("%.2f", monthAvgFever) + " °C");

        kgValues.clear();
        cmValues.clear();
        headValues.clear();
        feverValues.clear();

        XarMonth = getDate();
        monthAvgWeight = dataStack(mKgSum, kgValues, monthAvgWeight);
        monthAvgHeight = dataStack(mCmSum, cmValues, monthAvgHeight);
        monthAvgHead = dataStack(mHeadSum, headValues, monthAvgHead);
        monthAvgFever = dataStack(mFeverSum, feverValues, monthAvgFever);
    }

    private float dataStack(float sum, ArrayList<Entry> values, float avg) {
        for (int i = 1; i <= 12; i++) {
            float val = (float) (Math.random() * 10);
            sum = sum + val;
            values.add(new Entry(i, val));
            avg = sum / i;
        }
        return avg;
    }

    // chart X좌표 글자 넣기
    public ArrayList<String> getDate() {

        ArrayList<String> label = new ArrayList<>();
        for (int i = 0; i <= 12; i++) {
            label.add(i + "월");
        }
        return label;
    }


    // 차트 변경 적용
    private void ChartChange(LineChart chart) {
        chart.notifyDataSetChanged();
        chart.invalidate();
    }

}
