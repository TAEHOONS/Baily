package com.example.baily.log;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.baily.DBlink;
import com.example.baily.R;

public class FindIdPage extends AppCompatActivity {
    String dbName = "user.db", mgetId = "";
    int dbVersion = 3;
    private DBlink helper;
    private SQLiteDatabase db;
    EditText fip_nameEdt;
    String tmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);
        fip_nameEdt = (EditText) findViewById(R.id.fip_nameEdt);
        usingDB();

    }

    // DB 사용
    private void usingDB() {
        helper = new DBlink(this, dbName, null, dbVersion);
        db = helper.getWritableDatabase();
    }

    //아이디 확인 버튼 처리
    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.fip_nameBtn: {

                String name = fip_nameEdt.getText().toString();
                checkLogin(name);
                break;
            }
        }

    }

    //이름 있나없나 체크
    private void checkLogin(String insertName) {
        String sql = "select * from user where name = '" + insertName + "' "; // 검색용
        Cursor c = db.rawQuery(sql, null);
        String sqlName = "";

        while (c.moveToNext()) {
            sqlName = c.getString(3);

            if(!sqlName.equals(insertName)){
                Toast.makeText(FindIdPage.this, "없는 이름 입니다.", Toast.LENGTH_SHORT).show();
            }
            else if (sqlName.equals(insertName)) {//중복 있을경우
                Log.d("중복", "checkLogin: " + insertName);
                Toast.makeText(FindIdPage.this, insertName+"님의 이메일을 입력하세요.", Toast.LENGTH_SHORT).show();
                FindIdScreen();
            }

        }

    }


    // 화면이동 -> FIND ID_확인BTN->이메일 확인페이지
    private void FindIdScreen() {
        Intent intent = new Intent(FindIdPage.this, FindIdPage2.class);
        startActivity(intent);
    }
}
