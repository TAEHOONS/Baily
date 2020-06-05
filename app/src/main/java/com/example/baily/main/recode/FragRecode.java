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
    String sumT;
    Thread thread = null;
    AppCompatImageButton handle, nurs, bbfood, sleep, pwmilk, bowel, dosage, tem, bath, health, play;
    TextView day, nursT, bowerT, sleepT;
    SimpleDateFormat dateSet = new SimpleDateFormat("yyyyMMdd HH:mm");
    String date = dateSet.format(new Date());

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
    private static class TIME_MAXIMUM{
        public static final int SEC = 60;
        public static final int MIN = 60;
        public static final int HOUR = 24;
        public static final int DAY = 30;
        public static final int MONTH = 12;
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

               final long  start = System.currentTimeMillis();
                    if (thread != null) {
                        thread.interrupt();
                        thread = null;
                    }
               thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(flag) {final long end = System.currentTimeMillis();
                            try {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {

                                    long sum = (end - start) /1000;
                                        if (sum < TIME_MAXIMUM.SEC) {
                                            sumT = sum + "초 전";
                                        } else if ((sum /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN) {
                                            sumT = sum + "분 전";
                                        } else if ((sum /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR) {
                                            sumT = (sum) + "시간 전";
                                        } else if ((sum /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY) {
                                            sumT = (sum) + "일 전";
                                        } else if ((sum /= TIME_MAXIMUM.DAY) < TIME_MAXIMUM.MONTH) {
                                            sumT = (sum) + "달 전";
                                        } else {
                                            sumT = (sum) + "년 전";
                                        }
                                        nursT.setText(sumT);
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

                recodeAdapter.addItem(new RecodeData(getNowTime(),"수면"));
                recyclerView.setAdapter(recodeAdapter);
                final long start = System.currentTimeMillis();
                if (thread != null) {
                    thread.interrupt();
                    thread = null;
                }
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(flag) {final long  end = System.currentTimeMillis();
                            try {

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        long sum = (end - start) /1000;
                                        if (sum < TIME_MAXIMUM.SEC) {
                                            sumT = sum + "초 전";
                                        } else if ((sum /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN) {
                                            sumT = sum + "분 전";
                                        } else if ((sum /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR) {
                                            sumT = (sum) + "시간 전";
                                        } else if ((sum /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY) {
                                            sumT = (sum) + "일 전";
                                        } else if ((sum /= TIME_MAXIMUM.DAY) < TIME_MAXIMUM.MONTH) {
                                            sumT = (sum) + "달 전";
                                        } else {
                                            sumT = (sum) + "년 전";
                                        }
                                        sleepT.setText(sumT);
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
                final long start = System.currentTimeMillis();
                if (thread != null) {
                    thread.interrupt();
                    thread = null;
                }
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(flag) {final long end = System.currentTimeMillis();
                            try {

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        long sum = (end - start) /1000;
                                        if (sum < TIME_MAXIMUM.SEC) {
                                            sumT = sum + "초 전";
                                        } else if ((sum /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN) {
                                            sumT = sum + "분 전";
                                        } else if ((sum /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR) {
                                            sumT = (sum) + "시간 전";
                                        } else if ((sum /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY) {
                                            sumT = (sum) + "일 전";
                                        } else if ((sum /= TIME_MAXIMUM.DAY) < TIME_MAXIMUM.MONTH) {
                                            sumT = (sum) + "달 전";
                                        } else {
                                            sumT = (sum) + "년 전";
                                        }
                                        bowerT.setText(sumT);
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