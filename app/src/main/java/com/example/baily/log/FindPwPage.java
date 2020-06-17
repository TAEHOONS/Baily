package com.example.baily.log;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baily.DBlink;
import com.example.baily.R;

public class FindPwPage extends AppCompatActivity {

    Button fpp_idBtn;
    String dbName = "user.db", mgetId = "";
    int dbVersion = 3;
    private DBlink helper;
    private SQLiteDatabase db;
    EditText fpp_idEdt;
    String tmp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw_page);

        fpp_idBtn=(Button)findViewById(R.id.fpp_idBtn);
        fpp_idEdt=(EditText)findViewById(R.id.fpp_idEdt);
    }

    //아이디 입력 후 확인버튼 누를 시
    public void mOnClick(View v){
        switch (v.getId()){
            case R.id.fpp_idBtn:
                usingDB();
                //FindEmailScreen();


        }

    }
    // DB 사용
    private void usingDB() {
        helper = new DBlink(this, dbName, null, dbVersion);
        db = helper.getWritableDatabase();
    }
    //아이디 있나없나 체크
    private void checkLogin(String insertId) {
        String sql = "select * from user where id = '" + insertId + "' "; // 검색용
        Cursor c = db.rawQuery(sql, null);
        String sqlId = "";

        while (c.moveToNext()) {
            sqlId = c.getString(1);

            if(!sqlId.equals(insertId)){
                Toast.makeText(this, "없는 아이디 입니다.", Toast.LENGTH_SHORT).show();
            }
            else if (sqlId.equals(insertId)) {//중복 있을경우
                Log.d("중복", "checkLogin: " + insertId);
                Toast.makeText(this, insertId+"님의 이메일을 입력하세요.", Toast.LENGTH_SHORT).show();
                FindEmailScreen();
            }

        }

    }

    // 화면이동 -> FINDPWpage 아이디_확인BTN->이메일 확인페이지
    private void FindEmailScreen() {
        Intent intent = new Intent(FindPwPage.this, FindPwPage2.class);
        startActivity(intent);
    }
}
