package com.example.baily.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baily.DBlink;
import com.example.baily.PopupSet;
import com.example.baily.babyPlus.BirthdayPicker;
import com.example.baily.babyPlus.FirstPage;
import com.example.baily.babyPlus.HeightAndWeight;
import com.example.baily.babyPlus.SecondPage;
import com.example.baily.babyPlus.ThirdPage;
import com.example.baily.log.MainActivity;
import com.example.baily.R;
import com.example.baily.main.home.FragHome;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class setting extends AppCompatActivity {

    String dbName = "user.db";
    int dbVersion = 3;
    private DBlink helper;
    private SQLiteDatabase db;

    private String mId, mBabyname, mUserName, mEmail;

    RadioGroup mSexRG;
    Button mLogout, mDelete, mBRevise, mBabyDelete;
    ImageView s_close;
    TextView NameTV, EmailTV, IdTV, BirthDayTV;
    EditText BabyNameEdit;
    private CircleImageView imageview;
    //프로필변경관련
    private final int GET_GALLERY_IMAGE = 150;

    private int BYear, BMonth, BDay, NewBYear, NewBMonth, NewBDay,mPopint;
    private String imgpath, BGender, newbaby = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_home_setting);

        NameTV = (TextView) findViewById(R.id.ms_TV_username);
        EmailTV = (TextView) findViewById(R.id.ms_TV_Email);
        IdTV = (TextView) findViewById(R.id.ms_TV_id);
        BirthDayTV = (TextView) findViewById(R.id.ms_BabyBirthTV);
        mLogout = (Button) findViewById(R.id.ms_Btn_LogoutBtn);
        mDelete = (Button) findViewById(R.id.ms_Btn_delete);
        mBRevise = (Button) findViewById(R.id.ms_Btn_BabyRevise);
        mBabyDelete = (Button) findViewById(R.id.ms_Btn_Babydelete);
        mSexRG = (RadioGroup) findViewById(R.id.ms_sexRG);
        s_close = (ImageView) findViewById(R.id.ms_img_closeBtn);
        imageview = (CircleImageView) findViewById(R.id.ms_profileImg);
        BabyNameEdit = (EditText) findViewById(R.id.ms_BabyNameEdt);


        usingDB();
        getDBdata();
        s_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thisLogout();
                finish();
            }
        });
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupCall(1);
            }
        });
        mBRevise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BabyNameCheck(BabyNameEdit.getText().toString());
            }
        });
        mBabyDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupCall(3);
            }
        });
        BirthDayTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BerthPickerScreen();
            }
        });

    }

    // 로그아웃
    private void thisLogout() {
        // 파이어 베이스 저장
        //FireSave();

        // 지금 로그인 지우기
        String deleteThig = "DELETE FROM thisusing ";
        db.execSQL(deleteThig);

        ActivityCompat.finishAffinity(this);

        ContentValues values = new ContentValues();
        values.put("_id", 1);
        db.insert("thisusing", null, values);


        Intent intent = new Intent(setting.this, MainActivity.class);
        startActivity(intent);

    }

    // 계정 삭제
    private void DeleteId(Boolean set) {
        if (set) {
            // 지금 로그인 지우기
            String deleteThig = "DELETE FROM thisusing ";
            db.execSQL(deleteThig);
            // 아이디 데이터들 지우기
            deleteThig = "DELETE FROM baby ";
            db.execSQL(deleteThig);
            // 아이디 삭제
            deleteThig = "DELETE FROM user ";
            db.execSQL(deleteThig);
            deleteThig = "DELETE FROM growlog ";
            db.execSQL(deleteThig);
            deleteThig = "DELETE FROM events ";
            db.execSQL(deleteThig);
            deleteThig = "DELETE FROM recode ";
            db.execSQL(deleteThig);



            ActivityCompat.finishAffinity(this);
            ContentValues values = new ContentValues();
            values.put("_id", 1);
            db.insert("thisusing", null, values);


            Intent intent = new Intent(setting.this, MainActivity.class);
            startActivity(intent);
        }
    }

    // 아기 수정
    private void ReviseBaby(Boolean set) {
        String NewBabyName, sex;
        if (set) {
            // 아기 이름 받기
            NewBabyName = BabyNameEdit.getText().toString();
            NewBabyName = NewBabyName.trim();
            if (NewBabyName.getBytes().length <= 0)
                NewBabyName = mBabyname;

            // 아기 성별 받기
            int id = mSexRG.getCheckedRadioButtonId();
            RadioButton rb = (RadioButton) findViewById(id);
            sex = rb.getText().toString();
            Log.d("Revise", "name: " + NewBabyName);
            Log.d("Revise", "sex: " + sex);

            String Revisejob = "UPDATE baby  SET name='" + NewBabyName + "'," +
                    "sex ='" + sex + "'," +
                    "ybirth ='" + NewBYear + "'," +
                    "mbirth ='" + NewBMonth + "'," +
                    "dbirthy='" + NewBDay + "'" +
                    "where name='" + mBabyname + "'";
            db.execSQL(Revisejob);

            if(BYear!=NewBYear||BMonth!=NewBMonth||BDay!=NewBDay) {
                Log.d("ReviseBaby", " 년 월 일 중 하나라도 다름");
                String delGrow = "DELETE FROM growlog where name='" + mBabyname + "'";
                db.execSQL(delGrow);
            }


            Revisejob = "UPDATE thisusing SET baby='" + NewBabyName + "'WHERE id='" + mId + "'";
            db.execSQL(Revisejob);
            Revisejob = "UPDATE user SET lastbaby='" + NewBabyName + "' WHERE id='" + mId + "'";
            db.execSQL(Revisejob);
            Revisejob = "UPDATE growlog SET name='" + NewBabyName + "' WHERE name='" + mBabyname + "'";
            db.execSQL(Revisejob);
            Revisejob = "UPDATE recode SET name='" + NewBabyName + "' WHERE name='" + mBabyname + "'";
            db.execSQL(Revisejob);
            Revisejob = "UPDATE events SET name='" + NewBabyName + "' WHERE name='" + mBabyname + "'";
            db.execSQL(Revisejob);

        }

    }

    // 아기 삭제
    private void DeleteBaby(Boolean set) {
        if (set) {
            String deletejob;

            // 현재 아기 지우고 새로 셋팅
            // 현재 아기 외 찾기
            String sql = "select * from baby where not name='" + mBabyname + "'"; // 검색용
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                Log.d("DeleteBaby", "while start: " + newbaby);
                newbaby = cursor.getString(1);
                Log.d("DeleteBaby", "while end: " + newbaby);
            }
            // 현재 아기 외의것 thisusing에 넣기
            Log.d("DeleteBaby", "newbaby: " + newbaby);
            if (newbaby != null) {
                deletejob = "UPDATE thisusing  SET baby='" + newbaby + "' WHERE id='" + mId + "'";
                db.execSQL(deletejob);
                deletejob = "UPDATE user SET lastbaby='" + newbaby + "' WHERE id='" + mId + "'";
                db.execSQL(deletejob);

            } else {
                String Revisejob = "UPDATE thisusing SET baby=null WHERE id='" + mId + "'";
                db.execSQL(Revisejob);
                Revisejob = "UPDATE user SET lastbaby=null WHERE id='" + mId + "'";
                db.execSQL(Revisejob);
            }


            // 아기 테이블에서 지우기
            deletejob = "DELETE FROM baby where name='" + mBabyname + "'";
            db.execSQL(deletejob);

            // 현재 아기 데이터 지우기
            deletejob = "DELETE FROM growlog where name='" + mBabyname + "'";
            db.execSQL(deletejob);
            deletejob = "DELETE FROM events where name='" + mBabyname + "'";
            db.execSQL(deletejob);
            deletejob = "DELETE FROM recode where name='" + mBabyname + "'";
            db.execSQL(deletejob);



            if (newbaby == null) {

                Intent intent = new Intent(setting.this, FirstPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        }

    }

    // 수정 삭제 팝업창
    private void PopupCall(int popupText) {
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
        int width = dm.widthPixels; //디바이스 화면 너비
        int height = dm.heightPixels; //디바이스 화면 높이
        mPopint=popupText;

        PopupSet cd = new PopupSet(this);
        WindowManager.LayoutParams wm = cd.getWindow().getAttributes();  //다이얼로그의 높이 너비 설정하기위해
        wm.copyFrom(cd.getWindow().getAttributes());  //여기서 설정한값을 그대로 다이얼로그에 넣겠다는의미
        wm.width = (width / 3) * 2;  //화면 너비의 2/3
        wm.height = (height / 2) * 1;  //화면 높이의2/3

        cd.setDialogListener(new PopupSet.CustomDialogListener() {
            @Override
            public void onPositiveClicked(Boolean exit) {
               switch (mPopint){
                   case 1: DeleteId(exit);
                       break;
                   case 2:
                       ReviseBaby(exit);
                       break;
                   case 3:DeleteBaby(exit);
                       break;
               }

                if (exit)
                    finish();
            }

        }, popupText);

        cd.show();


    }


    // 아기 생일 팝업창
    private void BerthPickerScreen() {


        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
        int width = dm.widthPixels; //디바이스 화면 너비
        int height = dm.heightPixels; //디바이스 화면 높이


        BirthdayPicker cde = new BirthdayPicker(this);
        WindowManager.LayoutParams wm = cde.getWindow().getAttributes();  //다이얼로그의 높이 너비 설정하기위해
        wm.copyFrom(cde.getWindow().getAttributes());  //여기서 설정한값을 그대로 다이얼로그에 넣겠다는의미
        wm.width = (width / 3) * 2;  //화면 너비의 절반
        wm.height = (height / 3) * 2;  //화면 높이의 절반


        cde.setDialogListener(new BirthdayPicker.CustomDialogListener() {
            @Override
            public void onPositiveClicked(int year, int month, int day) {
                BirthDayTV.setText(year + "년 " + month + "월 " + day + "일");
                NewBYear = year;
                NewBMonth = month;
                NewBDay = day;
            }
        });

        cde.show();

    }

    private void getDBdata() {


        String sql = "select * from thisusing where _id=1"; // 검색용
        Cursor cursor = db.rawQuery(sql, null);

        // 기본 데이터
        while (cursor.moveToNext()) {
            mId = cursor.getString(1);
            mBabyname = cursor.getString(2);
            Log.d("Home", "db받기 id = " + mId + "  현재 아기 = " + mBabyname);
        }

        // 현재 사용 유저데이터
        sql = "select * from user where id='" + mId + "'"; // 검색용
        cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            mUserName = cursor.getString(3);
            mEmail = cursor.getString(4);
        }

        // 현재 사용 아기 데이터
        sql = "select * from baby where name='" + mBabyname + "'"; // 검색용
        cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            BGender = cursor.getString(2);
            BYear = cursor.getInt(3);
            BMonth = cursor.getInt(4);
            BDay = cursor.getInt(5);
            imgpath = cursor.getString(10);
        }

        try {
            Bitmap bm = BitmapFactory.decodeFile(imgpath);
            imageview.setImageBitmap(bm);
        } catch (Exception e) {
        }
        if (BGender.equals("남자"))
            mSexRG.check(R.id.ms_RBboy);
        else
            mSexRG.check(R.id.ms_RBgirl);

        NewBYear = BYear;
        NewBMonth = BMonth;
        NewBDay = BDay;

        BirthDayTV.setText(BYear + "년 " + BMonth + "월 " + BDay + "일");
        NameTV.setText(mUserName);
        EmailTV.setText(mEmail);
        IdTV.setText(mId);

        BabyNameEdit.setHint(mBabyname);
