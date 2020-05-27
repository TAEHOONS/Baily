package com.example.baily.main.recode;

public class RecodeData {




    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String value;


    public RecodeData(String time, String value) {
        this.time = time;
        this.value = value;
    }

}

