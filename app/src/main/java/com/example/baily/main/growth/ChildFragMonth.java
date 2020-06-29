package com.example.baily.main.growth;

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.baily.DBlink;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static com.google.common.base.Ascii.FF;

public class ChildFragMonth extends Fragment {

    private DBlink helper;
    private SQLiteDatabase db;
    private View view;
    private LineChart growMonthKgCart, growMonthCmCart, growMonthHeadCart, growMonthFeverCart;
    private SwipeRefreshLayout mSwipeLayout;
    TextView monthAvgKgTxt, monthAvgCmTxt, monthAvgHeadTxt, monthAvgFeverTxt, monthDateTxt;
    float monthAvgWeight, monthAvgHeight, monthAvgHead, monthAvgFever, mKgSum = 0, mCmSum = 0, mHeadSum = 0, mFeverSum = 0;
    Boolean btnCk;//버튼을 눌렀는가 체크
    Boolean abBtn;//이전버튼, 이후버튼 구분하기위한 체크

    ImageView monthBeforeBtn, monthAfterBtn;//이전 이후 버튼
    String monthStartDate, mToday;
    Calendar mCal, cal;
    Date date = new Date();
    SimpleDateFormat sFormat, mSimple, readChartD;
    int maxDay, bMaxDay, aMaxDay, mMaxDay;//마지막일

    int dbVersion = 3;
    String dbName = "user.db", mId, mBabyname, mBabyBirthYear;
    String[] SearchDay, mArrKg, mArrCm, mArrHead, mArrFever;
    float[] monthArrKg, monthArrCm, monthArrHead, monthArrFever;
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
        readChartD = new SimpleDateFormat("yyyy년 MM월 dd일");
        mToday = sFormat.format(date); //이번년도

        mCal = Calendar.getInstance();
        cal = Calendar.getInstance();
        cal.setTime(date);
        mMaxDay = cal.getActualMaximum(Calendar.DATE);
        mCal.set(mCal.get(Calendar.YEAR), Calendar.DAY_OF_MONTH, mCal.getActualMinimum(Calendar.DATE));

        Log.d("mCal의 날짜", "설정된 날짜: "+readChartD.format(mCal.getTime()));

        usingDB(container);

        SearchDay = new String[368];
        mArrKg = new String[368];
        mArrCm = new String[368];
        mArrHead = new String[368];
        mArrFever = new String[368];
        monthArrKg = new float[14];
        monthArrCm = new float[14];
        monthArrHead = new float[14];
        monthArrFever = new float[14];

        mCal.setTime(date);
        monthStartDate = sFormat.format(mCal.getTime());
        monthDateTxt.setText(monthStartDate);

        monthString();



        btnCk = false;
        abBtn = false;
        getDBdata();
        //해당 달의 마지막 날
        maxDay = mCal.getActualMaximum(Calendar.DATE);

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
                ArryClear(mArrKg, mArrCm, mArrHead, mArrFever, monthArrKg, monthArrCm, monthArrHead, monthArrFever);
                //if (monthStartDate.equals(mBabyBirthYear)) {//아이의 출생년도랑 비교해야함
                int a=Integer.valueOf(monthStartDate.substring(0,4));
                    a-=1;
                mCal.set(a,1,1);
                monthStartDate = sFormat.format(mCal.getTime());

