package com.example.administrator.cr_mqtt;

public class Bean {
    private String title;
    private String desc;
    private String time;
    private String phone;
//ListView的数据集
    public Bean() {
    }

    public Bean(String title, String desc, String time, String phone) {
        this.title = title;

        this.desc = desc;
        this.time = time;
        this.phone = phone;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}