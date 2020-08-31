package com.htht.cn.jiaxing.model;


import java.util.Date;

public class User {
    private long user_id;
    private String user_name;
    private String user_password;
    private String gender;
    private String phone_number;
    private String address;
    private String organization;
    private int user_type;
    private Date regtime;

    public User(){}
    public User(int user_id, String user_name, String user_password, String gender, String phone_number, String address, String organization, int user_type,Date regtime) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_password = user_password;
        this.gender = gender;
        this.phone_number = phone_number;
        this.address = address;
        this.organization = organization;
        this.user_type = user_type;
        this.regtime = regtime;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public Date getRegtime() {
        return regtime;
    }

    public void setRegtime(Date regtime) {
        this.regtime = regtime;
    }
}
