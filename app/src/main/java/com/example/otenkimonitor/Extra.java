package com.example.otenkimonitor;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Extra {
    Timestamp date;
    String msg;
    public Extra(){}
    public Date getDate(){
        return date.toDate();
    }
    public String getMsg(){
        return msg;
    }
}
