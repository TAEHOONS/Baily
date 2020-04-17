package com.example.baily;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;

//ID중복체크를 위한 VALIDDATE 클래스
//public class ValidateRequest extends StringRequest{}

public class RegisterPage extends AppCompatActivity {
    private String userID;
    private String userPassword;
    private String userPasswordCk;
    private String userEmail;

    //TextView lb_id;
    EditText reg_textEdt;
    EditText reg_repwdEdt;
    EditText reg_pwdEdt;

    //여기에 추가적으로 인증번호


    String dbName = "user.db",mgetId="";
    int dbVersion = 3;
    private DBlink helper;
    private SQLiteDatabase db;
    private NumberPicker reg_idEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        //lb_id=(TextView)findViewById(R.id.lb_id);
        reg_textEdt=(EditText)findViewById(R.id.reg_idEdt);
        reg_repwdEdt= (EditText)findViewById(R.id.reg_repwdEdt);
        reg_pwdEdt=(EditText)findViewById(R.id.reg_pwdEdt);
        usingDB();


       InsertData("200112","1111");
    }

    //비밀번호 중복 체크
    private void ChkPwd(String userPassword,String userPasswordCk){
        if(reg_repwdEdt.equals(reg_pwdEdt)){

        }else{
            reg_repwdEdt.setTextColor(Color.parseColor("RED"));
        }
    }

    //데이터 넣는법
    private void InsertData(String userId,String userPw){
        //선언
        ContentValues values = new ContentValues();
        //values에 테이블의 column에 넣을값 x 넣기
        //고정 변수는 ""로 하고 변수로 할꺼면 그냥 하기
        //컬럼 이름은 DBlink.java 참조
        values.put("id", userId);
        values.put("pw", userPw);
        // 테이블 이름 + 이제까지 입력한것을 저장한 변수(values)
        db.insert("user", null, values);
    }


    //아이디 중복체크
    private void checkLogin (String insertId) {
        String sql = "select * from user where id = '" + insertId + "' "; // 검색용
        Cursor c = db.rawQuery(sql, null);
        String sqlId = "", sqlPw = "", editId = "", editPw = "", sqlName = "";


        while (c.moveToNext()) {
            sqlId = c.getString(1);
        }

        if (sqlId.equals(insertId)) {//중복 있을경우
            reg_textEdt.setTextColor(Color.parseColor("RED"));
        } else { //중복 없을경우
            reg_textEdt.setTextColor(Color.parseColor("GREEN"));
        }
    }


    // 아이디 중복체크 버튼 입력처리
    public void m_regOnClick(View v){
        switch (v.getId()) {
            case R.id.reg_idckBtn: {
                mgetId=reg_textEdt.getText().toString();
                checkLogin (mgetId);

                }
            }

        }



    private void usingDB(){
        helper = new DBlink(this, dbName, null, dbVersion);
        db = helper.getWritableDatabase();
    }
}
