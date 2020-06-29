package com.example.baily.main.standard;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.baily.R;

public class StandardViewPagerAdapter extends FragmentPagerAdapter {
    public StandardViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    Drawable mD1,mD2,mD3;
    Context context;
    SpannableStringBuilder sb1,sb2,sb3;
    ImageSpan span;
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
