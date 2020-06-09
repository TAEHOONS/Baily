package com.example.baily.main.diary;

import android.widget.ImageView;

public class DiaryItem {
    private String recodeDay,diaryContents;
    private int diaryImg;

    public DiaryItem(String recodeDay,String diaryContents,int diaryImg) {
        this.recodeDay = recodeDay;
        this.diaryContents = diaryContents;
        this.diaryImg = diaryImg;

    }


    public String getRecodeDay() {
        return recodeDay;
    }

    public String getDiaryContents() {
        return diaryContents;
    }

    public int getDiaryImg() {
        return diaryImg;
    }

    public void setRecodeDay(String recodeDay) {
        this.recodeDay = recodeDay;
    }

    public void setDiaryContents(String diaryContents) {
        this.diaryContents = diaryContents;
    }

    public void setDiaryImg(int diaryImg) {
        this.diaryImg = diaryImg;
    }
}
