package com.example.baily.babyPlus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baily.DBlink;
import com.example.baily.R;
import com.example.baily.main.MainPage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ThirdPage extends AppCompatActivity {
    String dbName = "user.db";
    int dbVersion = 3;
    private DBlink helper;
    private SQLiteDatabase db;

    TextView tvName, tvSex, tvBrith, tvHeadline, tvHAW;
    InfoBaby baby;
    public static Activity activity;
    private CircleImageView imageview;
    String imgpath = "data/data/com.example.baily/files/";

    private String mLoginId,babypic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_page);

        usingDB();

        imageview = (CircleImageView) findViewById(R.id.tp_profileImg);
        tvName = (TextView) findViewById(R.id.tp_nameTV);
        tvSex = (TextView) findViewById(R.id.tp_sexTV);
        tvBrith = (TextView) findViewById(R.id.tp_berthTV);
        tvHeadline = (TextView) findViewById(R.id.tp_headlineTV);
        tvHAW = (TextView) findViewById(R.id.tp_HWATV);
        putDataTV();

        // path 받기
        babypic=(tvName.getText().toString() + ".jpg");
        imgpath = imgpath.concat(babypic);

        Log.d("3page", "tvName = " + (tvName.getText().toString() + ".jpg"));
        Log.d("3page", "imgpath = " + imgpath);

        //  이미지 좌표 가져와서 보기
        try {
            Bitmap bm = BitmapFactory.decodeFile(imgpath);
            imageview.setImageBitmap(bm);
            Toast.makeText(getApplicationContext(), "load ok", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "load error", Toast.LENGTH_SHORT).show();
        }
    }


    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.tp_plusBtn: {
                Log.d("3page", "putFireStore 시작");
                putFireStore();

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
        tvHAW.setText(intent.getStringExtra("height") + "cm  " + intent.getStringExtra("weight") + "Kg");


    }

    private void MainScreen() {



        Intent intent = new Intent(ThirdPage.this, MainPage.class);
        intent.putExtra("login", mLoginId);
        startActivity(intent);
    }


    public class InfoBaby {
        // 무적권 public 으로해야 데이더 읽힘
        public String name, sex, headline, tall, weight, parents,path;
        public int year, month, day;

        public InfoBaby() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public InfoBaby(String name, String sex, int year, int month, int day
                , String headline, String tall, String weight, String parents,String path) {
            this.name = name;
            this.sex = sex;
            this.year = year;
            this.month = month;
            this.day = day;
            this.headline = headline;
            this.tall = tall;
            this.weight = weight;
            this.parents = parents;
            this.path=path;
        }

    }

    // Firebase에 저장
    public void putFireStore() {
        Log.w("입력", "입력시작");
        Intent in = getIntent();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Log.w("입력", "class 에 넣기");
        baby = new InfoBaby(in.getStringExtra("name")
                , in.getStringExtra("sex")
                , in.getExtras().getInt("year")
                , in.getExtras().getInt("month")
                , in.getExtras().getInt("day")
                , in.getStringExtra("headline")
                , in.getStringExtra("height")
                , in.getStringExtra("weight")
                , mLoginId,imgpath);

        Log.w("입력", "db 입력");



        // 사진 db 저장
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference mountainsRef = storageRef.child("Baily/"+mLoginId+"/"+babypic);

        imageview.setDrawingCacheEnabled(true);
        imageview.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageview.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("저장", "실패: ");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("저장", "onSuccess: ");

            }
        });


        putLocalDB();
    }
    // local DB 에 저장
    public void putLocalDB(){
        ContentValues values = new ContentValues();

        values.put("name", baby.name);
        values.put("sex", baby.sex);
        values.put("ybirth", baby.year);
        values.put("mbirth", baby.month);
        values.put("dbirthy", baby.day);
        values.put("headline", baby.headline);
        values.put("tall", baby.tall);
        values.put("weight", baby.weight);
        values.put("parents", mLoginId);
        values.put("imgpath", imgpath);
        // 테이블 이름 + 이제까지 입력한것을 저장한 변수(values)
        Log.d("3page", "insert 시작");
        db.insert("baby", null, values);
        Log.d("3page", "insert 끝");

        String sqlUpdate = "UPDATE thisusing SET baby='"+baby.name+"' WHERE _id=1" ;
        db.execSQL(sqlUpdate) ;

        Log.d("ttttttttttttttttttt", "baby.name =  "+baby.name+"   id = "+mLoginId);
        sqlUpdate = "UPDATE user SET lastbaby='"+baby.name+"' WHERE id='"+mLoginId+"'" ;
        db.execSQL(sqlUpdate) ;


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(mLoginId)
                .update("lastbaby",baby.name)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("regitFire", "파베 입력 성공");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("regitFire", "입력 실패", e);
                    }
                });


        MainScreen();
    }

    private void usingDB(){
        helper = new DBlink(this, dbName, null, dbVersion);
        db = helper.getWritableDatabase();
        String sql = "select * from thisusing where _id=1"; // 검색용
        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            mLoginId=cursor.getString(1);
            Log.d("3page", "db받기 id = " +mLoginId);
        }

    }



}
