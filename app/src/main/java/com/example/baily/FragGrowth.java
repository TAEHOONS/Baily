package com.example.baily;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class FragGrowth extends Fragment {
    private View view;

    public static FragGrowth newInstance(){
        FragGrowth fragGrowth = new FragGrowth();
        return fragGrowth;
    }

    private FragmentPagerAdapter growthFragmentPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_growth, container, false);

        ViewPager growViewPager = view.findViewById(R.id.growChartViewpager);
        growthFragmentPagerAdapter = new GrowthViewPagerAdapter(getChildFragmentManager());

        TabLayout growtabLayout = view.findViewById(R.id.growth_Tablayout);
        growViewPager.setAdapter(growthFragmentPagerAdapter);
        growtabLayout.setupWithViewPager(growViewPager);
        return view;
    }
}