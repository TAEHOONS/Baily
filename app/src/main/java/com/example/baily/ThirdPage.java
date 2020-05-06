package com.example.baily;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

public class ThirdPage extends AppCompatActivity {


    TextView tvName, tvSex, tvBrith, tvHeadline, tvHAW;

    public static Activity activity;


    private String mLoginId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_page);
        Intent intent = getIntent();
        mLoginId = intent.getStringExtra("login");


        tvName = (TextView) findViewById(R.id.tp_nameTV);
        tvSex = (TextView) findViewById(R.id.tp_sexTV);
        tvBrith = (TextView) findViewById(R.id.tp_berthTV);
        tvHeadline = (TextView) findViewById(R.id.tp_headlineTV);
        tvHAW = (TextView) findViewById(R.id.tp_HWATV);
        putDataTV();
    }


    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.tp_plusBtn: {
                MainScreen();
                break;
            }

        }

    }


    private void putDataTV() {
        Intent intent = getIntent();

        tvName.setText(intent.getStringExtra("name"));
        tvSex.setText(intent.getStringExtra("sex"));
        tvBrith.setText(intent.getExtras().getInt("year") + "년 " +
                intent.getExtras().getInt("month") + "월 " + intent.getExtras().getInt("day") + "일");
        tvHeadline.setText(intent.getStringExtra("headline"));
        tvHAW.setText(intent.getStringExtra("weight") + "Kg  " + intent.getStringExtra("height") + "cm");
    }

    private void MainScreen() {
        Intent intent = new Intent(ThirdPage.this, MainPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.putExtra("login", mLoginId);
        startActivity(intent);

        FirstPage fir = (FirstPage) FirstPage.activity;
        SecondPage scn = (SecondPage) SecondPage.activity;

        putFireStore();

        fir.finish();
        scn.finish();
        finish();
    }


    public class InfoBaby {
        // 무적권 public 으로해야 데이더 읽힘
        public String name,sex,headline,tall,weight,parents;
        public int year,month,day;

        public InfoBaby() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public InfoBaby(String name,String sex, int year,int month,int day
                ,String headline,String tall,String weight,String parents) {
            this.name=name;
            this.sex = sex;
            this.year = year;
            this.month = month;
            this.day = day;
            this.headline = headline;
            this.tall = tall;
            this.weight = weight;
            this.parents = parents;
        }

    }

    public void putFireStore(){
        Log.w("입력", "입력시작");
        Intent in = getIntent();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Log.w("입력", "class 에 넣기");
        InfoBaby baby = new InfoBaby(in.getStringExtra("name")
                ,in.getStringExtra("sex")
                ,in.getExtras().getInt("year")
                ,in.getExtras().getInt("month")
                ,in.getExtras().getInt("day")
                ,in.getStringExtra("headline")
                ,in.getStringExtra("height")
                ,in.getStringExtra("weight")
                ,mLoginId);

        Log.w("입력", "db 입력");
        db.collection("baby")
                .add(baby)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("입력", "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("입력", "Error adding document", e);
                    }
                });
    }

}
