package com.example.baily.main.diary;

public class EventData {
    private String name;
    private String memo;
    private String date;
    private int id;
    public EventData(String name, String date){
        this.name = name;
        this.date = date;
    }
    public EventData(String name, String date, String memo, int id){
        this(name,date);
        this.memo = memo;
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setId(int id) { this.id = id; }

    public String getDate() {
        return date;
    }

    public String getMemo() {
        return memo;
    }

    public String getName() {
        return name;
    }

    public int getId() { return id; }

    @Override
    public String toString() {
        return name+"|"+date+"|"+memo+"|"+id;
    }
}
