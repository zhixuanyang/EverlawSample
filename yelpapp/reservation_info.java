package com.example.yelpapp;

public class reservation_info {
    private String id;
    private String business_name;
    private String res_email;
    private String res_date;
    private String res_time;
    public reservation_info(String id, String business_name, String res_email, String res_date, String res_time) {
        this.id = id;
        this.business_name = business_name;
        this.res_email = res_email;
        this.res_date = res_date;
        this.res_time = res_time;
    }
    public String getId() {
        return this.id;
    }
    public String getBusiness_name() {
        return this.business_name;
    }
    public String getRes_email() {
        return this.res_email;
    }
    public String getRes_time() {
        return this.res_time;
    }
    public String getRes_date() {
        return this.res_date;
    }
}
