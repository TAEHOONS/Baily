package com.example.baily.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.baily.R;

public class FragRecode extends Fragment {
    private View view;

    public static FragRecode newInstance(){
        FragRecode fragRecode = new FragRecode();
        return fragRecode;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_recode, container, false);
        return view;
    }
}