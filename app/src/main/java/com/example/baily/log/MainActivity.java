package com.example.baily.log;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.baily.DBlink;
import com.example.baily.R;
import com.example.baily.babyPlus.FirstPage;
import com.example.baily.main.BackPressClose;
import com.example.baily.main.MainPage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    String dbName = "user.db";
    int dbVersion = 3,checkFirst=0;
    private DBlink helper;
    private SQLiteDatabase db;

    EditText mETid, mETpw;
    Button mBloin, mBfid, mBfpw;
    TextView mTVeid, mTVepw;

    //파이어베이스 연동
    FirebaseDatabase database;
    DatabaseReference myRef;



    private BackPressClose backPressClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name


        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        backPressClose = new BackPressClose(this);
        mETid = (EditText) findViewById(R.id.lp_id);
        mETpw = (EditText) findViewById(R.id.lp_pwd);
        mBloin = (Button) findViewById(R.id.lp_logBtn);
        mTVeid = (TextView) findViewById(R.id.lp_Errorid);
        mTVepw = (TextView) findViewById(R.id.lp_Errorpw);
        mBfid = (Button) findViewById(R.id.lp_findid);
        mBfpw = (Button) findViewById(R.id.lp_findpw);
        usingDB();


        //파이어베이스연동
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Baliy");

        AutoLogin();

        // 터치 입력 처리 //findID,findPW
        mBfid.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        return true;
                    case MotionEvent.ACTION_UP:
                        FindIdScreen();
                        return true;

                    default:
                        return false;
                }
            }
        });
        mBfpw.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent e) {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        return true;
                    case MotionEvent.ACTION_UP:
                        FindPwdScreen();
                        return true;
                    default:
                        return false;
                }
            }
        });

    }

    // 버튼 입력처리
    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.lp_logBtn: {

                String editId = "";
                editId = mETid.getText().toString();
                mTVeid.setText("");
                mTVepw.setText("");
                if (editId.equals(null) || editId.equals("")){
                    mTVeid.setText("아이디를 입력해 주시길 바랍니다");
                    Toast.makeText(this, "아이디를 입력해 주시길 바랍니다", Toast.LENGTH_SHORT).show();
                }else{
                    loaddb(mETid.getText().toString());
                    checkLogin(editId);
                }

                break;
            }
            case R.id.lp_logJoin: {
                RegisterScreen();
                break;
            }


        }

    }
    //파이어베이스에서 데이터 로드
    public void loaddb(String id) {

        Log.d("들어가기", "DB 들어가기");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d("접속", "DB 접속");


        DocumentReference docRef = db.collection("member").document(id);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("검색1", "DocumentSnapshot data: " + document.getData());
                        Log.d("검색2", "DocumentSnapshot data: " + document.getData());
                        Log.d("검색3", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("검색", "No such document");
                    }
                } else {
                    Log.d("출력", "get failed with ", task.getException());
                }
            }
        });
    }


    //로그인 경우의 수 체크
    private void checkLogin(String insetId) {
        String sql = "select * from user where id = '" + insetId + "'"; // 검색용
        Cursor cursor = db.rawQuery(sql, null);
        String sqlId = "", sqlPw = "", editId = "", editPw = "", sqlName = "";
        while (cursor.moveToNext()) {
            sqlId = cursor.getString(1);
            sqlPw = cursor.getString(2);
            sqlName = cursor.getString(3);
        }
        editId = mETid.getText().toString();
        editPw = mETpw.getText().toString();

        mTVeid.setText("");
        mTVepw.setText("");

        // 아이디 False 비번 False
        if (!editId.equals(sqlId)) {
            mTVeid.setText("아이디가 없습니다.");
            Toast.makeText(this, "아이디가 없습니다.", Toast.LENGTH_SHORT).show();
        }
            // 아이디 OK 비번 Null
        else if (editId.equals(sqlId) && editPw.equals(null) || editPw.equals("")){
            mTVepw.setText("비밀번호를 입력해 주시길 바랍니다.");
            Toast.makeText(this, "비밀번호를 입력해 주시길 바랍니다.", Toast.LENGTH_SHORT).show();
        }

            // 아이디 OK 비번 False
        else if (editId.equals(sqlId) && !editPw.equals(sqlPw)){
            mTVepw.setText("비밀번호가 틀렸습니다.");
            Toast.makeText(this, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
        }

            // 아이디 OK 비번 OK
        else if (editId.equals(sqlId) && editPw.equals(sqlPw))
            DBcopy(sqlId);

    }

    // 로그인시 현재 아이디 기록
    private void DBcopy(String id) {
        Log.d("사용 아이디", "DBcopy");
        ContentValues values = new ContentValues();



        String sql = "select * from thisusing where _id=1"; // 검생용
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            checkFirst = cursor.getInt(0);
            Log.d("사용 아이디", "thisusing: checkFirst ="+checkFirst);
        }


        // 최초 실행시 동작
        if (checkFirst != 1) {
            values.put("_id", 1);
            db.insert("thisusing", null, values);
            Log.d("사용 아이디", "DBcopy: insert 동작");
        } else {
            String sqlUpdate = "UPDATE thisusing SET id='" + id + "'WHERE _id=1";
            db.execSQL(sqlUpdate);
            Log.d("사용 아이디", "DBcopy: update 동작");
        }
        // 테이블 이름 + 이제까지 입력한것을 저장한 변수(values)
        MainScreen(id);
    }


    // 화면이동 -> 메인페이지 or 퍼스트 페이지
    private void MainScreen(String Loginid) {
        Log.d("사용 아이디", "MainScreen");

        String thisbaby = "";
        getUserdata(Loginid);


        String sql = "select * from thisusing where _id=1"; // 검생용
        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            thisbaby = cursor.getString(2);
            Log.d("사용 아이디", "아기 있나 보기" + thisbaby);
        }

        String userId = "UPDATE thisusing SET id='" + Loginid + "' WHERE _id=1";
        db.execSQL(userId);

        if (thisbaby != null) {
            Log.d("사용 아이디", "메인페이지 가기");
            Intent intent = new Intent(this, MainPage.class);
            startActivity(intent);
            finish();
        } else {
            Log.d("사용 아이디", "아기 생산 가기");
            Intent intent = new Intent(this, FirstPage.class);
            startActivity(intent);
        }
    }

    // 화면이동 -> 메인페이지->회원가입페이지
    private void RegisterScreen() {
        Intent intent = new Intent(MainActivity.this, RegisterPage.class);
        startActivity(intent);
    }

    // 화면이동 -> 메인페이지 -> FIND_ID 페이지
    private void FindIdScreen() {
        Intent intent = new Intent(MainActivity.this, FindIdPage.class);
        startActivity(intent);
    }

    // 화면이동 -> 메인페이지 -> FIND_PWD 페이지
    private void FindPwdScreen() {
        Intent intent = new Intent(MainActivity.this, FindPwPage.class);
        startActivity(intent);
    }

    // 자동 로그인
    private void AutoLogin() {
        Log.d("사용 아이디", "AutoLogin");
        String sql = "select * from thisusing where _id=1"; // 검생용
        Cursor cursor = db.rawQuery(sql, null);
        String login = null;

        while (cursor.moveToNext()) {
            login = cursor.getString(1);
            Log.d("사용 아이디", "아이디 확인" + login);
        }
        if (login != null)
            DBcopy(login);
    }

    private void getUserdata(String id) {
        Log.d("사용 아이디", "getUserdata");

        String sql = "select * from user where id = '" + id + "'"; // 검색용
        Cursor cursor = db.rawQuery(sql, null);
        String sqlbaby = "";
        while (cursor.moveToNext()) {
            sqlbaby = cursor.getString(5);
        }
        Log.d("사용 아이디", "user id lastbaby = " + sqlbaby);
        if (sqlbaby != null) {
            String sqlUpdate = "UPDATE thisusing SET baby='" + sqlbaby + "' WHERE _id=1";
            db.execSQL(sqlUpdate);
        }
        Log.d("사용 아이디", "사용자 카피 완료 update 동작");
        Log.d("사용 아이디", "업데이트 id = " + id + "  sqlbaby = " + sqlbaby);


    }

    //DB 생성및 연결
    private void usingDB() {
        helper = new DBlink(this, dbName, null, dbVersion);
        db = helper.getWritableDatabase();
    }

    @Override
    public void onBackPressed() {
        backPressClose.onBackPressed();
    }
}
