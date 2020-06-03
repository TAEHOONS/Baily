package com.example.baily.log;


import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.baily.DBlink;
import com.example.baily.R;
import com.example.baily.babyPlus.FirstPage;
import com.example.baily.babyPlus.SecondPage;
import com.example.baily.caldate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;

public class RegisterPage extends AppCompatActivity implements View.OnClickListener {
    private String userID;
    private String userPassword;
    private String userPasswordCk;
    private String userEmail;

    String SendRandomCode;

    // FirebaseFirestore fdb = FirebaseFirestore.getInstance();
    Boolean emailFlag = false;
    Boolean idFlag = false;
    Boolean pwFlag = false;
    EditText reg_textEdt;
    EditText reg_repwdEdt;
    EditText reg_pwdEdt;
    EditText reg_nameEdt;
    EditText reg_emailEdt;
    Button reg_emailCkBtn; // 이메일 인증번호확인버튼
    Button reg_confirBtn;// OK버튼

    //회원가입버튼 눌렀을 때 dialog 띄움
    View r_dialog;
    TextView checkTxt;
    //인증번호확인과 중복아이디 체크를 했는지 확인하기 위한
    Boolean numClick=false;
    Boolean sameCk = false;

    //여기에 추가적으로 인증번호
    Button reg_numsendBtn = null;
    EditText emailText = null;


    String randomNum; // 이메일로 보내진 인증번호
    TextView time_counter; //시간을 보여주는 TextView
    EditText emailAuth_number; //사용자가 인증 번호를 입력 하는 칸
    Button emailAuth_btn; // OK버튼
    CountDownTimer countDownTimer;
    final int MILLISINFUTURE = 180 * 1000; //총 시간 (300초 = 5분)
    final int COUNT_DOWN_INTERVAL = 1000; //onTick 메소드를 호출할 간격 (1초)
    int count=0; //count_method에 쓰이는변수
    String dbName = "user.db", mgetId = "";
    String mgetPassword, mgetRePassword, mgetIdCk,mgetNameCk,mgetEmailCk,mgetEmailNumCk;  // 최종확인할때 쓰는버튼
    int dbVersion = 3;
    private DBlink helper;
    private SQLiteDatabase db;
    private NumberPicker reg_idEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        //보내기 버튼을 누르면 이메일로 전송
        reg_numsendBtn = (Button) findViewById(R.id.reg_numsendBtn);//emailEditText
        reg_numsendBtn.setOnClickListener(this);

        //이메일 입력칸
        emailText = (EditText) findViewById(R.id.emailText);
        reg_emailEdt = (EditText) findViewById(R.id.reg_emailnumEdt);

        checkTxt = (TextView)findViewById(R.id.checkTxt);

        reg_nameEdt = (EditText) findViewById(R.id.reg_nameEdt);
        reg_textEdt = (EditText) findViewById(R.id.reg_idEdt);
        reg_pwdEdt = (EditText) findViewById(R.id.reg_pwdEdt);
        reg_repwdEdt = (EditText) findViewById(R.id.reg_repwdEdt);
        reg_emailCkBtn = (Button) findViewById(R.id.reg_emailCheckBtn);
        reg_confirBtn = (Button) findViewById(R.id.reg_confirmBtn);
        usingDB();
        Get_Internet(this);


        //로컬디비에 넣는거
        InsertData(reg_textEdt.toString(), reg_pwdEdt.toString(),reg_nameEdt.toString());
        //다이얼로그

    }

    public void countDownTimer() {
        //카운트 다운 메소드

        //줄어드는 시간을 나타내는 TextView
        time_counter = (TextView) findViewById(R.id.emailAuth_time_counter);
        //OK 버튼
        reg_confirBtn = (Button) findViewById(R.id.reg_confirmBtn);

        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                //(300초에서 1초 마다 계속 줄어듬)

                long emailAuthCount = millisUntilFinished / 1000;
                Log.d("Alex", emailAuthCount + "");

                if ((emailAuthCount - ((emailAuthCount / 60) * 60)) >= 10) { //초가 10보다 크면 그냥 출력
                    time_counter.setText((emailAuthCount / 60) + " : " + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                } else { //초가 10보다 작으면 앞에 '0' 붙여서 같이 출력. ex) 02,03,04...
                    time_counter.setText((emailAuthCount / 60) + " : 0" + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                }

                //emailAuthCount은 종료까지 남은 시간임. 1분 = 60초 되므로,
                // 분을 나타내기 위해서는 종료까지 남은 총 시간에 60을 나눠주면 그 몫이 분이 된다.
                // 분을 제외하고 남은 초를 나타내기 위해서는, (총 남은 시간 - (분*60) = 남은 초) 로 하면 된다.

            }

            @Override
            public void onFinish() {
                cancel();
            }
        }.start();
        reg_confirBtn.setOnClickListener(this);
    }





