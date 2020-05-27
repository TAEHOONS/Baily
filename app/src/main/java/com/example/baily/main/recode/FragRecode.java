package com.example.baily.main.recode;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baily.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FragRecode extends Fragment {
    private View v;
    int count = 1;
    int recode = 0;
    RecodeAdapter recodeAdapter;
    RecyclerView recyclerView;
    private String sel;

    AppCompatImageButton handle, nurs, bbfood, sleep, pwmilk, bowel, dosage, tem, bath, health, play;
    TextView day, nursT, bowerT, sleepT;
    SimpleDateFormat dateSet = new SimpleDateFormat("yyyyMMdd HH:mm");
    String date = dateSet.format(new Date());

    long end = System.currentTimeMillis();
    String dateInit = null;
    private List<String> list;
    private RecodeSearchAdapter adapter;      // 리스트뷰에 연결할 아답터
    private ArrayList<String> arraylist;

    private String getNowTime() {
        // 현재시간을 msec 으로 구한다.
        long now = System.currentTimeMillis();
        // 현재시간을 date 변수에 저장한다.
        Date date = new Date(now);
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        SimpleDateFormat sdfNow = new SimpleDateFormat("HH:mm");

        return sdfNow.format(date);
    }

    public static FragRecode newInstance(){
        FragRecode fragRecode = new FragRecode();
        return fragRecode;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frag_recode, container, false);

        final boolean flag = true;

        final Handler handler = new Handler();

        handle = v.findViewById(R.id.handle);
        nurs = v.findViewById(R.id.nursing_btn);
        bbfood = v.findViewById(R.id.babyfood_btn);
        sleep = v.findViewById(R.id.sleep_btn);
        pwmilk = v.findViewById(R.id.powderedmilk_btn);
        bowel = v.findViewById(R.id.bowel_btn);
        dosage = v.findViewById(R.id.dosage_btn);
        tem = v.findViewById(R.id.temperature_btn);
        bath = v.findViewById(R.id.bath_btn);
        health = v.findViewById(R.id.health_btn);
        play = v.findViewById(R.id.play_btn);

        day = v.findViewById(R.id.whenDate);
        day.setText("   " + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "월 "
                + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "일");

        nursT = v.findViewById(R.id.last_nursing);
        sleepT = v.findViewById(R.id.last_sleep);
        bowerT = v.findViewById(R.id.last_bower);

        if(getArguments() != null){
            sel = getArguments().getString("select");
            FragmentTransaction transaction = getActivity()
                    .getSupportFragmentManager().beginTransaction();
                sleepT.setText(sel);
            transaction.commit();

        }



        recyclerView = v.findViewById(R.id.rec_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false); //레이아웃매니저 생성
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);


        recyclerView.setLayoutManager(layoutManager);
        recodeAdapter = new RecodeAdapter(getActivity().getApplicationContext());
        recyclerView.setAdapter(recodeAdapter);

        nurs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recodeAdapter.addItem(new RecodeData(getNowTime(),"수유"));
                recyclerView.setAdapter(recodeAdapter);

                String dateInit = dateSet.format(new Date());
                final long start = System.currentTimeMillis();

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(flag) {
                            try {
                                long end = System.currentTimeMillis();

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {

                                    }
                                });
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                thread.start();


            }
        });




        bbfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recodeAdapter.addItem(new RecodeData(getNowTime(),"이유식"));
                recyclerView.setAdapter(recodeAdapter);
            }
        });
        sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recodeAdapter.addItem(new RecodeData(getNowTime(),"낮잠"));
                recyclerView.setAdapter(recodeAdapter);
            }
        });
        pwmilk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recodeAdapter.addItem(new RecodeData(getNowTime(),"분유"));
                recyclerView.setAdapter(recodeAdapter);
            }
        });
        bowel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recodeAdapter.addItem(new RecodeData(getNowTime(),"배변"));
                recyclerView.setAdapter(recodeAdapter);
            }
        });
        dosage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recodeAdapter.addItem(new RecodeData(getNowTime(),"투약"));
                recyclerView.setAdapter(recodeAdapter);
            }
        });
        tem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recodeAdapter.addItem(new RecodeData(getNowTime(),"온도체크"));
                recyclerView.setAdapter(recodeAdapter);
            }
        });
        bath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recodeAdapter.addItem(new RecodeData(getNowTime(),"목욕"));
                recyclerView.setAdapter(recodeAdapter);
            }
        });
        health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recodeAdapter.addItem(new RecodeData(getNowTime(),"입원"));
                recyclerView.setAdapter(recodeAdapter);
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recodeAdapter.addItem(new RecodeData(getNowTime(),"놀이"));
                recyclerView.setAdapter(recodeAdapter);
            }
        });


        handle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count%2==0){
                    handle.setImageResource(R.drawable.selectbuttonb);
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentr, new FragRecodeSelect());
                    fragmentTransaction.commit();
                    count++;
                }else{
                    handle.setImageResource(R.drawable.selectbuttona);
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentr, new Fragment())
                            .addToBackStack(null)
                            .commit();

                    count++;
                }
            }
        });


        return v;
    }
}