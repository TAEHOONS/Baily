package com.example.baily;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragHome extends Fragment {

    String dbName = "user.db";
    int dbVersion = 3;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_home, container, false);
        usingDB(container);
        getDBdata();
        menuBtn = (ImageView) view.findViewById(R.id.h_bSelectBtn);
        imageview = (CircleImageView) view.findViewById(R.id.h_profileImg);
        tvName = (TextView) view.findViewById(R.id.h_bNameTxt);

        tvName.setText(mBabyname);

        try {
            Bitmap bm = BitmapFactory.decodeFile(imgpath);
            imageview.setImageBitmap(bm);
        } catch (Exception e) {
        }

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


        return view;
    }


    public static Fragment newInstance() {
        FragHome fragHome = new FragHome();
        return fragHome;

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
            imgpath = cursor.getString(10);

            Log.d("Home", "db받기 path = " + imgpath);
        }


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

        }
    }


}