package com.example.baily;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ThirdPage extends AppCompatActivity {


    TextView tvName, tvSex, tvBrith, tvHeadline, tvHAW;

    public static Activity activity;
    private CircleImageView imageview;
    String imgpath = "data/data/com.example.baily/files/";



    private String mLoginId,babypic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_page);
        Intent intent = getIntent();
        mLoginId = intent.getStringExtra("login");

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
        tvHAW.setText(intent.getStringExtra("height") + "cm  " + intent.getStringExtra("weight") + "Kg");


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
        public String name, sex, headline, tall, weight, parents;
        public int year, month, day;

        public InfoBaby() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public InfoBaby(String name, String sex, int year, int month, int day
                , String headline, String tall, String weight, String parents) {
            this.name = name;
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

    public void putFireStore() {
        Log.w("입력", "입력시작");
        Intent in = getIntent();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Log.w("입력", "class 에 넣기");
        InfoBaby baby = new InfoBaby(in.getStringExtra("name")
                , in.getStringExtra("sex")
                , in.getExtras().getInt("year")
                , in.getExtras().getInt("month")
                , in.getExtras().getInt("day")
                , in.getStringExtra("headline")
                , in.getStringExtra("height")
                , in.getStringExtra("weight")
                , mLoginId);

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

    }

}
