package com.alya.aplikasilansia.data;

public class Reminder {
    private String title;
    private String day;
    private String time;
    private String desc;
    private String userId;



    private int icon;


    private String timestamp;

    public Reminder() {}

    public Reminder(String userId, String title, String day, String time, String desc, String timestamp, Integer icon){
        this.userId = userId;
        this.title = title;
        this.day = day;
        this.time = time;
        this.desc = desc;
        this.timestamp = timestamp;
        this.icon = icon;

    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

}
