package com.example.calendarv3.Models;

import java.util.Comparator;

public class FamilyEvent {
    private String familyID;
    private String date;
    private String time;
    private String title;
    private String desc;

    public FamilyEvent(String familyID, String date, String time, String title, String desc) {
        this.familyID = familyID;
        this.date = date;
        this.time = time;
        this.title = title;
        this.desc = desc;
    }

    public String getFamilyID() {
        return familyID;
    }

    public void setFamilyID(String familyID) {
        this.familyID = familyID;
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

    public static final Comparator<FamilyEvent> COMPARE_BY_COUNT = new Comparator<FamilyEvent>() {
        @Override
        public int compare(FamilyEvent lhs, FamilyEvent rhs) {
            int lhsHH = Integer.parseInt(lhs.getTime().substring(0,1));
            int lhsMM =  Integer.parseInt(lhs.getTime().substring(3,4));
            int rhsHH = Integer.parseInt(lhs.getTime().substring(0,1));
            int rhsMM =  Integer.parseInt(lhs.getTime().substring(3,4));

            if (lhsHH - rhsHH == 0){
                return lhsMM - rhsMM;
            }else return lhsHH - rhsHH;
        }
    };
}
