package com.example.baily.main.diary;

import android.widget.ImageView;

public class DiaryItem {
    private String diaryTitle,recodeDay,diaryContents;
    private int diaryImg;

    public DiaryItem(String diaryTitle, String recodeDay,String diaryContents) {
        this.diaryTitle = diaryTitle;
        this.recodeDay = recodeDay;
        this.diaryContents = diaryContents;
        this.diaryImg = diaryImg;

    }

    public String getDiaryTitle() {
        return diaryTitle;
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

    public void setDiaryTitle(String recodeDay) {
        this.diaryTitle = diaryTitle;
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
