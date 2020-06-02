package com.example.baily.log;


import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

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


    //여기에 추가적으로 인증번호
    Button reg_numsendBtn = null;
    EditText emailText = null;
    //다이얼로그
    AlertDialog.Builder ad;

    String randomNum; // 이메일로 보내진 인증번호
    TextView time_counter; //시간을 보여주는 TextView
    EditText emailAuth_number; //사용자가 인증 번호를 입력 하는 칸
    Button emailAuth_btn; // OK버튼
    CountDownTimer countDownTimer;
    final int MILLISINFUTURE = 180 * 1000; //총 시간 (300초 = 5분)
    final int COUNT_DOWN_INTERVAL = 1000; //onTick 메소드를 호출할 간격 (1초)
    int count=0; //count_method에 쓰이는변수
    String dbName = "user.db", mgetId = "";
    String mgetPassword, mgetRePassword, mgetIdCk;  // 최종확인할때 쓰는버튼
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
        ad = new AlertDialog.Builder(RegisterPage.this);
        ad.setIcon(R.mipmap.ic_launcher);
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


    //비밀번호 중복 체크
    private void ChkPwd(String userPassword, String userPasswordCk) {
        if (reg_repwdEdt.equals(reg_pwdEdt)) {

        } else {
            reg_repwdEdt.setTextColor(Color.parseColor("RED"));
        }
    }


    private boolean IdCk() {
        //아이디 check --아이디가 빈칸일경우
        if (mgetIdCk.equals("")) {

            ad.setTitle("ID 재확인 요망");
            ad.setMessage("아이디는 빈 칸일 수 없습니다.");
            ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            ad.show();
            return false;
        } else {
            return true;
        }
    }

    private boolean PwCk() {
        if (mgetPassword.equals(mgetRePassword)) {
            return true;
        } else {

            ad.setTitle("비민번호 재확인 요망");
            ad.setMessage("비밀번호를 재확인 해주세요.");
            ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            ad.show();
            return false;
        }

    }

    private boolean EmailCk(boolean emailFlag) {
        //이메일 인증번호 check
        if (emailFlag ==false) {

            ad.setTitle("이메일 재확인 요망");
            ad.setMessage("이메일 인증번호가 틀립니다");

            ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            ad.show();
            return false;
        } else {

        }
        return true;
    }


    //최종 회원가입 버튼 입력 처리
    public void m_regRegClick(View v) {

        switch (v.getId()) {
            case R.id.reg_confirmBtn: {
                mgetPassword = reg_pwdEdt.getText().toString();
                mgetRePassword = reg_repwdEdt.getText().toString();
                mgetIdCk = reg_textEdt.getText().toString();

                if (IdCk()) {
                    Toast.makeText(this, "id 성공", Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(this, "id 실패", Toast.LENGTH_SHORT).show();

                }
                if (PwCk()) {
                    Toast.makeText(this, "pw 성공", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "pw ㅅㄹ패", Toast.LENGTH_SHORT).show();

                }
                if (EmailCk(emailFlag)) {
                    Toast.makeText(this, "모두성공", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "em 실페", Toast.LENGTH_SHORT).show();

                }

            }

        }
        //디비에 들어가는 버튼
        reg_confirBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putFireStore(reg_textEdt.getText().toString());

            }
        });
    }

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
        } else { //중복 없을경우
            reg_textEdt.setTextColor(Color.parseColor("GREEN"));
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
                } else {
                    String user_answer = reg_emailEdt.getText().toString();
                    if (user_answer.equals(SendRandomCode)) {
                        Toast.makeText(this, "이메일 인증 성공", Toast.LENGTH_SHORT).show();
                        emailFlag = true;
                        countDownTimer.cancel();//성공 시 타이머 중지
                    } else {
                        Toast.makeText(this, "이메일 인증 실패", Toast.LENGTH_SHORT).show();
                        emailFlag = false;
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