/*
    //최종 회원가입 버튼 입력 처리
    public void m_regRegClick(View v) {
        putFireStore(reg_textEdt.getText().toString());
        //디비에 들어가는 버튼

    }
*/
    // 아이디 중복체크 버튼 입력처리
    public void m_regIdChkClick(View v) {
        switch (v.getId()) {
            case R.id.reg_idckBtn: {
                mgetId = reg_textEdt.getText().toString();
                checkLogin(mgetId);
            }
        }
    }

    //인터넷 연결 확인
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


    //데이터 넣는법
    private void InsertData(String userId, String userPw,String userName) {
        //선언
        ContentValues values = new ContentValues();
        //values에 테이블의 column에 넣을값 x 넣기
        //고정 변수는 ""로 하고 변수로 할꺼면 그냥 하기
        //컬럼 이름은 DBlink.java 참조
        values.put("id", userId);
        values.put("pw", userPw);
        values.put("name", userName);
        values.put("email", "xorehdtk@naver.com");

        // 테이블 이름 + 이제까지 입력한것을 저장한 변수(values)
        db.insert("user", null, values);

    }


    //아이디 중복체크
    private void checkLogin(String insertId) {
        String sql = "select * from user where id = '" + insertId + "' "; // 검색용
        Cursor c = db.rawQuery(sql, null);
        String sqlId = "", sqlPw = "", editId = "", editPw = "", sqlName = "";


        while (c.moveToNext()) {
            sqlId = c.getString(1);
        }

        if (sqlId.equals(insertId)) {//중복 있을경우
            reg_textEdt.setTextColor(Color.parseColor("RED"));
            sameCk = false;
        } else { //중복 없을경우
            reg_textEdt.setTextColor(Color.parseColor("GREEN"));
            sameCk = true;
        }
    }


    // DB 사용
    private void usingDB() {
        helper = new DBlink(this, dbName, null, dbVersion);
        db = helper.getWritableDatabase();
    }

    //이메일 부분
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reg_emailCheckBtn: //OK 버튼을 눌렀을 시

                //인증번호를 입력하지 않은 경우 > null 값 처리
                String emailAuth_num = reg_emailEdt.getText().toString();
                if (emailAuth_num.getBytes().length <= 0) {
                    Toast.makeText(this, "이메일 인증번호 입력", Toast.LENGTH_SHORT).show();
                    numClick = false;
                } else {
                    String user_answer = reg_emailEdt.getText().toString();
                    if (user_answer.equals(SendRandomCode)) {
                        Toast.makeText(this, "이메일 인증 성공", Toast.LENGTH_SHORT).show();
                        emailFlag = true;
                        numClick = true;
                        countDownTimer.cancel();//성공 시 타이머 중지
                    } else {
                        Toast.makeText(this, "이메일 인증 실패", Toast.LENGTH_SHORT).show();
                        emailFlag = false;
                        numClick = false;
                    }
                }

                break;

            case R.id.reg_numsendBtn:
                SendMail mailServer = new SendMail();
                mailServer.sendSecurityCode(getApplicationContext(), emailText.getText().toString());
                countDownTimer();
                SendRandomCode = mailServer.getRandomNum();
                Log.d("email", "reg_numsendBtn: SendRandomCode=" + SendRandomCode);
                Log.d("email", "email end");
                break;

            case R.id.reg_confirmBtn:
                AlertDialog.Builder dlg = new AlertDialog.Builder(RegisterPage.this);
                mgetPassword = reg_pwdEdt.getText().toString();
                mgetRePassword = reg_repwdEdt.getText().toString();
                mgetIdCk = reg_textEdt.getText().toString();
                mgetNameCk = reg_nameEdt.getText().toString();
                mgetEmailCk = emailText.getText().toString();
                mgetEmailNumCk = reg_emailEdt.getText().toString();

                //모든 칸이 빈칸일 경우
                if (mgetNameCk.equals("")&&mgetIdCk.equals("")&&mgetPassword.equals("")&&mgetRePassword.equals("")&&mgetEmailCk.equals("")&&mgetEmailNumCk.equals("")){
                    dlg.setTitle("정보 재확인 요망");
                    dlg.setMessage("정보를 입력해주세요");
                    dlg.setPositiveButton("확인", null);
                    dlg.show();
                }
                //한개라도 빈칸이거나 정보가 일치하지않을 경우
                else {

                    //dlg.setTitle("정보 재확인 요망");
                    if(mgetNameCk.equals("")){
                        dlg.setTitle("정보 재확인 요망");
                        dlg.setMessage("이름을 입력해주세요");
                        dlg.setPositiveButton("확인", null);
                        dlg.show();
                    }
                    else if(mgetIdCk.equals("")){
                        dlg.setTitle("정보 재확인 요망");
                        dlg.setMessage("아이디를 입력해주세요");
                        dlg.setPositiveButton("확인", null);
                        dlg.show();
                    }
                    else if(mgetPassword.equals("")){
                        dlg.setTitle("정보 재확인 요망");
                        dlg.setMessage("비밀번호를 입력해주세요");
                        dlg.setPositiveButton("확인", null);
                        dlg.show();
                    }
                    else if(mgetRePassword.equals("")){
                        dlg.setTitle("정보 재확인 요망");
                        dlg.setMessage("비밀번호 확인란에 비밀번호를 입력해주세요");
                        dlg.setPositiveButton("확인", null);
                        dlg.show();
                    }
                    else if(!mgetRePassword.equals(mgetPassword)){
                        dlg.setTitle("정보 재확인 요망");
                        dlg.setMessage("비밀번호가 일치하지 않습니다.");
                        dlg.setPositiveButton("확인", null);
                        dlg.show();
                    }
                    else if(mgetEmailCk.equals("")){
                        dlg.setTitle("정보 재확인 요망");
                        dlg.setMessage("이메일을 입력해주세요");
                        dlg.setPositiveButton("확인", null);
                        dlg.show();
                    }
                    else if(mgetEmailNumCk.equals("")){
                        dlg.setTitle("정보 재확인 요망");
                        dlg.setMessage("이메일 인증번호를 입력해주세요");
                        dlg.setPositiveButton("확인", null);
                        dlg.show();
                    }
                    else if(numClick==false){
                        dlg.setTitle("정보 재확인 요망");
                        dlg.setMessage("이메일 인증번호 확인을 완료해주세요");
                        dlg.setPositiveButton("확인", null);
                        dlg.show();
                    }
                    else if(sameCk == false){
                        dlg.setTitle("정보 재확인 요망");
                        dlg.setMessage("중복된 아이디가 있는지 체크해주세요");
                        dlg.setPositiveButton("확인", null);
                        dlg.show();
                    }
                    else{
                        putFireStore(reg_textEdt.getText().toString());
                        dlg.setTitle("회원가입 완료");
                        dlg.setMessage(mgetNameCk+"님 환영합니다.");
                        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(RegisterPage.this, MainActivity.class);
                                finish();
                            }
                        });
                        dlg.show();
                    }

                }
                break;

        }
    }


    // FireBase에 회원가입 정보넣기
    public class member {

        public String name, pw, email;

        public member() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public member(String name, String password, String email) {
            this.name = name;
            this.pw = password;
            this.email = email;
        }

    }

    public void putFireStore(String id) {
        FirebaseFirestore fdb = FirebaseFirestore.getInstance();
        member member = new member(reg_nameEdt.getText().toString(), reg_pwdEdt.getText().toString(), reg_emailEdt.getText().toString());


// Add a new document with a generated ID


// Add a new document with a generated ID
        fdb.collection("member").document(id)
                .set(member)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("입력", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("에러", "Error writing document", e);
                    }
                });
    }
}