                if (monthStartDate.equals("2015년")) {//아이의 출생년도랑 비교해야함
                    Toast.makeText(getActivity(), mBabyBirthYear+" 이전의 기록은 확인불가합니다.", Toast.LENGTH_SHORT).show();
                    a+=1;
                    mCal.set(a,1,1);
                    monthStartDate = sFormat.format(mCal.getTime());

                } else {


                    monthDateTxt.setText(monthStartDate);


                    monthString();
                    btnCk = true;
                    abBtn = false;
                    getDBdata();
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

                ArryClear(mArrKg, mArrCm, mArrHead, mArrFever, monthArrKg, monthArrCm, monthArrHead, monthArrFever);

                int a=Integer.valueOf(monthStartDate.substring(0,4));
                a+=1;
                mCal.set(a,1,1);
                monthStartDate = sFormat.format(mCal.getTime());

                Log.d("연도 변경", "년도 옮기기" + monthStartDate);


                if (monthStartDate.equals("2021년")) {
                    Toast.makeText(getActivity(), "다음 년도 기록이 없습니다.", Toast.LENGTH_SHORT).show();
                    a-=1;
                    mCal.set(a,1,1);
                    monthStartDate = sFormat.format(mCal.getTime());
                } else {

                    monthStartDate = sFormat.format(mCal.getTime());
                    monthDateTxt.setText(monthStartDate);
                    aMaxDay = mCal.getActualMaximum(Calendar.DATE); //현재 달의 마지막날
                    mMaxDay = aMaxDay;
                    Log.d("이후 달의 마지막날", "이번달의 마지막 날은?==============" + aMaxDay);


                    monthString();
                    btnCk = true;
                    abBtn = true;
                    getDBdata();
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
                ArryClear(mArrKg, mArrCm, mArrHead, mArrFever, monthArrKg, monthArrCm, monthArrHead, monthArrFever);

                monthString();
                getDBdata();
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
        mKgDataSets.add(AvgData(monthAvgWeight));
        mCmDataSets.add(mCm);
        mCmDataSets.add(AvgData(monthAvgHeight));
        mHeadDataSets.add(mHead);
        mHeadDataSets.add(AvgData(monthAvgHead));
        mFeverDataSets.add(mFever);
        mFeverDataSets.add(AvgData(monthAvgFever));

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

        // 차트 x 에 라벨 넣기
        wXAxis.setValueFormatter(new IndexAxisValueFormatter(XarMonth));

        YAxis wYAxisLeft = growMonthCart.getAxisLeft(); //Y축의 왼쪽면 설정

        wYAxisLeft.setDrawLabels(false);
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

    private float dataStack(float sum, ArrayList<Entry> values, float avg, float[] AvgArr) {
        int count = 0;
        float val = 0;

        for (int i = 0; i < 12; i++) {
            if (AvgArr[i] != 0 && Float.isNaN(AvgArr[i]) == false) {
                val += AvgArr[i];
                values.add(new Entry(i, AvgArr[i]));
                count += 1;
            }
        }
        avg = val / count;
        return avg;
    }

    // 그래프 데이터 넣기용
    private void SetGraphData() {
        kgValues.clear();
        cmValues.clear();
        headValues.clear();
        feverValues.clear();

        XarMonth = getDate();
        monthAvgWeight = dataStack(mKgSum, kgValues, monthAvgWeight, monthArrKg);
        monthAvgHeight = dataStack(mCmSum, cmValues, monthAvgHeight, monthArrCm);
        monthAvgHead = dataStack(mHeadSum, headValues, monthAvgHead, monthArrHead);
        monthAvgFever = dataStack(mFeverSum, feverValues, monthAvgFever, monthArrFever);

        // 그래프 평균값 글자넣기
        monthAvgKgTxt.setText(String.format("%.2f", monthAvgWeight) + " kg");
        monthAvgCmTxt.setText(String.format("%.2f", monthAvgHeight) + " cm");
        monthAvgHeadTxt.setText(String.format("%.2f", monthAvgHead) + " cm");
        monthAvgFeverTxt.setText(String.format("%.2f", monthAvgFever) + " °C");

    }

    // chart X좌표 글자 넣기
    public ArrayList<String> getDate() {

        ArrayList<String> label = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            label.add(i + "월");
        }
        return label;
    }

    // 차트 변경 적용
    private void ChartChange(LineChart chart) {
        chart.notifyDataSetChanged();
        chart.invalidate();
    }

    // DB 연결
    private void usingDB(ViewGroup container) {
        helper = new DBlink(container.getContext(), dbName, null, dbVersion);
        db = helper.getWritableDatabase();

        String sql = "select * from thisusing where _id=1"; // 검색용
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            mId = cursor.getString(1);
            mBabyname = cursor.getString(2);
            Log.d("Home", "db받기 id = " + mId + "  현재 아기 = " + mBabyname);
        }

        sql = "select * from baby where name='"+mBabyname+"'"; // 검색용
        cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            mBabyBirthYear = Integer.valueOf(cursor.getInt(3))+"년";

        }

