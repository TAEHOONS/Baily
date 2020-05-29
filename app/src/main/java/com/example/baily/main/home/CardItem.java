package com.example.baily.main.home;

public class CardItem {


    private String kg,cm,head,fever,recodeDateNow,nowDday;

    public CardItem(String kg, String cm, String head, String fever, String recodeDateNow, String nowDday) {
        this.kg=kg;
        this.cm=cm;
        this.head=head;
        this.fever=fever;
        this.recodeDateNow=recodeDateNow;
        this.nowDday=nowDday;
    }


    public String getKg() {
        return kg;
    }

    public String getCm() {
        return cm;
    }

    public String getHead() {
        return head;
    }

    public String getfever() {
        return fever;
    }

    public String getNowDday() {
        return nowDday;
    }

    public String getRecodeDateNow() {
        return recodeDateNow;
    }

    public void setCm(String cm) {
        this.cm = cm;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public void setKg(String kg) {
        this.kg = kg;
    }

    public void setfever(String fever) {
        this.fever = fever;
    }

    public void setNowDday(String nowDday) {
        this.nowDday = nowDday;
    }

    public void setRecodeDateNow(String recodeDateNow) {
        this.recodeDateNow = recodeDateNow;
    }
}