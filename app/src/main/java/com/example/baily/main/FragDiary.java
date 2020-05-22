package com.example.baily.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.baily.R;

public class FragDiary extends Fragment {
    private View view;

    public static FragDiary newInstance(){
        FragDiary fragDiary = new FragDiary();
        return fragDiary;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_diary, container, false);
        return view;
    }
}
