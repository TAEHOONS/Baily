package com.example.baily;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    String dbName = "user.db";
    int dbVersion = 3;
    private DBlink helper;
    private SQLiteDatabase db;

    EditText mETid,mETpw;
    Button mBloin;
    TextView mTVeid,mTVepw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mETid=(EditText)findViewById(R.id.lp_id);
        mETpw=(EditText)findViewById(R.id.lp_pwd);
        mBloin=(Button) findViewById(R.id.lp_logBtn);
        mTVeid=(TextView) findViewById(R.id.lp_Errorid);
        mTVepw=(TextView) findViewById(R.id.lp_Errorpw);
        //스폰지밥 넌 해고야
        usingDB();

    }

    // 버튼 입력처리
    public void mOnClick (View v){
        switch (v.getId()) {
            case R.id.lp_logBtn: {

                String editId="";
                editId=mETid.getText().toString();
                mTVeid.setText("");mTVepw.setText("");
                if(editId.equals(null)||editId.equals(""))
                    mTVeid.setText("아이디를 입력해 주시길 바랍니다");
                else
                    checkLogin (editId);
                break;
            }
            case R.id.lp_logJoin: {
                RegisterScreen();
                break;
            }
            
        }

    }


    //로그인 경우의 수 체크
    private void checkLogin (String insetId) {
        String sql = "select * from user where id = '"+insetId+"'"; // 검생용
        Cursor cursor = db.rawQuery(sql, null);
        String sqlId="",sqlPw="",editId="",editPw="",sqlName="";
        while (cursor.moveToNext()) {
            sqlId=cursor.getString(1);
            sqlPw=cursor.getString(2);
            sqlName=cursor.getString(3);
        }
        editId=mETid.getText().toString();
        editPw=mETpw.getText().toString();

        mTVeid.setText("");
        mTVepw.setText("");

        // 아이디 False 비번 False
        if(!editId.equals(sqlId))
            mTVeid.setText("아이디가 없습니다");
            // 아이디 OK 비번 Null
        else if(editId.equals(sqlId)&&editPw.equals(null)||editPw.equals(""))
            mTVepw.setText("비밀번호를 입력해 주시길 바랍니다");
            // 아이디 OK 비번 False
        else if(editId.equals(sqlId)&&!editPw.equals(sqlPw))
            mTVepw.setText("비밀번호가 틀렸습니다");
            // 아이디 OK 비번 OK
        else if(editId.equals(sqlId)&&editPw.equals(sqlPw))
            NextScreen();

    }

    // 화면이동 -> 메인페이지
    private void NextScreen() {
        Intent intent = new Intent(this, MainPage.class);
        startActivity(intent);
        finish();
    }

    // 화면이동 -> 메인페이지->회원가입페이지
    private void RegisterScreen() {
        Intent intent = new Intent(MainActivity.this, RegisterPage.class);
        startActivity(intent);
        finish();
    }

    //DB 생성및 연결
    private void usingDB(){
        helper = new DBlink(this, dbName, null, dbVersion);
        db = helper.getWritableDatabase();
    }
}
