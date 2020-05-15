package com.example.baily;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
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

public class FragHome extends Fragment implements PopupMenu.OnMenuItemClickListener{

    String dbName = "user.db";
    int dbVersion = 3;
    private DBlink helper;
    private SQLiteDatabase db;

    ImageView writeBtn, menuBtn, profileImg;
    private String mId, mBabyname, imgpath;
    private Activity activity;
    private View view;
    private CircleImageView imageview;
    private TextView tvName;

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
                // This activity implements OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(getActivity(), "메뉴 터치 : " + item.getTitle(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        Log.d("menu1", "onCreateOptionsMenu: ");
        inflater.inflate(R.menu.menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_baby1 :
                Toast.makeText(getActivity(), "fragA", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }





    private void usingDB(ViewGroup container) {
        helper = new DBlink(container.getContext(), dbName, null, dbVersion);
        db = helper.getWritableDatabase();

    }

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



    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_baby1:
                Log.d("menu1", "baby1 터치");
                return true;
            case R.id.menu_babyplus:
                Log.d("menu1", "플러스 터치");
                return true;
            default:
                return false;
        }
    }
}