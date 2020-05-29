package com.example.baily.main.diary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.baily.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragDiaryAlbum extends Fragment {
View v;
    public FragDiaryAlbum() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.frag_diary_album, container, false);




return v;
    }
}
