package com.example.baily.main;

import android.util.Log;

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
        Log.d("moveScreen", "getItem: VPA");
        switch (position){
            case 0:FragDiary fragDiary=new FragDiary();
                return fragDiary;
            case 1:FragRecode fragRecode=new FragRecode();
                return fragRecode;
            case 2:FragHome fragHome=new FragHome();
                return fragHome;
            case 3:FragGrowth fragGrowth=new FragGrowth();
                return fragGrowth;
            case 4:FragStandard fragStandard=new FragStandard();
                return fragStandard;
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