//프로필사진눌렀을 때
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });
    }

    // 아기이름 체크
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
            PopupCall(2);
    }

    private void usingDB() {
        helper = new DBlink(this, dbName, null, dbVersion);
        db = helper.getWritableDatabase();
    }


    private void FireSave(){
        FirebaseFirestore firedb = FirebaseFirestore.getInstance();
        firedb.collection(mId + "baby").document().delete();
        firedb.collection(mId + "growth").document().delete();
        firedb.collection(mId + "recode").document().delete();
        firedb.collection(mId + "event").document().delete();

        // baby 검색
        String sql = "select * from baby where parents='" + mId + "' "; // 검색용
        Cursor c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            baby member = new baby(c.getString(1), c.getString(2), c.getInt(3)
                    , c.getInt(4), c.getInt(5), c.getString(6), c.getString(7)
                    , c.getString(8), c.getString(9), c.getString(10));
            firedb.collection(mId + "baby").document(c.getString(0)).set(member);
        }
        c.close();
        // growlog 검색
        sql = "select * from growlog where parents='" + mId + "' "; // 검색용
        c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            growthlog member = new growthlog(c.getString(1), c.getString(2), c.getString(3)
                    , c.getString(4), c.getString(5), c.getString(6), c.getString(7)
                    , c.getString(8));
            firedb.collection(mId + "growlog").document(c.getString(0)).set(member);
        }
        c.close();
        // recode 검색
        sql = "select * from recode where parents='" + mId + "' "; // 검색용
        c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            recode member = new recode(c.getString(1), c.getString(2), c.getString(3)
                    , c.getString(4), c.getString(5), c.getString(6), c.getString(7)
                    , c.getString(8));
            firedb.collection(mId + "recode").document(c.getString(0)).set(member);
        }
        c.close();
        // event 검색
        sql = "select * from events where parents='" + mId + "' "; // 검색용
        c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            diary member = new diary(c.getString(1), c.getString(2), c.getString(3)
                    , c.getString(4), c.getString(5));
            firedb.collection(mId + "events").document(c.getString(0)).set(member);
        }
        c.close();
    }

    public class baby {
        public int year, month, day;
        public String name, sex, headline, tall, weight, parents, imgpath;

        public baby() {
        }

        public baby(String name, String sex, int year, int month, int day,
                    String headline, String tall, String weight, String parents, String imgpath) {
            this.name = name;
            this.sex = sex;
            this.headline = headline;
            this.tall = tall;
            this.weight = weight;
            this.parents = parents;
            this.imgpath = imgpath;
            this.year = year;
            this.month = month;
            this.day = day;
        }

    }

    public class growthlog {

        public String name, weight, tall, headline, fever, date, caldate, parents;;

        public growthlog() {
        }

        public growthlog(String name, String weight, String tall, String headline, String fever
                , String date, String caldate, String parents) {
            this.name = name; this.weight = weight; this.tall = tall;
            this.headline = headline; this.fever = fever; this.date = date;
            this.caldate = caldate; this.parents = parents;
        }

    }

    public class recode {

        public String name, date, time, title, subt, contents1, contents2, parents;

        public recode() {

        }

        public recode(String name, String date, String time, String title, String subt
                , String contents1, String contents2, String parents) {
            this.name = name;this.date = date;this.time = time;
            this.title = title;this.subt = subt;this.contents1 = contents1;
            this.contents2 = contents2;this.parents = parents;
        }

    }

    public class diary {

        public String name,title , date , memo ,parents ;

        public diary() {
        }

        public diary(String name, String title, String date, String memo, String parents) {
            this.name = name; this.title = title;
            this.date = date; this.memo = memo;
            this.parents = parents;
        }

    }


    // 사진작업 터치시 갤러리 사진 선택
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 갤러리 접속 사진 가져오기
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri selectedImageUri = data.getData();
            imageview.setImageURI(selectedImageUri);

        }

        setPhotoNextScreen();
        savePhotoFB();
    }

    // 사진 divice 에 저장
    public void setPhotoNextScreen() {
        Log.d("저장", "저장 시작");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;

        Bitmap bm;
        Log.d("저장", "비트맵 받기");

        bm = ((BitmapDrawable) imageview.getDrawable()).getBitmap();

        try {

            Log.d("저장", "파일 생성 전");

            FileOutputStream fos = openFileOutput(mBabyname + ".jpg", 0);
            //   사진 저장 타입, 사진 퀄리티, 사진 명칭
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

        } catch (Exception e) {
        }


    }

    // FB 에 사진 저장
    public void savePhotoFB() {

        // 사진 db 저장
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference mountainsRef = storageRef.child("Baily/" + mId + "/" + mBabyname + ".jpg");

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
