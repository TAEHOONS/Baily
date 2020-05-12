package com.example.baily;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class SecondPage extends AppCompatActivity {
    private final int GET_GALLERY_IMAGE = 150;

    TextView mHWATV, mBirthTV;
    RadioGroup mSexRG;
    EditText mName, mHeadlin;
    String mHeight, mWeight;
    int mbirthY, mbirthM, mbirthD;

    private String mLoginId;
    public static Activity activity;
    CircleImageView imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_page);
        mName = (EditText) findViewById(R.id.sp_nameET);
        mHeadlin = (EditText) findViewById(R.id.sp_headlineET);
        mHWATV = (TextView) findViewById(R.id.sp_tallTV);
        mBirthTV = (TextView) findViewById(R.id.sp_berthTV);
        mSexRG = (RadioGroup) findViewById(R.id.sp_sexRG);
        imageview=(CircleImageView) findViewById(R.id.sp_profileImg);

        activity = this;

    }


    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.sp_creatBtn: {
                ThirdScreen();
                break;
            }
            case R.id.sp_tallTV: {
                HAWPickerScreen();
                break;
            }
            case R.id.sp_berthTV: {
                BerthPickerScreen();
                break;
            }
            //이미지 터치시 갤러리 가기
            case R.id.sp_profileImg: {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent. setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
                break;
            }
        }

    }

    // 입력 -> 결과 확인  (데이터 이동)
    private void ThirdScreen() {


        int id = mSexRG.getCheckedRadioButtonId();
        //getCheckedRadioButtonId() 의 리턴값은 선택된 RadioButton 의 id 값.
        RadioButton rb = (RadioButton) findViewById(id);

        Intent intent = new Intent(SecondPage.this, ThirdPage.class);
        intent.putExtra("name", mName.getText().toString());
        intent.putExtra("sex", rb.getText().toString());
        intent.putExtra("year",mbirthY );intent.putExtra("month",mbirthM );intent.putExtra("day",mbirthD);
        intent.putExtra("headline",mHeadlin.getText().toString() );
        intent.putExtra("height", mHeight);
        intent.putExtra("weight", mWeight);
        setPhotoNextScreen();
        startActivity(intent);

    }

    // 몸무게 키 팜업창
    private void HAWPickerScreen() {

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
        int width = dm.widthPixels; //디바이스 화면 너비
        int height = dm.heightPixels; //디바이스 화면 높이


        HeightAndWeight cd = new HeightAndWeight(this);
        WindowManager.LayoutParams wm = cd.getWindow().getAttributes();  //다이얼로그의 높이 너비 설정하기위해
        wm.copyFrom(cd.getWindow().getAttributes());  //여기서 설정한값을 그대로 다이얼로그에 넣겠다는의미
        wm.width = (width / 3) * 2;  //화면 너비의 절반
        wm.height = (height / 3) * 2;  //화면 높이의 절반

        cd.setDialogListener(new HeightAndWeight.CustomDialogListener() {
            @Override
            public void onPositiveClicked(String hei, String wei) {
                mHWATV.setText(hei + "cm  " + wei + "Kg");
                mWeight=wei;
                mHeight=hei;
            }
        });

        cd.show();

    }

    // 생일 팜업창
    private void BerthPickerScreen() {

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
        int width = dm.widthPixels; //디바이스 화면 너비
        int height = dm.heightPixels; //디바이스 화면 높이


        BirthdayPicker cd = new BirthdayPicker(this);
        WindowManager.LayoutParams wm = cd.getWindow().getAttributes();  //다이얼로그의 높이 너비 설정하기위해
        wm.copyFrom(cd.getWindow().getAttributes());  //여기서 설정한값을 그대로 다이얼로그에 넣겠다는의미
        wm.width = (width / 3) * 2;  //화면 너비의 절반
        wm.height = (height / 3) * 2;  //화면 높이의 절반


        cd.setDialogListener(new BirthdayPicker.CustomDialogListener() {
            @Override
            public void onPositiveClicked(int year, int month, int day) {
                mBirthTV.setText(year + "년 " + month + "월 " + day + "일");
                mbirthY=year; mbirthM=month; mbirthD=day;
            }
        });

        cd.show();

    }

    // 사진작업
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri selectedImageUri = data.getData();
            imageview.setImageURI(selectedImageUri);

        }

    }

    public void setPhotoNextScreen(){
        Log.d("저장", "저장 시작");
        Bitmap bm = ((BitmapDrawable)imageview.getDrawable()).getBitmap();
        Log.d("저장", "비트맵 받기");


        try{
            Log.d("저장", "파일 생성 전");
            File file = new File("test.jpg");
            FileOutputStream fos = openFileOutput(mName.getText().toString()+".jpg" , 0);
            bm.compress(Bitmap.CompressFormat.PNG, 100 , fos);
            fos.flush();
            fos.close();

            Toast.makeText(this, "file ok", Toast.LENGTH_SHORT).show();
        }catch(Exception e) { Toast.makeText(this, "file error", Toast.LENGTH_SHORT).show();}


    }

}
