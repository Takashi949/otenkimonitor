package com.example.otenkimonitor;

import com.google.firebase.Timestamp;

import java.util.Date;

public class CmdResult {
    String msg;
    Timestamp date;
    public CmdResult(){}

    public String getMsg() {
        return msg;
    }

    public Date getDate() {
        return date.toDate();
    }
}
