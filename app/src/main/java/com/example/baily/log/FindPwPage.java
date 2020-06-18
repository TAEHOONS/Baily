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
    String tmp,ckId;
    int count=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw_page);

        fpp_idBtn=(Button)findViewById(R.id.fpp_idBtn);
        fpp_idEdt=(EditText)findViewById(R.id.fpp_idEdt);

        usingDB();
    }

    //아이디 입력 후 확인버튼 누를 시
    public void mOnClick(View v){
        switch (v.getId()){
            case R.id.fpp_idBtn:
                tmp=fpp_idEdt.getText().toString();
                ckId(tmp);
                checkLogin(tmp);
                if(tmp.length()==0) {
                    Toast.makeText(FindPwPage.this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if(count==0){
                    Toast.makeText(FindPwPage.this, tmp+"님은 회원이 아닙니다.", Toast.LENGTH_SHORT).show();
                }

                break;



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
        Log.d("insertid", "checkLogin: insertId :"+insertId);
        String sqlId = "";

        while (c.moveToNext()) {
            sqlId = c.getString(1);

            if (sqlId.equals(insertId)) {//중복 있을경우
                count++;
                Log.d("중복", "checkLogin: " + insertId);
                Toast.makeText(this, insertId+"님의 이메일을 입력하세요.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FindPwPage.this,FindPwPage2.class);
                intent.putExtra("nowId",tmp);
                startActivity(intent);
                //FindEmailScreen();
            }
            else if(count==0){
                Toast.makeText(this, insertId+"는 없는 아이디 입니다.", Toast.LENGTH_SHORT).show();
            }

        }

    }


    private void ckId(String id){
        ckId=id;
    }
}
