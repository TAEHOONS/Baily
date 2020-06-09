package com.example.baily.main.diary;

public class EventData {
    private String user;
    private String title;
    private String memo;
    private String date;
    private String parents;

    private int id;

    public EventData(String user, String title, String date, String parents){
        this.user = user;
        this.title = title;
        this.date = date;
        this.parents = parents;
    }
    public EventData(String user, String title, String date, String memo, String parents, int id){
        this(user,title,date,parents);
        this.memo = memo;
        this.id = id;
    }

    public void setUser(String user) {
        this.user = user;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }
    public void setParents(String parents) {
        this.parents = parents;
    }

    public void setId(int id) { this.id = id; }


    public String getUser() {
        return user;
    }
    public String getTitle() {
        return title;
    }
    public String getDate() {
        return date;
    }
    public String getMemo() {
        return memo;
    }
    public String getParents() {
        return parents;
    }

    public int getId() { return id; }

    @Override
    public String toString() {
        return user+"|"+title+"|"+date+"|"+memo+"|"+parents+"|"+id;
    }
}
