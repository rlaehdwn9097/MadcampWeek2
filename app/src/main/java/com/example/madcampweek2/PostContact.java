package com.example.madcampweek2;

import com.google.gson.annotations.SerializedName;

public class PostContact {

    @SerializedName("name")
    private String name;

    @SerializedName("number")
    private String number;

    @SerializedName("email")
    private String email;

    @Override
    public String toString() {
        return "PostContact{" +
                "name = " + name +
                ", number = " + number +
                ", email = " + email +
                "}";
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
}