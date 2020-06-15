package com.example.baily.main.recode;

public class RecodeData {

    private String time,value,dal;

    
    public String getTime() {
        return time;
    }
    public String getValue() {
        return value;
    }
    public String getDal() {
        return dal;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public void setDal(String value) {
        this.value = value;
    }



    public RecodeData(String time, String value,String dal) {
        this.time = time;
        this.value = value;
        this.dal=dal;
    }

}

