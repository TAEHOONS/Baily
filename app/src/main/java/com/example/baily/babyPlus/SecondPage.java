package com.example.baily.babyPlus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baily.DBlink;
import com.example.baily.NumberTextWatcher;
import com.example.baily.R;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class SecondPage extends AppCompatActivity {
    private final int GET_GALLERY_IMAGE = 150;

    String dbName = "user.db", mId = "";
    int dbVersion = 3;
    private DBlink helper;
    private SQLiteDatabase db;

    TextView mHWATV, mBirthTV;
    RadioGroup mSexRG;
    EditText mName, mHeadlin;
    String mHeight = "0", mWeight = "0";
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
        imageview = (CircleImageView) findViewById(R.id.sp_profileImg);

        usingDB();
        Locale locale = new Locale("es", "AR"); // For example Argentina
        int numDecs = 2; // Let's use 2 decimals
        TextWatcher tw = new NumberTextWatcher(mHeadlin, locale, numDecs);
        mHeadlin.addTextChangedListener(tw);

        activity = this;
        Date curTime = Calendar.getInstance().getTime();

        SimpleDateFormat yearDate = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat monthDate = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat dayDate = new SimpleDateFormat("dd", Locale.getDefault());

        mbirthY = Integer.valueOf(yearDate.format(curTime));
        mbirthM = Integer.valueOf(monthDate.format(curTime));
        mbirthD = Integer.valueOf(dayDate.format(curTime));


        mBirthTV.setText(mbirthY + "년 " + mbirthM + "월 " + mbirthD + "일");
    }


    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.sp_creatBtn: {
                BabyNameCheck(mName.getText().toString());
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
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
                break;
            }
        }

    }
    private void BabyNameCheck(String getName){
        String sql = "select * from baby where parents='"+mId+"'"; // 검색용
        Cursor c = db.rawQuery(sql, null);
        String DBName;
        Boolean GAO=false;

        // 기본 데이터
        while (c.moveToNext()) {
            DBName = c.getString(1);
            if(getName.equals(DBName))
                GAO=true;
        }

        if(GAO==true)
            Toast.makeText(this, "계정에 같은 이름의 아이가 있습니다", Toast.LENGTH_SHORT).show();
        else
        ThirdScreen();
    }


    // 입력 -> 결과 확인  (데이터 이동)
    private void ThirdScreen() {


        int id = mSexRG.getCheckedRadioButtonId();
        //getCheckedRadioButtonId() 의 리턴값은 선택된 RadioButton 의 id 값.
        RadioButton rb = (RadioButton) findViewById(id);

        Intent intent = new Intent(SecondPage.this, ThirdPage.class);
        intent.putExtra("name", mName.getText().toString());
        intent.putExtra("sex", rb.getText().toString());
        intent.putExtra("year", mbirthY);
        intent.putExtra("month", mbirthM);
        intent.putExtra("day", mbirthD);

        String headlinNull=mHeadlin.getText().toString();
        headlinNull = headlinNull.trim();

        if (headlinNull.getBytes().length <= 0)
            intent.putExtra("headline", "0");
        else
            intent.putExtra("headline", mHeadlin.getText().toString());
        intent.putExtra("height", mHeight);
        intent.putExtra("weight", mWeight);
        setPhotoNextScreen();
        startActivity(intent);
        finish();

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
                mWeight = wei;
                mHeight = hei;
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
                mbirthY = year;
                mbirthM = month;
                mbirthD = day;
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

    public void setPhotoNextScreen() {
        Log.d("저장", "저장 시작");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;

        Bitmap bm;
        Log.d("저장", "비트맵 받기");

        bm = ((BitmapDrawable) imageview.getDrawable()).getBitmap();

        try {

            Log.d("저장", "파일 생성 전");
            FileOutputStream fos = openFileOutput(mName.getText().toString() + ".jpg", 0);
            //   사진 저장 타입, 사진 퀄리티, 사진 명칭
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();


            Toast.makeText(this, "file ok", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "file error", Toast.LENGTH_SHORT).show();
        }


    }

    public static Drawable getResizeFileImage(String file_route, int size, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = size;
        Bitmap src = BitmapFactory.decodeFile(file_route, options);
        Bitmap resized = Bitmap.createScaledBitmap(src, width, height, true);
        return new BitmapDrawable(resized);
    }
    public static Bitmap getResizedBitmap(Resources resources, int id, int size, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = size;
        Bitmap src = BitmapFactory.decodeResource(resources, id, options);
        Bitmap resized = Bitmap.createScaledBitmap(src, width, height, true);
        return resized;
    }

    // DB 사용
    private void usingDB() {
        helper = new DBlink(this, dbName, null, dbVersion);
        db = helper.getWritableDatabase();

        String sql = "select * from thisusing where _id=1"; // 검색용
        Cursor cursor = db.rawQuery(sql, null);

        // 기본 데이터
        while (cursor.moveToNext()) {
            mId = cursor.getString(1);
        }
    }


}
