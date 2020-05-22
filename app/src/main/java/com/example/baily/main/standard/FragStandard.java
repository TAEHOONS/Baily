package com.example.baily.main.standard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.baily.R;
import com.google.android.material.tabs.TabLayout;

public class FragStandard extends Fragment {
    private View view;

    public static FragStandard newInstance(){
        FragStandard fragStandard = new FragStandard();
        return fragStandard;
    }

    private FragmentPagerAdapter standardFragmentPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_standard, container, false);
        ViewPager standardViewPager = view.findViewById(R.id.standardChartViewpager);
        standardFragmentPagerAdapter = new StandardViewPagerAdapter(getChildFragmentManager());

        TabLayout standardtabLayout = view.findViewById(R.id.standard_Tablayout);
        standardViewPager.setAdapter(standardFragmentPagerAdapter);
        standardtabLayout.setupWithViewPager(standardViewPager);

        return view;
    }
}