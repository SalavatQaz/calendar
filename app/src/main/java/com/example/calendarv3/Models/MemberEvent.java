package com.example.calendarv3.Models;

public class MemberEvent {
    private String memberID;
    private String date;
    private String time;
    private String title;
    private String desc;

    public MemberEvent(String memberID, String date, String time, String title, String desc) {
        this.memberID = memberID;
        this.date = date;
        this.time = time;
        this.title = title;
        this.desc = desc;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
