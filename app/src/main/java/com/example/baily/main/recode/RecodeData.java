package com.example.baily.main.recode;

public class RecodeData {

    private String time, value, dal;
    private int recodeId;


    public String getTime() {
        return time;
    }
    public String getValue() {
        return value;
    }
    public String getDal() {
        return dal;
    }
    public int getRecodeId() {
        return recodeId;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public void setDal(String dal) {
        this.dal = dal;
    }
    public void setRecodeId(int recodeId) { this.recodeId = recodeId; }


    public RecodeData(String time, String value, String dal,int recodeId) {
        this.time = time;
        this.value = value;
        this.dal = dal;
        this.recodeId = recodeId;
    }

}

