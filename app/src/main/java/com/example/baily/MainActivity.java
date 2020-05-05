package com.example.baily;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String dbName = "user.db";
    int dbVersion = 3;
    private DBlink helper;
    private SQLiteDatabase db;

    EditText mETid,mETpw;
    Button mBloin;
    TextView mTVeid,mTVepw,mTVfid,mTVfpw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

// Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("DB", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("DB", "Error adding document", e);
                    }
                });
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("DB", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w("DB", "Error getting documents.", task.getException());
                        }
                    }
                });

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        Get_Internet(this);
        mETid=(EditText)findViewById(R.id.lp_id);
        mETpw=(EditText)findViewById(R.id.lp_pwd);
        mBloin=(Button) findViewById(R.id.lp_logBtn);
        mTVeid=(TextView) findViewById(R.id.lp_Errorid);
        mTVepw=(TextView) findViewById(R.id.lp_Errorpw);
        mTVfid=(TextView)findViewById(R.id.lp_findID);
        mTVfpw=(TextView)findViewById(R.id.lp_findPwd);
        usingDB();

        // 터치 입력 처리 //findID,findPW
        mTVfid.setOnTouchListener(new View.OnTouchListener() {
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
        mTVfpw.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent e){
                switch (e.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        return true;
                    case MotionEvent.ACTION_UP:
                        FindPwdScreen();
                        return true;
                    default:
                        return  false;
                }
            }
        });

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

    public static void Get_Internet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                Toast.makeText(context, "와이파이", Toast.LENGTH_SHORT).show();
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                Toast.makeText(context, "데이터 연결", Toast.LENGTH_SHORT).show();
            }
        }
        Toast.makeText(context, "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show();
    }



    //로그인 경우의 수 체크
    private void checkLogin (String insetId) {
        String sql = "select * from user where id = '"+insetId+"'"; // 검색용
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
            MainScreen(insetId);

    }

    // 화면이동 -> 메인페이지 or 퍼스트 페이지
    private void MainScreen(String userid) {
        String sql = "select * from baby where parents = '"+userid+"'"; // 검생용
        Cursor cursor = db.rawQuery(sql, null);
        String sqlmom="",sqlname;
        while (cursor.moveToNext()) {
            sqlmom=cursor.getString(9);
            Log.d("db", sqlmom);
        }
        if(sqlmom.equals(userid)) {
            Intent intent = new Intent(this, MainPage.class);
            intent.putExtra("login",userid);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(this, FirstPage.class);
            intent.putExtra("login",userid);
            startActivity(intent);
        }
    }

    // 화면이동 -> 메인페이지->회원가입페이지
    private void RegisterScreen() {
        Intent intent = new Intent(MainActivity.this, RegisterPage.class);
        startActivity(intent);
    }

    // 화면이동 -> 메인페이지 -> FIND_ID 페이지
    private void FindIdScreen(){
        Intent intent = new Intent(MainActivity.this,FindIdPage.class);
        startActivity(intent);
    }
    // 화면이동 -> 메인페이지 -> FIND_PWD 페이지
    private void FindPwdScreen(){
        Intent intent = new Intent(MainActivity.this,FindPwPage.class);
        startActivity(intent);
    }


    //DB 생성및 연결
    private void usingDB(){
        helper = new DBlink(this, dbName, null, dbVersion);
        db = helper.getWritableDatabase();
    }
}
