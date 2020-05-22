package com.example.baily.main.standard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class StandardViewPagerAdapter extends FragmentPagerAdapter {
    public StandardViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return ChildFragKg.newInstance();
            case 1:
                return ChildFragTall.newInstance();
            case 2:
                return ChildFragHead.newInstance();
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
                return "몸무게";
            case 1:
                return "신장";
            case 2:
                return "머리둘레";
            default:
                return null;
        }
    }
}
