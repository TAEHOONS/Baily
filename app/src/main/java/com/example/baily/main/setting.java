package com.example.baily.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.baily.DBlink;
import com.example.baily.log.MainActivity;
import com.example.baily.R;
import com.example.baily.main.home.FragHome;

import de.hdodenhof.circleimageview.CircleImageView;

public class setting extends AppCompatActivity {

    String dbName = "user.db";
    int dbVersion = 3;
    private DBlink helper;
    private SQLiteDatabase db;

    private String mId, mBabyname,mUserName,mEmail;

    RadioGroup mSexRG;
    Button mLogout,mDelete,mBRevise,mBabyDelete;
    ImageView s_close;
    TextView NameTV,EmailTV,IdTV;
    EditText BabyNameEdit;
    private CircleImageView imageview;

    private int BYear,BMonth,BDay;
    private String imgpath,BGender,newbaby=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_home_setting);

        NameTV=(TextView)findViewById(R.id.ms_TV_username);
        EmailTV=(TextView)findViewById(R.id.ms_TV_Email);
        IdTV=(TextView)findViewById(R.id.ms_TV_id);
        mLogout =(Button)findViewById(R.id.ms_Btn_LogoutBtn);
        mDelete = (Button)findViewById(R.id.ms_Btn_delete);
        mBRevise = (Button)findViewById(R.id.ms_Btn_BabyRevise);
        mBabyDelete = (Button)findViewById(R.id.ms_Btn_Babydelete);
        mSexRG = (RadioGroup) findViewById(R.id.ms_sexRG);
        s_close = (ImageView)findViewById(R.id.ms_img_closeBtn);
        imageview = (CircleImageView)findViewById(R.id.ms_profileImg);
        BabyNameEdit=(EditText)findViewById(R.id.ms_BabyNameEdt);


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
                DeleteId();
                finish();
            }
        });
        mBRevise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReviseBaby();
                finish();
            }
        });
        mBabyDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteBaby();
                finish();
            }
        });


    }
    // 로그아웃
    private void thisLogout(){
        Log.d("setting", "delete 끝");
        String deleteThig = "DELETE FROM thisusing ";
        db.execSQL(deleteThig);
        Log.d("setting", "delete 끝");
        ActivityCompat.finishAffinity(this);

        ContentValues values = new ContentValues();
        values.put("_id", 1);
        db.insert("thisusing", null, values);


        Intent intent = new Intent(setting.this, MainActivity.class);
        startActivity(intent);

    }

    private void DeleteId(){
        // 지금 로그인 지우기
        String deleteThig = "DELETE FROM thisusing ";
        db.execSQL(deleteThig);
        // 아이디 데이터들 지우기
        deleteThig = "DELETE FROM baby ";
        db.execSQL(deleteThig);
        // 아이디 삭제
        deleteThig = "DELETE FROM user ";
        db.execSQL(deleteThig);

        ActivityCompat.finishAffinity(this);
        ContentValues values = new ContentValues();
        values.put("_id", 1);
        db.insert("thisusing", null, values);


        Intent intent = new Intent(setting.this, MainActivity.class);
        startActivity(intent);

    }
    // 아기 수정
    private void  ReviseBaby(){
       String NewBabyName,sex;

        // 아기 이름 받기
        NewBabyName=BabyNameEdit.getText().toString();
        NewBabyName = NewBabyName.trim();
        if (NewBabyName.getBytes().length <= 0)
            NewBabyName=mBabyname;

        // 아기 성별 받기
        int id = mSexRG.getCheckedRadioButtonId();
        RadioButton rb = (RadioButton) findViewById(id);
        sex=rb.getText().toString();
        Log.d("Revise", "name: "+NewBabyName);
        Log.d("Revise", "sex: "+sex);

        String Revisejob = "UPDATE baby  SET name='"+NewBabyName+"'," +
                "sex ='"+sex+"'" +
                "where name='"+mBabyname+"'";
        db.execSQL(Revisejob);

        Revisejob = "UPDATE thisusing SET baby='" + NewBabyName + "'WHERE id='" + mId + "'";
        db.execSQL(Revisejob);
        Revisejob = "UPDATE user SET lastbaby='" + NewBabyName + "' WHERE id='" + mId + "'";
        db.execSQL(Revisejob);
        Revisejob = "UPDATE growlog SET name='" + NewBabyName + "' WHERE name='" + mBabyname + "'";
        db.execSQL(Revisejob);



    }
    // 아기 삭제
    private void DeleteBaby(){

        String deletejob;
        // 현재 아기 지우고 새로 셋팅
        // 현재 아기 외 찾기
        String sql = "select * from baby where not name='"+mBabyname+"'"; // 검색용
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            newbaby = cursor.getString(1);
            // 현재 아기 외의것 thisusing에 넣기
            Log.d("DeleteBaby", "newbaby: "+newbaby);
            if (newbaby != null) {
                deletejob = "UPDATE thisusing  SET baby='" + newbaby + "' WHERE id='" + mId + "'";
                db.execSQL(deletejob);
                deletejob = "UPDATE user SET lastbaby='" + newbaby + "' WHERE id='" + mId + "'";
                db.execSQL(deletejob);

            }else {
                String Revisejob = "UPDATE thisusing SET baby='" + null + "'WHERE id='" + mId + "'";
                db.execSQL(Revisejob);
                Revisejob = "UPDATE user SET lastbaby='" + null + "' WHERE id='" + mId + "'";
                db.execSQL(Revisejob);
            }
        }



        // 아기 테이블에서 지우기
        deletejob = "DELETE FROM baby where name='"+mBabyname+"'";
        db.execSQL(deletejob);

        // 현재 아기 growlog 지우기
        deletejob = "DELETE FROM growlog where name='"+mBabyname+"'";
        db.execSQL(deletejob);




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
        if(BGender.equals("남자"))
            mSexRG.check(R.id.ms_RBboy);
        else
            mSexRG.check(R.id.ms_RBgirl);
        NameTV.setText(mUserName);
        EmailTV.setText(mEmail);
        IdTV.setText(mId);;
        BabyNameEdit.setHint(mBabyname);

    }

    private void usingDB(){
        helper = new DBlink(this, dbName, null, dbVersion);
        db = helper.getWritableDatabase();
    }
}
