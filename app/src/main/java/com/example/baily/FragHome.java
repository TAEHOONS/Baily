package com.example.baily;

import android.content.Intent;
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

    private View view;
    String imgpath = "data/data/com.example.baily/files/";
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

        imageview = (CircleImageView)view.findViewById(R.id.h_profileImg);
        Log.d("Home", "id 연결 성공: ");


        imgpath = imgpath.concat("연수.jpg");
        Log.d("Home", "imgpath: " +imgpath);

        Log.d("Home", "이미지 가져오기");
        try {
            Bitmap bm = BitmapFactory.decodeFile(imgpath);
            imageview.setImageBitmap(bm);
            Log.d("Home", "이미지 성공: ");
        } catch (Exception e) {
            Log.d("Home", "이미지 불러오기 실패: ");
        }

        return view;
    }
}