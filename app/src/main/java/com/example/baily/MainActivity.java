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
import com.google.firebase.firestore.DocumentSnapshot;
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
    boolean mNextPage=false;

    EditText mETid,mETpw;
    Button mBloin;
    TextView mTVeid,mTVepw,mTVfid,mTVfpw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseFirestore db = FirebaseFirestore.getInstance();


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
    private void checkLogin (final String insetId) {
        // DB에서 받는값
        final String sqlId = "";
        String sqlPw="";
        String sqlName="";
        // edit에 id, pw 입력값
        final String editId="",editPw="";
        Log.d("검색", "시작");
        final String finalEditPw = mETpw.getText().toString();



        mTVeid.setText("");
        mTVepw.setText("");


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("user").document(insetId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        if(finalEditPw.equals(null)|| finalEditPw.equals(""))
                            mTVepw.setText("비밀번호를 입력해 주시길 바랍니다");
                            // 아이디 OK 비번 False
                        else if(!finalEditPw.equals(document.get("pw")))
                            mTVepw.setText("비밀번호가 틀렸습니다");
                            // 아이디 OK 비번 OK
                        else if(finalEditPw.equals(document.get("pw")))
                            loginAfter(insetId);

                    } else {
                        mTVeid.setText("아이디가 없습니다");
                    }
                } else {
                    Log.d("검색", "get failed with ", task.getException());
                }
            }
        });
//        db.collection("users").document(insetId)
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("검색", document.getId() + " => " + document.get("pw"));
//
//                            }
//                        } else {
//                            Log.d("검색", "Error getting documents: ", task.getException());
//                        }
//                    }
//                });



        // 아이디 False 비번 False

            // 아이디 OK 비번 Null


    }

    // 화면이동 -> 메인페이지 or 퍼스트 페이지
    private void loginAfter(final String userid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mNextPage=false;
        // 컬렉션 일치 검색↓
        db.collection("baby")
                .whereEqualTo("parents", userid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("검색_스크린", "db 돌아감 ");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                mNextPage=true;
                                Log.d("검색_스크린", "데이더 있어서 돌아감");

                            }
                            Log.d("검색_스크린", "아기 for문 끝");
                            MainScreen(userid,mNextPage);

                        } else {
                            Log.d("검색_스크린", "아기가 없어요");
                        }

                    }
                });

    }


    // 화면이동 -> 메인페이지 or 퍼스트 페이지
    private void MainScreen(String id,boolean page){
        Intent intent;
        if(page==true)
        intent = new Intent(this, MainPage.class);
        else
        intent = new Intent(this, FirstPage.class);

        intent.putExtra("login",id);
        startActivity(intent);

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