        cursor.close();
    }

    // 현재값 받기
    private void getDBdata() {
        Log.d("searchDay", "start ");
        // 현재 사용 아기데이터
        for (int i = 0; i < 368; i++) {
            String sql = "select * from growlog where name='" + mBabyname + "'AND date='" + SearchDay[i] + "'"; // 검색용
            Cursor c = db.rawQuery(sql, null);
            while (c.moveToNext()) {
                //SearchDay[i] = c.getString(3);
                mArrKg[i] = c.getString(2);
                mArrCm[i] = c.getString(3);
                mArrHead[i] = c.getString(4);
                mArrFever[i] = c.getString(5);
                Log.d("searchWeek", "SearchDay = " + SearchDay[i] + " ,mArrKg = "
                        + mArrKg[i] + "   ,mArrCm = " + mArrCm[i] + "   ,mArrHead = " + mArrHead[i] + "   ,mArrFever = " + mArrFever[i]);
            }
            c.close();
        }
        monthAvg(mArrKg, monthArrKg);
        monthAvg(mArrCm, monthArrCm);
        monthAvg(mArrHead, monthArrHead);
        monthAvg(mArrFever, monthArrFever);

    }


    private void monthAvg(String[] dayArr, float[] YearArr) {
        Log.d("weekarry", "실행 mMaxDay= "+mMaxDay);
        int monthCount=1,avgCount=0;
        for (int i = 0; i < 367; i++) {
            if (dayArr[i] != null) {
                try {
                    YearArr[monthCount] += Float.parseFloat(dayArr[i].trim());
                    Log.d("weekarry", "i="+i+"    "+monthCount+" - YearArr[monthCount]="+YearArr[monthCount]+" dayArr[i]= "+dayArr[i]);
                    avgCount+=1;
                } catch (Exception e) { }
            }
            if((i%30==0)&&i>0) {
                YearArr[monthCount]= YearArr[monthCount]/avgCount;
                monthCount += 1;
                avgCount=0;
            }
        }


    }

    public void monthString() {
        Log.d("ekffur", "year: "+mCal.get(Calendar.YEAR)+"  , month = "+mCal.getActualMinimum(Calendar.MONTH));
        int a=Integer.valueOf(monthStartDate.substring(0,4));
        mCal.set(a,1,1);


        SearchDay[0] = readChartD.format((mCal.getTime()));
        for (int i = 0; i < 367; i++) {
            mCal.add(Calendar.DATE, +1);
            SearchDay[i + 1] = readChartD.format((mCal.getTime()));

        }

    }

    private LineDataSet AvgData(float avgData) {
        LineDataSet avgDataSet;
        ArrayList<Entry> avgValues = new ArrayList<>();

        for (int i = 0; i <= 11; i++) {
            avgValues.add(new Entry(i, avgData));
        }

        avgDataSet = new LineDataSet(avgValues, null);

        avgDataSet.setDrawValues(false);//데이터 값 없애기
        avgDataSet.setDrawCircles(false);//포인트 원 없애기
        // 그래프 색 넣기
        GraphLineColor(avgDataSet, Color.argb(0, 255, 0, 0));

        return avgDataSet;
    }

    private void ArryClear(String[] a, String[] b, String[] c, String[] d, float[] q, float[] w, float[] e, float[] r) {
        Arrays.fill(a, null);
        Arrays.fill(b, null);
        Arrays.fill(c, null);
        Arrays.fill(d, null);
        Arrays.fill(q, 0);
        Arrays.fill(w, 0);
        Arrays.fill(e, 0);
        Arrays.fill(r, 0);
    }


}
