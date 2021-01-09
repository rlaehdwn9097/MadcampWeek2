package com.example.madcampweek2;

import com.google.gson.annotations.SerializedName;

public class Contact implements Comparable<Contact> {

    @SerializedName("id")
    private int id;

    @SerializedName("userid")
    private String userid;

    @SerializedName("name")
    private String name;

    @SerializedName("number")
    private String number;

    @SerializedName("email")
    private String email;

    public Contact(int id, String userid, String nam, String num, String mail) {
        this.id = id;
        this.userid = userid;
        this.name = nam;
        this.number = num;
        this.email = mail;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id = " + id +
                ", userid = " + userid +
                ", name = " + name +
                ", number = " + number +
                ", email = " + email +
                "}";
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getUserid() {
        return userid;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public int compareTo(Contact o) {
        return name.compareTo(o.getName());
    }
}