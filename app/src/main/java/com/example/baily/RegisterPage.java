package com.example.baily;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.EditText;

public class RegisterPage extends AppCompatActivity {
    private String userID;
    private String userPassword;
    private String userPasswordCk;
    private String userEmail;
    //여기에 추가적으로 인증번호 해야합니다요요요우


    String dbName = "user.db";
    int dbVersion = 3;
    private DBlink helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        usingDB();


        InsertData("200112","1111");
    }

    private void InsertData(String userid,String userpw){
        //선언
        ContentValues values = new ContentValues();
        //values에 테이블의 column에 넣을값 x 넣기
        //고정 변수는 ""로 하고 변수로 할꺼면 그냥 하기
        //컬럼 이름은 DBlink.java 참조
        values.put("id", userid);
        values.put("pw", userid);
        // 테이블 이름 + 이제까지 입력한것을 저장한 변수(values)
        db.insert("user", null, values);
    }


    private void usingDB(){
        helper = new DBlink(this, dbName, null, dbVersion);
        db = helper.getWritableDatabase();

        final EditText nameText = (EditText) findViewById(R.id.reg_nameEdt);
        final EditText idText = (EditText) findViewById(R.id.reg_idEdt);
        final EditText pwdText = (EditText)findViewById(R.id.reg_pwdEdt);
        final EditText repwdText = (EditText)findViewById(R.id.reg_repwdEdt);
        final EditText emailText = (EditText)findViewById(R.id.reg_emailnumEdt);

    }
}
