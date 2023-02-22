package com.vishugahlot.kcians;

public class followdata {
    String uid,date;

    public followdata(String uid, String date) {
        this.uid = uid;
        this.date = date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
