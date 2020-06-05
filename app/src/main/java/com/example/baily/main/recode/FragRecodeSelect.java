package com.example.baily.main.recode;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.baily.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragRecodeSelect extends Fragment {
View v;
    AppCompatImageButton snurs, sbbfood, ssleep, spwmilk, sbowel, sdosage, stem, sbath, shealth, splay;
   AppCompatButton sall;
    public FragRecodeSelect() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.frag_recode_select, container, false);

        sall = v.findViewById(R.id.select_all);
        snurs = v.findViewById(R.id.select_nursing);
        sbbfood = v.findViewById(R.id.select_food);
        ssleep = v.findViewById(R.id.select_sleep);
        spwmilk = v.findViewById(R.id.select_milk);
        sbowel = v.findViewById(R.id.select_bower);
        sdosage = v.findViewById(R.id.select_drag);
        stem = v.findViewById(R.id.select_tem);
        sbath = v.findViewById(R.id.select_bath);
        shealth = v.findViewById(R.id.select_helth);
        splay = v.findViewById(R.id.select_play);

        sall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        snurs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        sbbfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        ssleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("select", "sleep");
                // Key, Value fragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                FragRecode fragRecode = new FragRecode();
                fragRecode.setArguments(bundle);

            }
        });
        spwmilk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        sbowel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        sdosage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        stem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        sbath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        shealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        splay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


        return v;
    }
}
