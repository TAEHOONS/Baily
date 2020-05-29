package com.example.baily.main.home;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baily.DBlink;
import com.example.baily.R;
import com.example.baily.babyPlus.FirstPage;
import com.example.baily.caldate;
import com.example.baily.main.MainPage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class FragHome extends Fragment {

    String dbName = "user.db";
    int dbVersion = 3,BYear,BMonth,BDay;;


    private DBlink helper;
    private SQLiteDatabase db;
    // mId= 현재 사용 id, baby, 사진경로
    private String mId, mBabyname, imgpath;
    private int addItem=0;
    private Activity activity;
    private View view;
    Menu menu;
    private CircleImageView imageview;
    private TextView tvName;
    ImageView writeBtn, menuBtn, profileImg;
    //기록한 몸무게, 키, 머리둘레 표시 텍스트
    TextView kgInfor, cmInfor, girthInfo;
    //기록한 날짜와 D-day표시 텍스트
    TextView recodeDate, recodeDday,homeDday;
    String recodeDateNow,kg,cm,head,nowDday;
    //기록할때 몸무게, 키, 머리둘레 입력하는 에디트텍스트
    EditText kgAdd,cmAdd,girthAdd;
    //기록버튼 눌렀을 때 dialog로 기록하는 거 띄움
    View dialogView;
    //프로필변경관련
    private final int GET_GALLERY_IMAGE = 150;

    Date now = new Date();
    SimpleDateFormat sFormat;
    private int position=0;

    public static Fragment newInstance() {
        FragHome fragHome = new FragHome();
        return fragHome;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_home, container, false);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.h_rView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);
        usingDB(container);

        kgInfor = (TextView)view.findViewById(R.id.h_kgInfoTxt);
        cmInfor = (TextView)view.findViewById(R.id.h_cmInfoTxt);
        girthInfo = (TextView)view.findViewById(R.id.h_girthInfoTxt);
        writeBtn = (ImageView)view.findViewById(R.id.h_writeBtn);
        menuBtn = (ImageView) view.findViewById(R.id.h_bSelectBtn);
        imageview = (CircleImageView) view.findViewById(R.id.h_profileImg);
        tvName = (TextView) view.findViewById(R.id.h_bNameTxt);
        homeDday = (TextView) view.findViewById(R.id.h_birthDdayTxt);

        getDBdata();
        tvName.setText(mBabyname);
        try {
            Bitmap bm = BitmapFactory.decodeFile(imgpath);
            imageview.setImageBitmap(bm);
        } catch (Exception e) {
        }

        //아기 변경 및 추가 버튼 눌렀을 때
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("menu1", "onClick: ");
                PopupMenu popup = new PopupMenu(getActivity(), v);
                MakeMenuData(popup);
                // This activity implements OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        MenuClick(item);
                        return true;
                    }

                });
                popup.inflate(R.menu.menu);
                popup.show();
            }
        });

        //기록버튼 눌렀을 때
        writeBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                dialogView = (View) View.inflate(activity, R.layout.write_dialog1,null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(activity);

                dlg.setTitle("새 기록 작성");
                dlg.setView(dialogView);
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
                        recodeDateNow =sFormat.format(now);//오늘 날짜
                        kgAdd = (EditText)dialogView.findViewById(R.id.h_kgAddEdt);
                        cmAdd = (EditText)dialogView.findViewById(R.id.h_cmAddEdt);
                        girthAdd = (EditText)dialogView.findViewById(R.id.h_girthAddEdt);
                        recodeDate = (TextView)view.findViewById(R.id.h_recodeDateTxt);
                        recodeDday = (TextView)view.findViewById(R.id.h_recodeDdayTxt);

                        kg = kgAdd.getText().toString()+"kg";
                        cm =cmAdd.getText().toString()+"cm";
                        head =girthAdd.getText().toString()+"cm";




                        caldate caldate=new caldate(BYear,BMonth,BDay);
                        String date="D + "+caldate.result;

                        CardItem itemdata=new CardItem(kg, cm, head, recodeDateNow, date);
                        ArrayList<CardItem> growList= new ArrayList<>();
                        growList.add(itemdata);
                        MyRecyclerAdapter adapter = new MyRecyclerAdapter(growList);

                        recyclerView.setAdapter(adapter);

                    }
                });
                dlg.setNegativeButton("취소",null);
                dlg.show();
            }

        });

        //프로필사진눌렀을 때
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });
        return view;
    }

    // 사진작업 터치시 갤러리 사진 선택
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 갤러리 접속 사진 가져오기
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri selectedImageUri = data.getData();
            imageview.setImageURI(selectedImageUri);

        }

        setPhotoNextScreen();
        savePhotoFB();
    }



    // 사진 divice 에 저장
    public void setPhotoNextScreen() {
        Log.d("저장", "저장 시작");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;

        Bitmap bm;
        Log.d("저장", "비트맵 받기");

        bm = ((BitmapDrawable) imageview.getDrawable()).getBitmap();

        try {

            Log.d("저장", "파일 생성 전");

            FileOutputStream fos = getActivity().openFileOutput(mBabyname + ".jpg", 0);
            //   사진 저장 타입, 사진 퀄리티, 사진 명칭
            bm.compress(Bitmap.CompressFormat.JPEG, 30, fos);
            fos.flush();
            fos.close();

        } catch (Exception e) { }
    }



    // FB 에 사진 저장
    public void savePhotoFB(){

        // 사진 db 저장
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference mountainsRef = storageRef.child("Baily/"+mId+"/"+mBabyname+ ".jpg");

        imageview.setDrawingCacheEnabled(true);
        imageview.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageview.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("저장", "실패: ");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("저장", "onSuccess: ");

            }
        });
    }






    // DB 연결
    private void usingDB(ViewGroup container) {
        helper = new DBlink(container.getContext(), dbName, null, dbVersion);
        db = helper.getWritableDatabase();
    }

    // 현재값 받기
    private void getDBdata() {
        String sql = "select * from thisusing where _id=1"; // 검색용
        Cursor cursor = db.rawQuery(sql, null);

        // 기본 데이터
        while (cursor.moveToNext()) {
            mId = cursor.getString(1);
            mBabyname = cursor.getString(2);
            Log.d("Home", "db받기 id = " + mId + "  현재 아기 = " + mBabyname);
        }
        // 현재 사용 아기데이터
        sql = "select * from baby where name='" + mBabyname + "'"; // 검색용
        cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            BYear = cursor.getInt(3);
            BMonth = cursor.getInt(4);
            BDay = cursor.getInt(5);
            imgpath = cursor.getString(10);
        }
        caldate caldate=new caldate(BYear,BMonth,BDay);
        int calint=Integer.valueOf(caldate.result);
        homeDday.setText("D + "+caldate.result+", "+(calint/30)+" 개월 "+(calint%30)+"일");

    }

    // 팝업 메뉴 생성 함수
    public void MakeMenuData(PopupMenu popup) {
        menu = popup.getMenu();
        String sql = "select * from baby where parents='"+mId+"'"; // 검색용
        Cursor cursor = db.rawQuery(sql, null);

        int maxbaby=0;
        // 기본 데이터
        while (cursor.moveToNext()) {
            menu.add(0, maxbaby, 0, cursor.getString(1));
            maxbaby++;
            Log.d("Home", "maxbaby = "+maxbaby+"   아기이름 = "+cursor.getString(1));
        }
        addItem=maxbaby;
        Log.d("Home", "maxbaby = "+maxbaby+"   addItem = "+addItem);
        if(addItem<=2)
            menu.add(0, maxbaby, 0, "+ 아기 추가하기");
        Log.d("Fraghome", "MakeMenuData: end");
    }

    // 메뉴 터치 이벤트
    public void MenuClick(MenuItem item) {
        Toast.makeText(getActivity(), "메뉴 터치 : " + item.getTitle(), Toast.LENGTH_SHORT).show();
        Log.d("Home", "item.getItemId() = "+item.getItemId()+"  additem = "+addItem );
        if (item.getItemId() == addItem) {
            // + 버튼시
            Intent intent = new Intent(getContext(), FirstPage.class);
            startActivity(intent);
        }
        else if(item.getTitle().toString().equals(mBabyname)) {
            // 자기 터치
            Log.d("Home", "지금 데이터와 같음");
            Log.d("Home", "item.getTitle() = "+item.getTitle());
            Log.d("Home", "mBabyname = "+mBabyname);
        }
        else{
            Log.d("Home", "아기 변경");
            // 지금 thisusing에 baby를 다른 baby 로 변경
            String userId = "UPDATE thisusing SET baby='"+item.getTitle().toString()+"' WHERE _id=1";
            db.execSQL(userId) ;

            // 새로 고침
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();

            ((MainPage)getActivity()).getDay();


        }

    }


}