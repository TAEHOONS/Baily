package com.example.baily;

import android.app.Activity;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class FragHome extends Fragment {

    String dbName = "user.db";
    int dbVersion = 3;
    private DBlink helper;
    private SQLiteDatabase db;

    private String mId, mBabyname, imgpath;
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
                putMenuData(popup);
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

    public void putMenuData(PopupMenu popup) {
        menu = popup.getMenu();
        int limit=3;

            menu.add(0, 1, 0, tvName.getText().toString());
            menu.add(0, 2, 0, "+ 아기 추가하기");

    }

    public void MenuClick(MenuItem item) {
        Toast.makeText(getActivity(), "메뉴 터치 : " + item.getTitle(), Toast.LENGTH_SHORT).show();
        Log.d("menu1", "baby1 터치");

    }


}