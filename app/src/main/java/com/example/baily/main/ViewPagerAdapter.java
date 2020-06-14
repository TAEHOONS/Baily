package com.example.baily.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.baily.main.diary.FragDiary;
import com.example.baily.main.growth.FragGrowth;
import com.example.baily.main.home.FragHome;
import com.example.baily.main.recode.FragRecode;
import com.example.baily.main.standard.FragStandard;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return FragDiary.newInstance();
            case 1:
                return FragRecode.newInstance();
            case 2:
                return FragHome.newInstance();
            case 3:
                return FragGrowth.newInstance();
            case 4:
                return FragStandard.newInstance();
            default:
                return null;
        }


    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Diary";
            case 1:
                return "Recode";
            case 2:
                return "Home";
            case 3:
                return "Growth";
            case 4:
                return "Standard";
            default:
                return null;
        }
    }
}