package com.example.baily;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

    private String mId,mBabyname,imgpath;

    private View view;
    private CircleImageView imageview;
    private TextView tvName;

    public static FragHome newInstance(){
        FragHome fragHome = new FragHome();
        return fragHome;

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_home, container, false);
        usingDB(container);
        getDBdata();

        imageview = (CircleImageView)view.findViewById(R.id.h_profileImg);
        tvName=(TextView)view.findViewById(R.id.h_bNameTxt);

        tvName.setText(mBabyname);

        try {
            Bitmap bm = BitmapFactory.decodeFile(imgpath);
            imageview.setImageBitmap(bm);
        } catch (Exception e) {
        }

        return view;
    }

    private void usingDB(ViewGroup container){
        helper = new DBlink(container.getContext(), dbName, null, dbVersion);
        db = helper.getWritableDatabase();

    }

    private void getDBdata(){
        String sql = "select * from thisusing where _id=1"; // 검색용
        Cursor cursor = db.rawQuery(sql, null);

        // 기본 데이터
        while (cursor.moveToNext()) {
            mId=cursor.getString(1);
            mBabyname=cursor.getString(2);
            Log.d("Home", "db받기 id = " +mId+"  현재 아기 = "+mBabyname);
        }

       // 현재 사용 아기데이터
        sql = "select * from baby where name='"+mBabyname+"'"; // 검색용
        cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            imgpath=cursor.getString(10);

            Log.d("Home", "db받기 path = " +imgpath);
        }


    }



}