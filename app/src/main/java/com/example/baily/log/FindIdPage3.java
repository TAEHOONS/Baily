package com.example.baily.log;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.example.baily.DBlink;
import com.example.baily.R;

public class FindIdPage3 extends AppCompatActivity {

    EditText fip_emailnunEdt;
    Button fip_emailnumBtn;
    String nowName,nowId,getId;
    String dbName = "user.db", mgetId = "";
    String sqlId = "";
    int dbVersion = 3;
    private DBlink helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id_page3);
        fip_emailnunEdt = findViewById(R.id.fip_emailnunEdt);
        fip_emailnumBtn = findViewById(R.id.fip_emailnumBtn);

        usingDB();

    }

    public void mOnClick(View v) {
        Intent intent1 = getIntent();
        String SendRandomCode = intent1.getStringExtra("code");
        nowName = intent1.getStringExtra("nowName");

        Log.d("email", " SendRandomCode=" + SendRandomCode);
        switch (v.getId()) {
            case R.id.fip_emailnumBtn:
                //인증번호를 입력하지 않은 경우 > null 값 처리
                String emailAuth_num = fip_emailnunEdt.getText().toString();
                if (emailAuth_num.getBytes().length <= 0) {
                    Toast.makeText(this, "이메일 인증번호 입력", Toast.LENGTH_SHORT).show();
                } else {
                    String user_answer = fip_emailnunEdt.getText().toString();

                    if (user_answer.equals(SendRandomCode)) {
                        Toast.makeText(this, "이메일 인증 성공", Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder ad = new AlertDialog.Builder(FindIdPage3.this);
                        ad.setIcon(R.mipmap.ic_launcher);
                        ad.setTitle("인증 완료");
                        readId(nowName);
                        ad.setMessage(nowName+"님의 아이디는 "+nowId+"입니다");
                        Log.d("nowName", "email end "+nowName);
                        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent2 = new Intent(FindIdPage3.this, MainActivity.class);
                                startActivity(intent2);
                                //dialog.dismiss();

                            }
                        });
                        ad.show();

                    } else {
                        Toast.makeText(this, "이메일 인증 실패", Toast.LENGTH_SHORT).show();
                    }
                }

        }

    }

    // DB 사용
    private void usingDB() {
        helper = new DBlink(this, dbName, null, dbVersion);
        db = helper.getWritableDatabase();
    }
    //받은 이름의 아이디값 읽어오기
    private void readId(String insertName) {
        String sql = "select * from user where name = '" + insertName + "' "; // 검색용
        Cursor c = db.rawQuery(sql, null);


        while (c.moveToNext()) {
            sqlId = c.getString(1);
            nowId = c.getString(1);
            getterId(nowId);
            Log.d("와일내부1", "checkLogin: "+"아이디 "+nowId);
        }
        Log.d("와일외부1", "readId: "+"아이디 "+nowId);
    }

    private void getterId(String id){
        getId=id;
        Log.d("겟아디", "getId: "+ getId);
    }

}


