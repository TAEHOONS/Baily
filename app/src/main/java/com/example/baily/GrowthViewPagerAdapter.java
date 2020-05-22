package com.example.baily;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class GrowthViewPagerAdapter extends FragmentPagerAdapter {
    public GrowthViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
        case 0:
            return ChildFragDay.newInstance();
        case 1:
            return ChildFragWeek.newInstance();
        case 2:
            return ChildFragMonth.newInstance();
        default:
            return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Day";
            case 1:
                return "Week";
            case 2:
                return "Month";
            default:
                return null;
        }
    }
}
