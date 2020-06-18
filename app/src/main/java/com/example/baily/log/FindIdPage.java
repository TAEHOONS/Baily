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
    String nowName,ckName;
    int count=0;

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
                ckName(name);
                checkLogin(name);
                if(name.length()==0) {
                    Toast.makeText(FindIdPage.this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if(count==0){
                    Toast.makeText(FindIdPage.this, name+"님은 회원이 아닙니다.", Toast.LENGTH_SHORT).show();
                }



                break;
            }
        }

    }

    //이름 있나없나 체크
    private void checkLogin(String insertName) {
        String sql = "select * from user where name = '" + insertName + "' "; // 검색용
        Cursor c = db.rawQuery(sql, null);
        String sqlName = "";
        Log.d("중복", "checkLogin:1 " + insertName);
        while (c.moveToNext()) {
            sqlName = c.getString(3);
            Log.d("중복", "checkLogin:2 " + insertName);
            ckName(sqlName);
            if (sqlName.equals(insertName)) {//중복 있을경우
                count++;
                Log.d("중복", "checkLogin:3" + insertName);
                Toast.makeText(FindIdPage.this, insertName+"님의 이메일을 입력하세요.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FindIdPage.this, FindIdPage2.class);
                nowName=insertName;
                intent.putExtra("nowName",nowName);
                startActivity(intent);
            }

        }

    }
    private void ckName(String name){
        ckName=name;
    }

}
