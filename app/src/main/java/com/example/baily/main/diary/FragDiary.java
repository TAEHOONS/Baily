package com.example.baily.main.diary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.baily.R;

public class FragDiary extends Fragment {
    private View v;
    int count = 1;
    public static FragDiary newInstance(){
        FragDiary fragDiary = new FragDiary();
        return fragDiary;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frag_diary, container, false);


        final Button diary_date = (Button) v.findViewById(R.id.diary_date);
        final Button diary_album = (Button) v.findViewById(R.id.diary_album);
        diary_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diary_album.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

                diary_date.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragD, new FragDiaryDate());
                fragmentTransaction.commit();

            }
        });

        diary_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diary_album.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                diary_date.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragD, new FragDiaryAlbum());
                fragmentTransaction.commit();

            }
        });
        if (count == 1){
            diary_album.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

            diary_date.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragD, new FragDiaryDate());
            fragmentTransaction.commit();

        }


        return v;


    }
}
