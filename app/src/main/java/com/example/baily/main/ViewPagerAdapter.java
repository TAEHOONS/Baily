package com.example.baily.main;

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
import com.example.baily.main.diary.FragDiary;
import com.example.baily.main.growth.FragGrowth;
import com.example.baily.main.home.FragHome;
import com.example.baily.main.recode.FragRecode;
import com.example.baily.main.standard.FragStandard;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm, android.content.Context context) {
        super(fm);
        this.context=context;

    }
    Drawable myDrawable,mD1,mD2,mD3,mD4;
    String title;
    Context context;
    SpannableStringBuilder sb,sb1,sb2,sb3,sb4;
    ImageSpan span;
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
                myDrawable = ContextCompat.getDrawable(context,R.drawable.main_diary);
                sb = new SpannableStringBuilder(" "); // space added before text for convenience

                myDrawable.setBounds(0, 20, 100, 120);
                Log.d("ahfmrpTek", "getPageTitle: "+myDrawable.getIntrinsicHeight());
                span = new ImageSpan(myDrawable, ImageSpan.ALIGN_BASELINE);

                sb.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                return sb;
            case 1:
                mD1 = ContextCompat.getDrawable(context,R.drawable.main_record);
                sb1 = new SpannableStringBuilder(" "); // space added before text for convenience

                mD1.setBounds(0, 15, 90, 130);
                Log.d("ahfmrpTek", "getPageTitle: "+mD1.getIntrinsicHeight());
                span = new ImageSpan(mD1, ImageSpan.ALIGN_BASELINE);

                sb1.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                return sb1;
            case 2:
                mD2 = ContextCompat.getDrawable(context,R.drawable.main_home);
                sb2 = new SpannableStringBuilder(" "); // space added before text for convenience

                mD2.setBounds(0, 15, 100, 130);
                Log.d("ahfmrpTek", "getPageTitle: "+mD2.getIntrinsicHeight());
                span = new ImageSpan(mD2, ImageSpan.ALIGN_BASELINE);

                sb2.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                return sb2;
            case 3:
                mD3 = ContextCompat.getDrawable(context,R.drawable.main_growth);
                sb3 = new SpannableStringBuilder(" "); // space added before text for convenience

                mD3.setBounds(0, 15, 100, 140);
                Log.d("ahfmrpTek", "getPageTitle: "+mD3.getIntrinsicHeight());
                span = new ImageSpan(mD3, ImageSpan.ALIGN_BASELINE);

                sb3.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                return sb3;
            case 4:
                mD4 = ContextCompat.getDrawable(context,R.drawable.main_standardgrowth);
                sb4 = new SpannableStringBuilder(" "); // space added before text for convenience

                mD4.setBounds(0, 15, 100, 140);
                Log.d("ahfmrpTek", "getPageTitle: "+mD4.getIntrinsicHeight());
                span = new ImageSpan(mD4, ImageSpan.ALIGN_BASELINE);

                sb4.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                return sb4;
            default:
                return null;
        }

    }
}