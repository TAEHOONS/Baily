package com.example.baily;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.appcompat.app.AlertDialog;
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
    Button reg_confirBtn;

    //여기에 추가적으로 인증번호


    String dbName = "user.db",mgetId="";
    String mgetPassword, mgetRePassword , mgetIdCk;  // 최종확인할때 쓰는버튼
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
        reg_confirBtn=(Button)findViewById(R.id.reg_confirmBtn);
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
    //최종 회원가입 버튼 입력 처리
    public void m_regRegClick(View v){
        switch (v.getId()){
            case R.id.reg_confirmBtn:{
                mgetPassword=reg_pwdEdt.getText().toString();
                mgetRePassword=reg_repwdEdt.getText().toString();
                mgetIdCk=reg_textEdt.getText().toString();




                //아이디 check --아이디가 빈칸일경우
                if(mgetIdCk.equals("")){
                    AlertDialog.Builder ad = new AlertDialog.Builder(RegisterPage.this);
                    ad.setIcon(R.mipmap.ic_launcher);
                    ad.setTitle("ID 재확인 요망");
                    ad.setMessage("아이디는 빈 칸일 수 없습니다.");

                    ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    ad.show();
                }


                //비밀번호 check
                if(mgetPassword.equals(mgetRePassword)) {
                    break;
                }
                else {
                    AlertDialog.Builder ad = new AlertDialog.Builder(RegisterPage.this);
                    ad.setIcon(R.mipmap.ic_launcher);
                    ad.setTitle("비밀번호 재확인 요망");
                    ad.setMessage("비밀번호가 틀립니다");

                    ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    ad.show();
                }





            }
        }
    }

    // 아이디 중복체크 버튼 입력처리
    public void m_regIdChkClick(View v){
        switch (v.getId()) {
            case R.id.reg_idckBtn: {
                mgetId=reg_textEdt.getText().toString();
                checkLogin (mgetId);
            }
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


    // DB 사용
    private void usingDB(){
        helper = new DBlink(this, dbName, null, dbVersion);
        db = helper.getWritableDatabase();
    }
}
